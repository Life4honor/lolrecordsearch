package com.lolsearch.lolrecordsearch.service.impl;

import com.lolsearch.lolrecordsearch.domain.Champion;
import com.lolsearch.lolrecordsearch.dto.ChampionDTO;
import com.lolsearch.lolrecordsearch.repository.ChampionRepository;
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

    @Override
    public Champion saveChampion(ChampionDTO championDTO) {
        Champion champion = new Champion();
        champion.setId(Long.valueOf(championDTO.getId()));
        champion.setName(championDTO.getName());
        return championRepository.save(champion);
    }
}
