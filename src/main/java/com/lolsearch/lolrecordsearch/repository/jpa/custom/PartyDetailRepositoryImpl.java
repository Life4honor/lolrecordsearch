package com.lolsearch.lolrecordsearch.repository.jpa.custom;

import com.lolsearch.lolrecordsearch.domain.jpa.Party;
import com.lolsearch.lolrecordsearch.domain.jpa.PartyDetail;
import com.lolsearch.lolrecordsearch.domain.jpa.QPartyDetail;
import com.lolsearch.lolrecordsearch.domain.jpa.User;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class PartyDetailRepositoryImpl extends QuerydslRepositorySupport implements PartyDetailRepositoryCustom {

    public PartyDetailRepositoryImpl(){
        super(PartyDetail.class);
    }

    QPartyDetail qPartyDetail = QPartyDetail.partyDetail;

    @Override
    public List<PartyDetail> getPartyDetailListByParty(Party party) {

        JPQLQuery<PartyDetail> jpqlQuery = from(qPartyDetail).where(qPartyDetail.party.eq(party));
        List<PartyDetail> partyDetailList = jpqlQuery.fetch();
        return partyDetailList;

    }

    @Override
    public PartyDetail findPartyDetailByPartyAndUser(Party party, User user) {

        JPQLQuery<PartyDetail> jpqlQuery = from(qPartyDetail).where(qPartyDetail.party.eq(party)).where(qPartyDetail.user.eq(user));
        return jpqlQuery.fetchOne();

    }
}
