package com.lolsearch.lolrecordsearch.service.impl;

import com.lolsearch.lolrecordsearch.domain.jpa.Champion;
import com.lolsearch.lolrecordsearch.repository.jpa.ChampionRepository;
import com.lolsearch.lolrecordsearch.service.ChampionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChampionServiceImpl implements ChampionService {

    @Autowired
    ChampionRepository championRepository;

    @Override
    public Champion getChampionById(Long id) {
        return championRepository.findChampionById(id);
    }
}
