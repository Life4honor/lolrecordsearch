package com.lolsearch.lolrecordsearch.service;

import com.lolsearch.lolrecordsearch.domain.Summoner;
import com.lolsearch.lolrecordsearch.dto.SummonerDTO;

public interface SummonerService {
    public Summoner getSummonerByName(String name);

    public Summoner addSummoner(SummonerDTO summonerDTO);
}
