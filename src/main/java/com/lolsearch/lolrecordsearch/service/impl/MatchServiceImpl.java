package com.lolsearch.lolrecordsearch.service.impl;

import com.lolsearch.lolrecordsearch.domain.Match;
import com.lolsearch.lolrecordsearch.repository.MatchRepository;
import com.lolsearch.lolrecordsearch.repository.ParticipantRepository;
import com.lolsearch.lolrecordsearch.service.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MatchServiceImpl implements MatchService {

    @Autowired
    MatchRepository matchRepository;

    @Autowired
    ParticipantRepository participantRepository;

    @Override
    public Match saveMatch(Match match) {
        return matchRepository.save(match);
    }

    @Override
    public Match getMatch(Long gameId) {
        return matchRepository.findMatchByMatchReference_GameId(gameId);
    }
}
