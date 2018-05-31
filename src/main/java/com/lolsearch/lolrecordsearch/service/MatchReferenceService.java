package com.lolsearch.lolrecordsearch.service;

import com.lolsearch.lolrecordsearch.domain.MatchReference;

public interface MatchReferenceService {

    public MatchReference getMatchReferencByGameId(Long gameId);

}
