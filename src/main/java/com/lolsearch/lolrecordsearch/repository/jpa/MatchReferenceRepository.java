package com.lolsearch.lolrecordsearch.repository.jpa;

import com.lolsearch.lolrecordsearch.domain.jpa.MatchReference;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MatchReferenceRepository extends JpaRepository<MatchReference,Long> {

    MatchReference findMatchReferenceByGameIdAndChampionId(Long gameId, Long championId);
}
