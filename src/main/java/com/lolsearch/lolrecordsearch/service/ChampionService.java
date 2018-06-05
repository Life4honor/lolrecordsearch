package com.lolsearch.lolrecordsearch.service;

import com.lolsearch.lolrecordsearch.domain.Champion;
import com.lolsearch.lolrecordsearch.dto.ChampionDTO;

public interface ChampionService {

    public Champion getChampionById(Long id);

    public Champion saveChampion(ChampionDTO championDTO);

}
