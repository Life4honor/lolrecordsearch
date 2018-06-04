package com.lolsearch.lolrecordsearch.repository;

import com.lolsearch.lolrecordsearch.domain.MatchReference;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchReferenceRepository extends JpaRepository<MatchReference,Long> {

    public MatchReference findMatchReferenceByGameId(Long gameId);
}
