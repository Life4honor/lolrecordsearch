package com.lolsearch.lolrecordsearch.service.impl;

import com.lolsearch.lolrecordsearch.domain.Match;
import com.lolsearch.lolrecordsearch.domain.Participant;
import com.lolsearch.lolrecordsearch.domain.ParticipantIdentity;
import com.lolsearch.lolrecordsearch.dto.RecordDTO;
import com.lolsearch.lolrecordsearch.dto.ResultDTO;
import com.lolsearch.lolrecordsearch.repository.ChampionRepository;
import com.lolsearch.lolrecordsearch.repository.ParticipantIdentityRepository;
import com.lolsearch.lolrecordsearch.repository.ParticipantRepository;
import com.lolsearch.lolrecordsearch.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecordServiceImpl implements RecordService {

    @Autowired
    ParticipantIdentityRepository participantIdentityRepository;

    @Autowired
    ParticipantRepository participantRepository;

    @Autowired
    ChampionRepository championRepository;

    @Override
    public List<ParticipantIdentity> getParticipantIdentitiesByGameId(Long gameId) {
        return participantIdentityRepository.findParticipantIdentitiesByGameIdOrderByParticipantId(gameId);
    }

    @Override
    public List<Participant> getParticipantsByGameId(Long gameId) {
        return participantRepository.findParticipantsByGameIdOrderByParticipantId(gameId);
    }

    @Override
    public void setResultDTO(Match match, ResultDTO resultDTO) {
        resultDTO.setGameId(match.getMatchReference().getGameId());
        resultDTO.setChampion(championRepository.findChampionById(match.getMatchReference().getChampion()));
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
}
