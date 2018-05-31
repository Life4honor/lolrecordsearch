package com.lolsearch.lolrecordsearch.service;

import com.lolsearch.lolrecordsearch.domain.ParticipantIdentity;

import java.util.List;

public interface ParticipantIdentityService {

    public List<ParticipantIdentity> getParticipantIdentitiesByGameId(Long gameId);

}
