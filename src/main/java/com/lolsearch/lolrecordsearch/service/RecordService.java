package com.lolsearch.lolrecordsearch.service;

import com.lolsearch.lolrecordsearch.domain.Match;
import com.lolsearch.lolrecordsearch.domain.Participant;
import com.lolsearch.lolrecordsearch.domain.ParticipantIdentity;
import com.lolsearch.lolrecordsearch.dto.RecordDTO;
import com.lolsearch.lolrecordsearch.dto.ResultDTO;

import java.util.List;

public interface RecordService {

    public List<ParticipantIdentity> getParticipantIdentitiesByGameId(Long gameId);

    public List<Participant> getParticipantsByGameId(Long gameId);

    public void setResultDTO(Match match, ResultDTO resultDTO);

    public void setRecordDTO(RecordDTO recordDTO, ParticipantIdentity participantIdentity, Participant participant);
}
