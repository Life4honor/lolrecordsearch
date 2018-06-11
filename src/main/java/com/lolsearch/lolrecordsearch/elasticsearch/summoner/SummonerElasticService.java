package com.lolsearch.lolrecordsearch.elasticsearch.summoner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SummonerElasticService {

    @Autowired
    SummonerElasticRepository summonerElasticRepository;

    public SummonerElastic save(SummonerElastic summonerElastic){
        return summonerElasticRepository.save(summonerElastic);
    }

    public void delete(SummonerElastic summonerElastic) {
        summonerElasticRepository.delete(summonerElastic);
    }

    public SummonerElastic findByName(String name){
        return summonerElasticRepository.findSummonerByName(name);
    }

}
