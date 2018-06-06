package com.lolsearch.lolrecordsearch.service;

import com.lolsearch.lolrecordsearch.domain.jpa.Champion;

public interface ChampionService {

    public Champion getChampionById(Long id);

}
