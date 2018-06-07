package com.lolsearch.lolrecordsearch.service;

import com.lolsearch.lolrecordsearch.domain.jpa.MatchReference;
import com.lolsearch.lolrecordsearch.dto.MatchReferenceDTO;

public interface MatchReferenceService {

    public MatchReference getMatchReferencByGameId(Long gameId);

    public MatchReference saveMatchReference(MatchReferenceDTO matchReferenceDTO);
}
