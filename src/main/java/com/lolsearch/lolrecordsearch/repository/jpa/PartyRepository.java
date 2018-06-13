package com.lolsearch.lolrecordsearch.repository.jpa;

import com.lolsearch.lolrecordsearch.domain.jpa.Party;
import com.lolsearch.lolrecordsearch.repository.jpa.custom.PartyRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartyRepository extends JpaRepository<Party, Long>, PartyRepositoryCustom {

}
