package com.lolsearch.lolrecordsearch.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lolsearch.lolrecordsearch.domain.elasticsearch.SummonerElastic;
import com.lolsearch.lolrecordsearch.domain.jpa.*;
import com.lolsearch.lolrecordsearch.dto.*;
import com.lolsearch.lolrecordsearch.repository.elasticsearch.SummonerElasticRepository;
import com.lolsearch.lolrecordsearch.repository.jpa.*;
import com.lolsearch.lolrecordsearch.service.RecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@Slf4j
public class RecordServiceImpl implements RecordService {
    private static final int BRINGNUMBER = 5;

    @Value("${apiKey}")
    private String apiKey;

    @Value("${summonerPath}")
    private String summonerPath;

    @Value("${matchListPath}")
    private String matchListPath;

    @Value("${matchPath}")
    private String matchPath;

    @Value("${championPath}")
    private String championPath;

    @Value("${leaguePositionPath}")
    private String leaguePositionPath;

//    @Autowired
//    SummonerElasticRepository summonerElasticRepository;

    @Autowired
    ParticipantIdentityRepository participantIdentityRepository;

    @Autowired
    SummonerRepository summonerRepository;

    @Autowired
    ParticipantRepository participantRepository;

    @Autowired
    ChampionRepository championRepository;

    @Autowired
    LeaguePositionRepository leaguePositionRepository;

    @Autowired
    MatchReferenceRepository matchReferenceRepository;

    @Autowired
    MatchRepository matchRepository;

    @Override
    @Transactional
    public Match saveMatch(Match match) {
        return matchRepository.save(match);
    }

    @Override
    @Transactional
    public Match getMatch(Long gameId, Long summonerId) {
        return matchRepository.findMatchByMatchReference_GameIdAndSummoner_Id(gameId, summonerId);
    }

    @Override
    @Transactional
    public Champion getChampionById(Long id) {
        return championRepository.findChampionById(id);
    }

    @Override
    @Transactional
    public Champion saveChampion(ChampionDTO championDTO) {
        Champion champion = new Champion();
        champion.setId(Long.valueOf(championDTO.getId()));
        champion.setName(championDTO.getName());
        return championRepository.save(champion);
    }

    @Override
    @Transactional(readOnly = true)
    public MatchReference getMatchReferencByGameIdAndChampionId(Long gameId, Long championId) {
        return matchReferenceRepository.findMatchReferenceByGameIdAndChampionId(gameId, championId);
    }

    @Override
    @Transactional
    public MatchReference saveMatchReference(MatchReferenceDTO matchReferenceDTO) {
        MatchReference matchReference = new MatchReference();
        matchReference.setChampionId(matchReferenceDTO.getChampion());
        matchReference.setGameId(matchReferenceDTO.getGameId());
        matchReference.setLane(matchReferenceDTO.getLane());
        matchReference.setPlatformId(matchReferenceDTO.getPlatformId());
        matchReference.setQueue(matchReferenceDTO.getQueue());
        matchReference.setRole(matchReferenceDTO.getRole());
        matchReference.setSeason(matchReferenceDTO.getSeason());
        matchReference.setTimestamp(matchReferenceDTO.getTimestamp());
        return matchReferenceRepository.save(matchReference);
    }

    @Override
    @Transactional(readOnly = true)
    public Summoner getSummonerByName(String name){

        return summonerRepository.findSummonerByNameIgnoreCase(name);
    }

    @Override
    @Transactional
    public Summoner saveSummoner(SummonerDTO summonerDTO) {
        Summoner summoner = new Summoner();
        summoner.setAccountId(summonerDTO.getAccountId());
        summoner.setId(summonerDTO.getId());
        summoner.setSummonerLevel(summonerDTO.getSummonerLevel());
        summoner.setRevisionDate(summonerDTO.getRevisionDate());
        summoner.setProfileIconId(summonerDTO.getProfileIconId());
        summoner.setName(summonerDTO.getName());
        return summonerRepository.save(summoner);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ParticipantIdentity> getParticipantIdentitiesByGameId(Long gameId) {
        return participantIdentityRepository.findParticipantIdentitiesByGameIdOrderByParticipantId(gameId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Participant> getParticipantsByGameId(Long gameId) {
        return participantRepository.findParticipantsByGameIdOrderByParticipantId(gameId);
    }

    private ResultDTO createResultDTO(Match match, Champion champion, Long timestamp) {
        ResultDTO resultDTO = new ResultDTO();
//        BeanUtils.copyProperties(match,resultDTO);
        resultDTO.setChampion(champion);
        resultDTO.setGameId(match.getMatchReference().getGameId());
        resultDTO.setRole(match.getMatchReference().getRole());
        resultDTO.setKills(match.getKills());
        resultDTO.setDeaths(match.getDeaths());
        resultDTO.setAssists(match.getAssists());
        resultDTO.setWin(match.getWin());
        resultDTO.setTimestamp(timestamp);
        resultDTO.setStrDatetime(timestamp);
        return resultDTO;
    }

    @Override
    public void setRecordDTO(RecordDTO recordDTO, ParticipantIdentity participantIdentity, Participant participant) {
        recordDTO.setParticipantIdentity(participantIdentity);
        recordDTO.setParticipant(participant);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LeaguePosition> getLeaguePositionsByName(String name) {
        return leaguePositionRepository.findLeaguePositionsByPlayerOrTeamName(name);
    }

    @Override
    @Transactional
    public Participant saveParticipant(Participant participant) {
        return participantRepository.save(participant);
    }

    @Override
    @Transactional
    public ParticipantIdentity saveParticipantIdentity(ParticipantIdentity participantIdentity) {
        return participantIdentityRepository.save(participantIdentity);
    }

    @Override
    @Transactional
    public LeaguePosition saveLeaguePosition(LeaguePositionDTO leaguePositionDTO) {
        LeaguePosition leaguePosition = new LeaguePosition();
        BeanUtils.copyProperties(leaguePositionDTO, leaguePosition);
        return leaguePositionRepository.save(leaguePosition);
    }

    @Override
    @Transactional(readOnly = true)
    public List<List<LeaguePosition>> getLeaguePositionListResult(@RequestParam(name = "summoner") List<String> summoners) {
        List<List<LeaguePosition>> leaguePositionListResult = new ArrayList<>();
        summoners.forEach(s -> {
            String summonerName = getSummonerByName(s).getName();
            List<LeaguePosition> leaguePositionList = getLeaguePositionsByName(summonerName);
            List<String> queueTypeList = new ArrayList<>();
            for (LeaguePosition leaguePosition : leaguePositionList) {
                queueTypeList.add(leaguePosition.getQueueType());
            }
            if(!queueTypeList.contains("RANKED_SOLO_5x5")){
                LeaguePosition leaguePosition = new LeaguePosition();
                leaguePosition.setPlayerOrTeamName(summonerName);
                leaguePosition.setTier("unranked");
                leaguePosition.setQueueType("RANKED_SOLO_5x5");
                leaguePosition.setRank("");
                leaguePositionList.add(leaguePosition);
            }
            if(!queueTypeList.contains("RANKED_FLEX_SR")){
                LeaguePosition leaguePosition = new LeaguePosition();
                leaguePosition.setPlayerOrTeamName(summonerName);
                leaguePosition.setTier("unranked");
                leaguePosition.setQueueType("RANKED_FLEX_SR");
                leaguePosition.setRank("");
                leaguePositionList.add(leaguePosition);
            }

            leaguePositionListResult.add(leaguePositionList);
        });
        return leaguePositionListResult;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResultDTO> getResultDTOList(Summoner summoner) {
        List<ResultDTO> resultDTOList = new ArrayList<>();
        Map<Long, Champion> championMap = new HashMap<>();

        List<Long> championIdList = new ArrayList<>();
        summoner.getMatches().forEach(match -> {
            championIdList.add(match.getMatchReference().getChampionId());
        });

        List<Champion> championList = championRepository.findAllById(championIdList);
        for (Champion champion : championList) {
            championMap.put(champion.getId(), champion);
        }
        for(Match match : summoner.getMatches()){
            ResultDTO resultDTO = createResultDTO(match, championMap.get(match.getMatchReference().getChampionId()), match.getMatchReference().getTimestamp());
            if(!resultDTOList.contains(resultDTO)) {
                resultDTOList.add(resultDTO);
            }
        }

        return resultDTOList;
    }

    @Override
    @Transactional(readOnly = true)
    public List<List<PlayerDTO>> getPlayerDTOListResult(List<ResultDTO> resultDTOList) {
        List<List<PlayerDTO>> playerDTOListResult = new ArrayList<>();
        resultDTOList.forEach(resultDTO -> {
            List<PlayerDTO> playerDTOList = new ArrayList<>();
            Long gameId = resultDTO.getGameId();
            //DB 조회
            List<ParticipantIdentity> participantIdentities = getParticipantIdentitiesByGameId(gameId);
            //DB 조회
            List<Participant> participants = getParticipantsByGameId(gameId);

            if (participants.size() > 0 && participantIdentities.size() >0) {
                for (int i = 0; i < participants.size(); i++) {
                    RecordDTO recordDTO = new RecordDTO();
                    setRecordDTO(recordDTO, participantIdentities.get(i), participants.get(i));

                    //String -> JSON -> Object
                    String jsonString = recordDTO.getParticipantIdentity().getPlayer();
                    ObjectMapper mapper = new ObjectMapper();
                    try {
                        JsonNode actualObj = mapper.readTree(jsonString);
                        PlayerDTO playerDTO = mapper.readValue(actualObj.traverse(), PlayerDTO.class);
                        playerDTOList.add(playerDTO);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            playerDTOListResult.add(playerDTOList);
        });
        return playerDTOListResult;
    }


    @Override
    @Transactional
    public List<String> saveRecords(String type, List<String> summoners, int beginIndex){

        List<String> savedSummonerList = new ArrayList<>();
        RestTemplate restTemplate = new RestTemplate();
        for(String summonerName : summoners) {
            SummonerDTO summonerDTO;
            try {
                summonerDTO = restTemplate.getForObject(summonerPath + summonerName + "?api_key=" + apiKey, SummonerDTO.class);
//                SummonerElastic summonerElastic = new SummonerElastic();
//                BeanUtils.copyProperties(summonerDTO, summonerElastic);
//                summonerElasticRepository.save(summonerElastic);
                Summoner summoner = new Summoner();
                BeanUtils.copyProperties(summonerDTO, summoner);
                summonerRepository.save(summoner);
            }catch (Exception e){
                String errorMsg = e.getMessage();
                if(errorMsg.contains("403")){
                    throw new RuntimeException("API Key has been Expired");
                }
                log.error("",e);
                continue;
            }
            savedSummonerList.add(summonerName);
            Summoner summoner = summonerRepository.findSummonerByNameIgnoreCase(summonerName);
            if(summoner == null) {
                summoner = saveSummoner(summonerDTO);
            }

            int endIndex= beginIndex+BRINGNUMBER;

            MatchlistDTO matchlistDTO = restTemplate.getForObject(matchListPath+summoner.getAccountId()+"?beginIndex="+beginIndex+"&endIndex="+endIndex+"&api_key=" + apiKey,MatchlistDTO.class);
            List<MatchReferenceDTO> matchReferenceDTOList = matchlistDTO.getMatches();
            for(MatchReferenceDTO matchReferenceDTO: matchReferenceDTOList) {
                MatchReference matchReference = getMatchReferencByGameIdAndChampionId(matchReferenceDTO.getGameId(), matchReferenceDTO.getChampion());
                if(matchReference == null || matchReferenceDTO.getChampion() != matchReference.getChampionId()) {
                    matchReference = saveMatchReference(matchReferenceDTO);
                }
                MatchDTO matchDTO = restTemplate.getForObject(matchPath+matchReference.getGameId()+"?api_key="+apiKey, MatchDTO.class);

                List<ParticipantIdentityDTO> participantIdentityDTOList = matchDTO.getParticipantIdentities();
                for(ParticipantIdentityDTO participantIdentityDTO : participantIdentityDTOList){
                    ParticipantIdentity participantIdentity = new ParticipantIdentity();
                    participantIdentity.setGameId(matchReference.getGameId());
                    participantIdentity.setParticipantId(Long.valueOf(participantIdentityDTO.getParticipantId()));
                    PlayerDTO playerDTO = participantIdentityDTO.getPlayer();
                    ObjectMapper mapper = new ObjectMapper();
                    try {
                        participantIdentity.setPlayer(mapper.writeValueAsString(playerDTO));
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                    List<ParticipantIdentity> participantIdentityCheck = getParticipantIdentitiesByGameId(participantIdentity.getGameId());
                    if(participantIdentityCheck.size() < 10) {
                        saveParticipantIdentity(participantIdentity);
                    }
                }

                Match match = new Match();
                List<ParticipantDTO> participantDTOList = matchDTO.getParticipants();
                for(ParticipantDTO participantDTO : participantDTOList){
                    Participant participant = new Participant();
                    participant.setChampionId(participantDTO.getChampionId());
                    participant.setParticipantId((long) participantDTO.getParticipantId());
                    ParticipantStatsDTO participantStatsDTO = participantDTO.getStats();
                    ObjectMapper mapper = new ObjectMapper();
                    try {
                        participant.setStats(mapper.writeValueAsString(participantStatsDTO));
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                    participant.setGameId(matchReferenceDTO.getGameId());
                    participant.setTeam_id(participantDTO.getTeamId());
                    List<Participant> participantCheck = getParticipantsByGameId(participant.getGameId());
                    if(participantCheck.size()<10) {
                        saveParticipant(participant);
                    }

                    if(matchReferenceDTO.getChampion() == participantDTO.getChampionId()){
                        Champion champion =getChampionById(matchReferenceDTO.getChampion());
                        if(champion == null) {
                            ChampionDTO championDTO = restTemplate.getForObject(championPath+matchReferenceDTO.getChampion()+"?api_key="+apiKey, ChampionDTO.class);
                            saveChampion(championDTO);
                        }
                        match.setKills(participantDTO.getStats().getKills());
                        match.setAssists(participantDTO.getStats().getAssists());
                        match.setDeaths(participantDTO.getStats().getDeaths());

                        if(participantDTO.getStats().getWin()){
                            match.setWin("Win");
                        }else{
                            match.setWin("Lose");
                        }
                    }
                }
                match.setMatchReference(matchReference);
                match.setSummoner(summoner);
                Match matchCheck = getMatch(matchReference.getGameId(), summoner.getId());
                if(matchCheck == null) {
                    saveMatch(match);
                }
            }

            ParameterizedTypeReference<List<LeaguePositionDTO>> myBean =
                    new ParameterizedTypeReference<List<LeaguePositionDTO>>() {};

            ResponseEntity<List<LeaguePositionDTO>> response =
                    restTemplate.exchange(leaguePositionPath + summoner.getId() + "?api_key=" + apiKey, HttpMethod.GET, null, myBean);

            List<LeaguePositionDTO> leaguePositionDTOList = response.getBody();
            List<LeaguePosition> leaguePositionList = getLeaguePositionsByName(summoner.getName());
            List<String> queueTypeList = new ArrayList<>();
            for (LeaguePosition leaguePosition : leaguePositionList) {
                queueTypeList.add(leaguePosition.getQueueType());
            }
            for(LeaguePositionDTO leaguePositionDTO: leaguePositionDTOList){
                if(!queueTypeList.contains(leaguePositionDTO.getQueueType())){
                    saveLeaguePosition(leaguePositionDTO);
                }
            }
        }
        return savedSummonerList;
    }
}
