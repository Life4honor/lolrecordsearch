package com.lolsearch.lolrecordsearch.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lolsearch.lolrecordsearch.domain.*;
import com.lolsearch.lolrecordsearch.dto.*;
import com.lolsearch.lolrecordsearch.repository.ChampionRepository;
import com.lolsearch.lolrecordsearch.repository.LeaguePositionRepository;
import com.lolsearch.lolrecordsearch.repository.ParticipantIdentityRepository;
import com.lolsearch.lolrecordsearch.repository.ParticipantRepository;
import com.lolsearch.lolrecordsearch.service.RecordService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.*;

@Service
public class RecordServiceImpl implements RecordService {

    @Autowired
    ParticipantIdentityRepository participantIdentityRepository;

    @Autowired
    ParticipantRepository participantRepository;

    @Autowired
    ChampionRepository championRepository;

    @Autowired
    LeaguePositionRepository leaguePositionRepository;

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

    private ResultDTO createResultDTO(Match match, Champion champion) {
        ResultDTO resultDTO = new ResultDTO();
//        BeanUtils.copyProperties(match,resultDTO);
        resultDTO.setChampion(champion);
        resultDTO.setGameId(match.getMatchReference().getGameId());
        resultDTO.setRole(match.getMatchReference().getRole());
        resultDTO.setKills(match.getKills());
        resultDTO.setDeaths(match.getDeaths());
        resultDTO.setAssists(match.getAssists());
        resultDTO.setWin(match.getWin());
        return resultDTO;
    }

    @Override
    public void setRecordDTO(RecordDTO recordDTO, ParticipantIdentity participantIdentity, Participant participant) {
        recordDTO.setParticipantIdentity(participantIdentity);
        recordDTO.setParticipant(participant);
    }

    @Override
    @Transactional
    public LeaguePosition getLeaguePositionByNameAndQueueType(String name, String queueType) {
        return leaguePositionRepository.findLeaguePositionByPlayerOrTeamNameAndQueueType(name, queueType);
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
//        leaguePosition.setWins(leaguePositionDTO.getWins());
//        leaguePosition.setTier(leaguePositionDTO.getTier());
//        leaguePosition.setRank(leaguePositionDTO.getRank());
//        leaguePosition.setQueueType(leaguePositionDTO.getQueueType());
//        leaguePosition.setPlayerOrTeamName(leaguePositionDTO.getPlayerOrTeamName());
//        leaguePosition.setPlayerOrTeamId(leaguePositionDTO.getPlayerOrTeamId());
//        leaguePosition.setLosses(leaguePositionDTO.getLosses());
//        leaguePosition.setLeaguePoints(leaguePositionDTO.getLeaguePoints());
//        leaguePosition.setLeagueName(leaguePositionDTO.getLeagueName());
//        leaguePosition.setLeagueId(leaguePositionDTO.getLeagueId());
        BeanUtils.copyProperties(leaguePositionDTO, leaguePosition);
        return leaguePositionRepository.save(leaguePosition);
    }

    @Override
    @Transactional
    public List<LeaguePosition> getLeaguePositionList(@RequestParam(name = "summoner") List<String> summoners) {
        List<LeaguePosition> leaguePositionList = new ArrayList<>();
        summoners.forEach(s -> {
            LeaguePosition leaguePositionSolo = getLeaguePositionByNameAndQueueType(s,"RANKED_SOLO_5x5");
//            LeaguePosition leaguePositionSolo = getLeaguePositionByNameAndQueueType(s.replaceAll(" ",""),"RANKED_SOLO_5x5");
            LeaguePosition leaguePositionFlex = getLeaguePositionByNameAndQueueType(s,"RANKED_FLEX_SR");
//            LeaguePosition leaguePositionFlex = getLeaguePositionByNameAndQueueType(s.replaceAll(" ",""),"RANKED_FLEX_SR");
            if(leaguePositionSolo != null){
                leaguePositionList.add(leaguePositionSolo);
            }
            if(leaguePositionFlex != null){
                leaguePositionList.add(leaguePositionFlex);
            }
        });
        return leaguePositionList;
    }

    @Override
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

        summoner.getMatches().forEach(match -> {
            ResultDTO resultDTO = createResultDTO(match, championMap.get(match.getMatchReference().getChampionId()));
            resultDTOList.add(resultDTO);
        });

        return resultDTOList;
    }

    @Override
    public List<List<PlayerDTO>> getPlayerDTOListResult(List<ResultDTO> resultDTOList) {
        List<List<PlayerDTO>> playerDTOListResult = new ArrayList<>();
        List<PlayerDTO> playerDTOList = new ArrayList<>();
        resultDTOList.forEach(resultDTO -> {
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
}
