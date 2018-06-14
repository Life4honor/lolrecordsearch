package com.lolsearch.lolrecordsearch.repository.jpa.custom;

import com.lolsearch.lolrecordsearch.domain.jpa.Party;
import com.lolsearch.lolrecordsearch.domain.jpa.PartyDetail;
import com.lolsearch.lolrecordsearch.domain.jpa.User;

import java.util.List;

public interface PartyDetailRepositoryCustom {

    List<PartyDetail> getPartyDetailListByParty(Party party);

    PartyDetail findPartyDetailByPartyAndUser(Party party, User user);

}
