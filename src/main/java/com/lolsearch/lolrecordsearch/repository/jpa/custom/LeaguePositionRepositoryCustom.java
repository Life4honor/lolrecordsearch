package com.lolsearch.lolrecordsearch.repository.jpa.custom;

import com.lolsearch.lolrecordsearch.domain.jpa.LeaguePosition;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LeaguePositionRepositoryCustom {

    List<LeaguePosition> findLeaguePositionsByPlayerOrTeamName(@Param("name") String name);

}
