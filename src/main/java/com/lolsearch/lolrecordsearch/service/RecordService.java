package com.lolsearch.lolrecordsearch.service;

import com.lolsearch.lolrecordsearch.domain.jpa.LeaguePosition;
import com.lolsearch.lolrecordsearch.domain.jpa.Match;
import com.lolsearch.lolrecordsearch.domain.jpa.Participant;
import com.lolsearch.lolrecordsearch.domain.jpa.ParticipantIdentity;
import com.lolsearch.lolrecordsearch.dto.RecordDTO;
import com.lolsearch.lolrecordsearch.dto.ResultDTO;

import java.util.List;

public interface RecordService {

    public List<ParticipantIdentity> getParticipantIdentitiesByGameId(Long gameId);

    public List<Participant> getParticipantsByGameId(Long gameId);

    public void setResultDTO(Match match, ResultDTO resultDTO);

    public void setRecordDTO(RecordDTO recordDTO, ParticipantIdentity participantIdentity, Participant participant);

    public LeaguePosition getLeaguePositionByNameAndQueueType(String name, String queueType);

    public Participant addParticipant(Participant participant);

    public ParticipantIdentity addParticipantIdentity(ParticipantIdentity participantIdentity);
}
