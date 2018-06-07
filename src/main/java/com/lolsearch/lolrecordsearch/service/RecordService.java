package com.lolsearch.lolrecordsearch.service;

import com.lolsearch.lolrecordsearch.domain.jpa.*;
import com.lolsearch.lolrecordsearch.dto.LeaguePositionDTO;
import com.lolsearch.lolrecordsearch.dto.PlayerDTO;
import com.lolsearch.lolrecordsearch.dto.RecordDTO;
import com.lolsearch.lolrecordsearch.dto.ResultDTO;
import com.lolsearch.lolrecordsearch.dto.*;

import java.util.List;

public interface RecordService {

    public List<ParticipantIdentity> getParticipantIdentitiesByGameId(Long gameId);

    public List<Participant> getParticipantsByGameId(Long gameId);

    public void setRecordDTO(RecordDTO recordDTO, ParticipantIdentity participantIdentity, Participant participant);

    public List<LeaguePosition> getLeaguePositionsByName(String name);

    public Participant saveParticipant(Participant participant);

    public ParticipantIdentity saveParticipantIdentity(ParticipantIdentity participantIdentity);

    public LeaguePosition saveLeaguePosition(LeaguePositionDTO leaguePositionDTO);

    public List<List<LeaguePosition>> getLeaguePositionListResult(List<String> summoners);

    public List<ResultDTO> getResultDTOList(Summoner summoner);

    public List<List<PlayerDTO>> getPlayerDTOListResult(List<ResultDTO> resultDTOList);

    public List<String> saveRecords(String type, List<String> summoners);

    public Match saveMatch(Match match);

    public Match getMatch(Long gameId, Long summonerId);

    public Champion getChampionById(Long id);

    public Champion saveChampion(ChampionDTO championDTO);

    public MatchReference getMatchReferencByGameId(Long gameId);

    public MatchReference saveMatchReference(MatchReferenceDTO matchReferenceDTO);

    public Summoner getSummonerByName(String name);

    public Summoner saveSummoner(SummonerDTO summonerDTO);
}
