package com.lolsearch.lolrecordsearch.repository.jpa;

import com.lolsearch.lolrecordsearch.domain.jpa.MatchReference;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchReferenceRepository extends JpaRepository<MatchReference,Long> {

    MatchReference findMatchReferenceByGameId(Long gameId);
}
