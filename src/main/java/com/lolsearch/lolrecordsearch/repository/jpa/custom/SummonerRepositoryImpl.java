package com.lolsearch.lolrecordsearch.repository.jpa.custom;

import com.lolsearch.lolrecordsearch.domain.jpa.QSummoner;
import com.lolsearch.lolrecordsearch.domain.jpa.Summoner;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

public class SummonerRepositoryImpl extends QuerydslRepositorySupport implements SummonerRepositoryCustom {

    public SummonerRepositoryImpl(){
        super(Summoner.class);
    }

    private QSummoner qSummoner = QSummoner.summoner;

    @Override
    public Summoner findSummonerByNameIgnoreCase(String name) {

        JPQLQuery<Summoner> jpqlQuery = from(qSummoner).where(qSummoner.name.equalsIgnoreCase(name));
        return jpqlQuery.fetchOne();
    }
}
