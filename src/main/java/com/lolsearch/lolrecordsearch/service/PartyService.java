package com.lolsearch.lolrecordsearch.service;

import com.lolsearch.lolrecordsearch.domain.jpa.Category;
import com.lolsearch.lolrecordsearch.domain.jpa.CategoryName;
import com.lolsearch.lolrecordsearch.domain.jpa.Party;
import com.lolsearch.lolrecordsearch.domain.jpa.PartyDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface PartyService {

    Party saveParty(Party party);

    PartyDetail savePartyDetail(PartyDetail partyDetail);

    Page<Party> getPartiesByCategoryName(CategoryName categoryName, String type, LocalDateTime start, LocalDateTime end, String searchStr, String searchType, Pageable pageable);
}
