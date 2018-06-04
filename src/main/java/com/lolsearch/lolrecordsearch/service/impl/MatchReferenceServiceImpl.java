package com.lolsearch.lolrecordsearch.service.impl;

import com.lolsearch.lolrecordsearch.domain.Match;
import com.lolsearch.lolrecordsearch.domain.MatchReference;
import com.lolsearch.lolrecordsearch.dto.MatchReferenceDTO;
import com.lolsearch.lolrecordsearch.repository.MatchReferenceRepository;
import com.lolsearch.lolrecordsearch.service.MatchReferenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MatchReferenceServiceImpl implements MatchReferenceService {

    @Autowired
    MatchReferenceRepository matchReferenceRepository;

    @Override
    @Transactional(readOnly = true)
    public MatchReference getMatchReferencByGameId(Long gameId) {
        return matchReferenceRepository.findMatchReferenceByGameId(gameId);
    }

    @Override
    @Transactional
    public MatchReference addMatchReference(MatchReferenceDTO matchReferenceDTO) {
        MatchReference matchReference = new MatchReference();
        matchReference.setChampionId(matchReferenceDTO.getChampion());
        matchReference.setGameId(matchReferenceDTO.getGameId());
        matchReference.setLane(matchReferenceDTO.getLane());
        matchReference.setPlatformId(matchReferenceDTO.getPlatformId());
        matchReference.setQueue(matchReferenceDTO.getQueue());
        matchReference.setRole(matchReferenceDTO.getRole());
        matchReference.setSeason(matchReferenceDTO.getSeason());
        matchReference.setTimestamp(matchReferenceDTO.getTimestamp());
        return matchReferenceRepository.save(matchReference);
    }
}
