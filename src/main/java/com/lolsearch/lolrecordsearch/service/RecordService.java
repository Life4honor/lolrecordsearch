package com.lolsearch.lolrecordsearch.service;

import com.lolsearch.lolrecordsearch.domain.*;
import com.lolsearch.lolrecordsearch.dto.LeaguePositionDTO;
import com.lolsearch.lolrecordsearch.dto.PlayerDTO;
import com.lolsearch.lolrecordsearch.dto.RecordDTO;
import com.lolsearch.lolrecordsearch.dto.ResultDTO;

import java.util.List;

public interface RecordService {

    public List<ParticipantIdentity> getParticipantIdentitiesByGameId(Long gameId);

    public List<Participant> getParticipantsByGameId(Long gameId);

    public void setRecordDTO(RecordDTO recordDTO, ParticipantIdentity participantIdentity, Participant participant);

    public LeaguePosition getLeaguePositionByNameAndQueueType(String name, String queueType);

    public Participant saveParticipant(Participant participant);

    public ParticipantIdentity saveParticipantIdentity(ParticipantIdentity participantIdentity);

    public LeaguePosition saveLeaguePosition(LeaguePositionDTO leaguePositionDTO);

    public List<List<LeaguePosition>> getLeaguePositionListResult(List<String> summoners);

    public List<ResultDTO> getResultDTOList(Summoner summoner);

    public List<List<PlayerDTO>> getPlayerDTOListResult(List<ResultDTO> resultDTOList);
}
