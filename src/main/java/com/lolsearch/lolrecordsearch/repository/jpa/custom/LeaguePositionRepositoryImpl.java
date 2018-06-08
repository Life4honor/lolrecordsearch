package com.lolsearch.lolrecordsearch.repository.jpa.custom;

import com.lolsearch.lolrecordsearch.domain.jpa.LeaguePosition;
import com.lolsearch.lolrecordsearch.domain.jpa.QLeaguePosition;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class LeaguePositionRepositoryImpl extends QuerydslRepositorySupport implements LeaguePositionRepositoryCustom {

    public LeaguePositionRepositoryImpl() {
        super(LeaguePosition.class);
    }

    private QLeaguePosition qLeaguePosition = QLeaguePosition.leaguePosition;

    @Override
    public List<LeaguePosition> findLeaguePositionsByPlayerOrTeamName(String name) {
        JPQLQuery<LeaguePosition> jpqlQuery = from(qLeaguePosition).where(qLeaguePosition.playerOrTeamName.equalsIgnoreCase(name));
        return jpqlQuery.fetch();
    }
}
