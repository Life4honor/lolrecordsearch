package com.lolsearch.lolrecordsearch.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lolsearch.lolrecordsearch.domain.*;
import com.lolsearch.lolrecordsearch.dto.*;
import com.lolsearch.lolrecordsearch.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/records")
public class RecordController {
//    public static void main(String[] args) {
//        Calendar cal = Calendar.getInstance();
//        Date revisionDate = cal.getTime();
//        LocalDateTime currentDateTime = LocalDateTime.now();
//        log.info(Long.valueOf(currentDateTime.toEpochSecond(ZoneOffset.MAX)).toString());
//        log.info(Long.valueOf(currentDateTime.minusWeeks(1L).toEpochSecond(ZoneOffset.MAX)).toString());
//        log.info(Long.valueOf(currentDateTime.minusDays(6L).toEpochSecond(ZoneOffset.MAX)).toString());
//    }

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

    @Autowired
    SummonerService summonerService;

    @Autowired
    RecordService recordService;

    @Autowired
    ChampionService championService;

    @Autowired
    MatchReferenceService matchReferenceService;

    @Autowired
    MatchService matchService;

    @GetMapping
    public String recordSearch(){
        return "recordSearch/recordSearchForm";
    }

    @PutMapping("/result")
    public String refreshResult(@RequestParam(name = "type") String type,
                                @RequestParam(name = "summoner") List<String> summoners,
                                ModelMap modelMap){

        return recordResult(type, summoners, modelMap);
    }

    @PostMapping("/result")
    public String postResult(@RequestParam(name = "type") String type,
                             @RequestParam(name = "summoner") List<String> summoners,
                             ModelMap modelMap){

        // API요청해서 DB 저장
        // 필요한 객체 : ResultDTOList, PlayerDTOList, // LeaguePosition, Summoner, Match, ParticipantIdentity, Participant
        RestTemplate restTemplate = new RestTemplate();
        for(String summonerName : summoners) {
            SummonerDTO summonerDTO = restTemplate.getForObject(summonerPath + summonerName + "?api_key=" + apiKey, SummonerDTO.class);
            Summoner summoner = summonerService.getSummonerByName(summonerName);
            if(summonerDTO==null){
                return "recordSearch/recordSearchError";
            }
            // List로 한꺼번에 저장하기??
            if(summoner == null) {
                summoner = summonerService.saveSummoner(summonerDTO);
            }

            MatchlistDTO matchlistDTO = restTemplate.getForObject(matchListPath+summoner.getAccountId()+"?beginIndex=0&endIndex=5&api_key=" + apiKey,MatchlistDTO.class);
            List<MatchReferenceDTO> matchReferenceDTOList = matchlistDTO.getMatches();
            for(MatchReferenceDTO matchReferenceDTO: matchReferenceDTOList) {
                MatchReference matchReference = matchReferenceService.getMatchReferencByGameId(matchReferenceDTO.getGameId());
                // List로 한꺼번에 저장하기??
                if(matchReference == null || matchReferenceDTO.getChampion() != matchReference.getChampionId()) {
                    matchReference = matchReferenceService.saveMatchReference(matchReferenceDTO);
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
                        e.printStackTrace();
                    }
                    // List로 한꺼번에 저장하기
                    List<ParticipantIdentity> participantIdentityCheck = recordService.getParticipantIdentitiesByGameId(participantIdentity.getGameId());
                    if(participantIdentityCheck.size() < 10) {
                        recordService.saveParticipantIdentity(participantIdentity);
                    }
                }

                Match match = new Match();
                List<ParticipantDTO> participantDTOList = matchDTO.getParticipants();
                for(ParticipantDTO participantDTO : participantDTOList){
                    Participant participant = new Participant();
                    participant.setChampionId(participantDTO.getChampionId());
                    participant.setParticipantId(Long.valueOf(participantDTO.getParticipantId()));
                    ParticipantStatsDTO participantStatsDTO = participantDTO.getStats();
                    ObjectMapper mapper = new ObjectMapper();
                    try {
                        participant.setStats(mapper.writeValueAsString(participantStatsDTO));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    participant.setGameId(matchReferenceDTO.getGameId());
                    participant.setTeam_id(participantDTO.getTeamId());
                    List<Participant> participantCheck = recordService.getParticipantsByGameId(participant.getGameId());
                    if(participantCheck.size()<10) {
                        recordService.saveParticipant(participant);
                    }

                    if(matchReferenceDTO.getChampion() == participantDTO.getChampionId()){
                        if(championService.getChampionById(matchReferenceDTO.getChampion()) == null) {
                            ChampionDTO championDTO = restTemplate.getForObject(championPath+matchReferenceDTO.getChampion()+"?api_key="+apiKey, ChampionDTO.class);
                            championService.saveChampion(championDTO);
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
                Match matchCheck = matchService.getMatch(matchReference.getGameId(), summoner.getId());
                if(matchCheck == null) {
                    matchService.saveMatch(match);
                }
            }

            ParameterizedTypeReference<List<LeaguePositionDTO>> myBean =
                    new ParameterizedTypeReference<List<LeaguePositionDTO>>() {};

            ResponseEntity<List<LeaguePositionDTO>> response =
                    restTemplate.exchange(leaguePositionPath + summoner.getId() + "?api_key=" + apiKey, HttpMethod.GET, null, myBean);

            List<LeaguePositionDTO> leaguePositionDTOList = response.getBody();
            //List로 한꺼번에 저장하기
            for(LeaguePositionDTO leaguePositionDTO: leaguePositionDTOList){
                LeaguePosition leaguePosition = recordService.getLeaguePositionByNameAndQueueType(leaguePositionDTO.getPlayerOrTeamName(), leaguePositionDTO.getQueueType());
                if(leaguePosition == null) {
                    recordService.saveLeaguePosition(leaguePositionDTO);
                }
            }
        }
        return recordResult(type, summoners, modelMap);
    }

    @GetMapping("/result")
    public String recordResult(@RequestParam(name = "type") String type,
                               @RequestParam(name = "summoner") List<String> summoners,
                               ModelMap modelMap) {
        // DB 에서 소환사 명 조회
        if(summoners == null){
            return "recordSearch/recordSearchError";
        }
        modelMap.addAttribute("type", type);

        if("single".equals(type)) {
            //게임 상세 정보 출력
            Summoner summoner = summonerService.getSummonerByName(summoners.get(0));
            if(summoner == null){
                return postResult(type,summoners,modelMap);
            }
            if(summoner != null) {
//                Date revisionDate = new Date(summoner.getRevisionDate());
//                LocalDateTime currentDateTime = LocalDateTime.now();
//                boolean timeCheck = revisionDate.toInstant().getEpochSecond() < currentDateTime.minusWeeks(1L).toEpochSecond(ZoneOffset.MAX);
//                if (timeCheck) {
//                    return refreshResult(type, summoners, modelMap);
//                }
            }else{
                return postResult(type, summoners, modelMap);
            }

            List<ResultDTO> resultDTOList = recordService.getResultDTOList(summoner);
            List<List<PlayerDTO>> playerDTOListResult = recordService.getPlayerDTOListResult(resultDTOList);
            modelMap.addAttribute("playersListResult", playerDTOListResult);
            modelMap.addAttribute("matches", resultDTOList);

            //소환사 통계 정보 출력
            //LeaguePosition Entity에서 가져와서 출력.
            List<LeaguePosition> leaguePositionList = recordService.getLeaguePositionList(summoners);
            while(leaguePositionList.size()<2){
                LeaguePosition leaguePosition = new LeaguePosition();
                leaguePosition.setPlayerOrTeamName(summoner.getName());
                leaguePosition.setTier("unranked");
                if(leaguePositionList.size()<1) {
                    leaguePosition.setQueueType("RANKED_SOLO_5x5");
                }else{
                    leaguePosition.setQueueType("RANKED_FLEX_SR");
                }
                leaguePosition.setRank("");
                leaguePositionList.add(leaguePosition);
            }
            modelMap.addAttribute("leaguePositions", leaguePositionList);

            return "recordSearch/recordSearchResult";
        }else if("multi".equals(type) && summoners.size() >0){
            // 멀티서치 -> 소환사들 통계 정보 출력
            List<LeaguePosition> leaguePositionList = recordService.getLeaguePositionList(summoners);
            if(leaguePositionList==null){
                return "recordSearch/recordSearchError";
            }
            modelMap.addAttribute("leaguePositions", leaguePositionList);
            return "recordSearch/recordSearchResult";
        }else{
            return "recordSearch/recordSearchError";
        }
    }
}