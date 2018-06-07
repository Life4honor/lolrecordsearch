package com.lolsearch.lolrecordsearch.repository.jpa;

import com.lolsearch.lolrecordsearch.domain.jpa.LeaguePosition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LeaguePositionRepository extends JpaRepository<LeaguePosition, Long> {

    @Query("SELECT lp from LeaguePosition lp WHERE lower(lp.playerOrTeamName) = lower(:name)")
    public List<LeaguePosition> findLeaguePositionsByPlayerOrTeamName(@Param("name") String name);

}
