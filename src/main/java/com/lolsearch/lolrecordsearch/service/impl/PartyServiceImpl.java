package com.lolsearch.lolrecordsearch.service.impl;

import com.lolsearch.lolrecordsearch.domain.jpa.*;
import com.lolsearch.lolrecordsearch.dto.StartToEndTime;
import com.lolsearch.lolrecordsearch.repository.jpa.PartyDetailRepository;
import com.lolsearch.lolrecordsearch.repository.jpa.PartyRepository;
import com.lolsearch.lolrecordsearch.repository.jpa.UserRepository;
import com.lolsearch.lolrecordsearch.service.PartyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class PartyServiceImpl implements PartyService {

    @Autowired
    PartyRepository partyRepository;

    @Autowired
    PartyDetailRepository partyDetailRepository;

    @Autowired
    UserRepository userRepository;

    @Override
    @Transactional
    public Party saveParty(Board board, String date, String qType, String time) {
        Party party = createParty(board, date, qType, time);
        return partyRepository.save(party);
    }

    @Override
    @Transactional
    public PartyDetail savePartyDetail(Party party, User user, String position) {
        PartyDetail partyDetail = createPartyDetail(party, user, position);
        return partyDetailRepository.save(partyDetail);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Party> getPartiesByCategoryName(CategoryName categoryName, String type, StartToEndTime startToEndTime, String searchStr, String searchType, Pageable pageable) {
        LocalDateTime start = startToEndTime.getStart();
        LocalDateTime end = startToEndTime.getEnd();
        return partyRepository.getPartiesByCategoryName(categoryName, type, start, end, searchStr, searchType, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Party findPartyByBoardId(Long boardId) {
        return partyRepository.findPartyByBoardId(boardId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PartyDetail> getPartyDetailListByParty(Party party) {
        return partyDetailRepository.getPartyDetailListByParty(party);
    }

    @Override
    @Transactional(readOnly = true)
    public PartyDetail findPartyDetailByPartyAndUser(Party party, User user) {
        return partyDetailRepository.findPartyDetailByPartyAndUser(party, user);
    }

    @Override
    @Transactional
    public void deletePartyDetail(PartyDetail partyDetail) {
        partyDetailRepository.delete(partyDetail);
    }

    @Override
    public Party createParty(Board board, String date, String qType, String time) {
        Party party = new Party();
        party.setBoard(board);
        LocalDateTime matchDate = LocalDateTime.parse(date + " " + time, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        party.setMatchDate(matchDate);
        if(qType.equals("솔로랭크")){
            party.setMaxParticipant(2);
        }else{
            party.setMaxParticipant(5);
        }
        party.setCurrentParticipant(1);
        if(qType.equals("솔로랭크")) {
            party.setType(PartyType.SOLO);
        }else{
            party.setType(PartyType.NORMAL);
        }
        return party;
    }

    @Override
    public PartyDetail createPartyDetail(Party party, User user, String position) {
        PartyDetail partyDetail = new PartyDetail();
        partyDetail.setParty(party);
        partyDetail.setUser(user);
        partyDetail.setPosition(position);
        return partyDetail;
    }

    @Override
    public List<PartyPosition> getPartyPositionList(List<PartyDetail> partyDetailList) {
        List<PartyPosition> partyPositionList = new ArrayList<>();
        for (PartyDetail partyDetail : partyDetailList) {
            partyPositionList.add(partyDetail.getPosition());
        }
        return partyPositionList;
    }

    @Override
    public List<PartyPosition> getAvailablePositionList(List<PartyPosition> partyPositionList) {
        List<PartyPosition> availablePositionList = new ArrayList<>();
        for (PartyPosition partyPosition : PartyPosition.getPartyPositionList()) {
            if(!partyPositionList.contains(partyPosition)) {
                availablePositionList.add(partyPosition);
            }
        }
        return availablePositionList;
    }

    @Override
    public boolean dupCheck(List<PartyDetail> partyDetailList, User user) {
        for (PartyDetail partyDetail : partyDetailList) {
            if(partyDetail.getUser().equals(user)){
                return true;
            }
        }
        return false;
    }

    @Override
    @Transactional
    public void getInToParty(Party party, User user, String position) {

        party.setCurrentParticipant(party.getCurrentParticipant()+1);
        List<PartyDetail> partyDetailList = party.getPartyDetails();

        PartyDetail partyDetail = new PartyDetail();
        partyDetail.setPosition(position);

        List<PartyPosition> positionList = new ArrayList<>();
        for (PartyDetail detail : partyDetailList) {
            positionList.add(detail.getPosition());
        }
        if(!positionList.contains(partyDetail.getPosition())) {
            partyDetail.setUser(user);
            partyDetail.setParty(party);
            party.addPartyDetail(partyDetail);
            partyDetailRepository.save(partyDetail);
        }
    }

    @Override
    @Transactional
    public void getOutFromParty(Party party, User user) {

        party.setCurrentParticipant(party.getCurrentParticipant()-1);
        PartyDetail partyDetail = partyDetailRepository.findPartyDetailByPartyAndUser(party, user);

        List<PartyDetail> userPartyDetailList = user.getPartyDetails();
        if(userPartyDetailList.contains(partyDetail))
            userPartyDetailList.remove(partyDetail);

        List<PartyDetail> partyPartyDetailList = party.getPartyDetails();
        if(partyPartyDetailList.contains(partyDetail))
            partyPartyDetailList.remove(partyDetail);

        partyDetailRepository.delete(partyDetail);

    }
}
