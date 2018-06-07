package com.lolsearch.lolrecordsearch.service.impl;

import com.lolsearch.lolrecordsearch.domain.Summoner;
import com.lolsearch.lolrecordsearch.dto.SummonerDTO;
import com.lolsearch.lolrecordsearch.repository.SummonerRepository;
import com.lolsearch.lolrecordsearch.service.SummonerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SummonerServiceImpl implements SummonerService {

    @Autowired
    SummonerRepository summonerRepository;

    @Override
    @Transactional(readOnly = true)
    public Summoner getSummonerByName(String name) {
        return summonerRepository.findSummonerByNameIgnoreCase(name);
    }

    @Override
    @Transactional
    public Summoner saveSummoner(SummonerDTO summonerDTO) {
        Summoner summoner = new Summoner();
        summoner.setAccountId(summonerDTO.getAccountId());
        summoner.setId(summonerDTO.getId());
        summoner.setSummonerLevel(summonerDTO.getSummonerLevel());
        summoner.setRevisionDate(summonerDTO.getRevisionDate());
        summoner.setProfileIconId(summonerDTO.getProfileIconId());
        summoner.setName(summonerDTO.getName());
        return summonerRepository.save(summoner);
    }
}
