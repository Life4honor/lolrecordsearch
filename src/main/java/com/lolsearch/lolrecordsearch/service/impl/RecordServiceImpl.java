package com.lolsearch.lolrecordsearch.service.impl;

import com.lolsearch.lolrecordsearch.domain.jpa.LeaguePosition;
import com.lolsearch.lolrecordsearch.domain.jpa.Match;
import com.lolsearch.lolrecordsearch.domain.jpa.Participant;
import com.lolsearch.lolrecordsearch.domain.jpa.ParticipantIdentity;
import com.lolsearch.lolrecordsearch.dto.RecordDTO;
import com.lolsearch.lolrecordsearch.dto.ResultDTO;
import com.lolsearch.lolrecordsearch.repository.jpa.ChampionRepository;
import com.lolsearch.lolrecordsearch.repository.jpa.LeaguePositionRepository;
import com.lolsearch.lolrecordsearch.repository.jpa.ParticipantIdentityRepository;
import com.lolsearch.lolrecordsearch.repository.jpa.ParticipantRepository;
import com.lolsearch.lolrecordsearch.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    public List<ParticipantIdentity> getParticipantIdentitiesByGameId(Long gameId) {
        return participantIdentityRepository.findParticipantIdentitiesByGameIdOrderByParticipantId(gameId);
    }

    @Override
    public List<Participant> getParticipantsByGameId(Long gameId) {
        return participantRepository.findParticipantsByGameIdOrderByParticipantId(gameId);
    }

    @Override
    @Transactional
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
    @Transactional
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
    public Participant addParticipant(Participant participant) {
        return participantRepository.save(participant);
    }

    @Override
    @Transactional
    public ParticipantIdentity addParticipantIdentity(ParticipantIdentity participantIdentity) {
        return participantIdentityRepository.save(participantIdentity);
    }
}
