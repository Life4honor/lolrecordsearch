package com.lolsearch.lolrecordsearch.repository.jpa.custom;

import com.lolsearch.lolrecordsearch.domain.jpa.Summoner;

public interface SummonerRepositoryCustom{

    Summoner findSummonerByNameIgnoreCase(String name);

}
