package com.lolsearch.lolrecordsearch.service.impl;

import com.lolsearch.lolrecordsearch.domain.ParticipantIdentity;
import com.lolsearch.lolrecordsearch.repository.ParticipantIdentityRepository;
import com.lolsearch.lolrecordsearch.service.ParticipantIdentityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ParticipantIdentityServiceImpl implements ParticipantIdentityService {

    @Autowired
    ParticipantIdentityRepository participantIdentityRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ParticipantIdentity> getParticipantIdentitiesByGameId(Long gameId) {
        return participantIdentityRepository.findParticipantIdentitiesByGameIdOrderByParticipantId(gameId);
    }
}
