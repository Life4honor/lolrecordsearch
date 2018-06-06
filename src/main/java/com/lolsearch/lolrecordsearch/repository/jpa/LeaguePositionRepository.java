package com.lolsearch.lolrecordsearch.repository.jpa;

import com.lolsearch.lolrecordsearch.domain.jpa.LeaguePosition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LeaguePositionRepository extends JpaRepository<LeaguePosition, Long> {

    @Query("SELECT lp from LeaguePosition lp WHERE lp.playerOrTeamName = :name AND lp.queueType = :queueType")
    public LeaguePosition findLeaguePositionByPlayerOrTeamNameAndQueueType(@Param("name") String name, @Param("queueType") String type);

}
