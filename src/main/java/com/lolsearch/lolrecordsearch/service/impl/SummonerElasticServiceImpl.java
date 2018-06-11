package com.lolsearch.lolrecordsearch.service.impl;

import com.lolsearch.lolrecordsearch.domain.elasticsearch.SummonerElastic;
import com.lolsearch.lolrecordsearch.service.SummonerElasticService;
import com.lolsearch.lolrecordsearch.repository.elasticsearch.SummonerElasticRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SummonerElasticServiceImpl implements SummonerElasticService {

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
