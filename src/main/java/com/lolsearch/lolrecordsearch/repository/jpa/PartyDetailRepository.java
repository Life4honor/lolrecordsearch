package com.lolsearch.lolrecordsearch.repository.jpa;

import com.lolsearch.lolrecordsearch.domain.jpa.PartyDetail;
import com.lolsearch.lolrecordsearch.repository.jpa.custom.PartyDetailRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartyDetailRepository extends JpaRepository<PartyDetail, Long>, PartyDetailRepositoryCustom {

}
