package com.lolsearch.lolrecordsearch.service.impl;

import com.lolsearch.lolrecordsearch.domain.jpa.*;
import com.lolsearch.lolrecordsearch.repository.jpa.PartyDetailRepository;
import com.lolsearch.lolrecordsearch.repository.jpa.PartyRepository;
import com.lolsearch.lolrecordsearch.service.PartyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class PartyServiceImpl implements PartyService {

    @Autowired
    PartyRepository partyRepository;

    @Autowired
    PartyDetailRepository partyDetailRepository;

    @Override
    @Transactional
    public Party saveParty(Party party) {
        return partyRepository.save(party);
    }

    @Override
    @Transactional
    public PartyDetail savePartyDetail(PartyDetail partyDetail) {
        return partyDetailRepository.save(partyDetail);
    }

    @Override
    public Page<Party> getPartiesByCategoryName(CategoryName categoryName, String type, LocalDateTime start, LocalDateTime end, String searchStr, String searchType, Pageable pageable) {
        return partyRepository.getPartiesByCategoryName(categoryName, type, start, end, searchStr, searchType, pageable);
    }
}
