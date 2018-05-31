package com.lolsearch.lolrecordsearch.service;

import com.lolsearch.lolrecordsearch.domain.Participant;

import java.util.List;

public interface ParticipantService {

    public List<Participant> getParticipantsByGameId(Long gameId);

}
