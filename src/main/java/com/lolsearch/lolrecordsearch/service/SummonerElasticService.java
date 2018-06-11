package com.lolsearch.lolrecordsearch.service;

import com.lolsearch.lolrecordsearch.domain.elasticsearch.SummonerElastic;

public interface SummonerElasticService {

    SummonerElastic save(SummonerElastic summonerElastic);

    void delete(SummonerElastic summonerElastic);

    SummonerElastic findByName(String name);

}
