package com.lolsearch.lolrecordsearch.service;

import com.lolsearch.lolrecordsearch.domain.jpa.*;
import com.lolsearch.lolrecordsearch.dto.StartToEndTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface PartyService {

    Party saveParty(Board board, String date, String qType, String time);

    PartyDetail savePartyDetail(Party party, User user, String position);

    Page<Party> getPartiesByCategoryName(CategoryName categoryName, String type, StartToEndTime startToEndTime, String searchStr, String searchType, Pageable pageable);

    Party findPartyByBoardId(Long boardId);

    List<PartyDetail> getPartyDetailListByParty(Party party);

    PartyDetail findPartyDetailByPartyAndUser(Party party, User user);

    void deletePartyDetail(PartyDetail partyDetail);

    Party createParty(Board board, String date, String qType, String time);

    PartyDetail createPartyDetail(Party party, User user, String position);

    List<PartyPosition> getPartyPositionList(List<PartyDetail> partyDetailList);

    List<PartyPosition> getAvailablePositionList(List<PartyPosition> partyPositionList);

    boolean dupCheck(List<PartyDetail> partyDetailList, User user);

    void getInToParty(Party party, User user, String position);

    void getOutFromParty(Party party, User user);
}
