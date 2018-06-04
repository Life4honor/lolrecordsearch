package com.lolsearch.lolrecordsearch.repository;

import com.lolsearch.lolrecordsearch.domain.Match;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchRepository extends JpaRepository<Match, Long> {

}
