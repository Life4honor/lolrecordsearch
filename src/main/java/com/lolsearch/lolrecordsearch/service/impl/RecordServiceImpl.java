package com.lolsearch.lolrecordsearch.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
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
import java.util.ArrayList;
import java.util.List;

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

    @Override
    public void setResultDTO(Match match, ResultDTO resultDTO) {
        resultDTO.setGameId(match.getMatchReference().getGameId());
        resultDTO.setChampion(championRepository.findChampionById(match.getMatchReference().getChampionId()));
        resultDTO.setRole(match.getMatchReference().getRole());
        resultDTO.setKills(match.getKills());
        resultDTO.setDeaths(match.getDeaths());
        resultDTO.setAssists(match.getAssists());
        resultDTO.setWin(match.getWin());
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
    public LeaguePosition saveLeaguePosition(LeaguePositionDTO leaguePositionDTO) {
        LeaguePosition leaguePosition = new LeaguePosition();
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
        summoner.getMatches().forEach(match -> {
            ResultDTO resultDTO = new ResultDTO();
            setResultDTO(match, resultDTO);
            resultDTOList.add(resultDTO);
        });
        return resultDTOList;
    }

    @Override
    public List<PlayerDTO> getPlayerDTOList(List<ResultDTO> resultDTOList) {
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
        });
        return playerDTOList;
    }
}
