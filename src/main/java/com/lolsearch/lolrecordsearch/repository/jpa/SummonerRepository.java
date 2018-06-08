package com.lolsearch.lolrecordsearch.repository.jpa;

import com.lolsearch.lolrecordsearch.domain.jpa.Summoner;
import com.lolsearch.lolrecordsearch.repository.jpa.custom.SummonerRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SummonerRepository extends JpaRepository<Summoner, Long>, SummonerRepositoryCustom {

//    Summoner findSummonerByNameIgnoreCase(String name);

}
