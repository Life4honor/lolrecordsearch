package com.lolsearch.lolrecordsearch.service;

import com.lolsearch.lolrecordsearch.domain.jpa.Match;

public interface MatchService {

    public Match saveMatch(Match match);

    public Match getMatch(Long gameId, Long summonerId);

}
