package com.lolsearch.lolrecordsearch.elasticsearch.summoner;

import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface SummonerElasticRepository extends ElasticsearchRepository<SummonerElastic, Integer> {

    @Query("{\"match\" : {\"name\" : {\"query\" : \"?0\", \"fuzziness\":\"AUTO\"}}}")
    SummonerElastic findSummonerByName(String name);

}