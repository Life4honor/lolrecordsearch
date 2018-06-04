package com.lolsearch.lolrecordsearch.service.impl;

import com.lolsearch.lolrecordsearch.domain.Match;
import com.lolsearch.lolrecordsearch.repository.MatchRepository;
import com.lolsearch.lolrecordsearch.service.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MatchServiceImpl implements MatchService {

    @Autowired
    MatchRepository matchRepository;

    @Override
    public Match addMatch(Match match) {
        return matchRepository.save(match);
    }
}
