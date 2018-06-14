package com.lolsearch.lolrecordsearch.repository.jpa.custom;

import com.lolsearch.lolrecordsearch.domain.jpa.CategoryName;
import com.lolsearch.lolrecordsearch.domain.jpa.Party;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface PartyRepositoryCustom{

    Page<Party> getPartiesByCategoryName(CategoryName categoryName, String type, LocalDateTime start, LocalDateTime end, String searchStr, String searchType, Pageable pageable);

    Party findPartyByBoardId(Long boardId);

}
