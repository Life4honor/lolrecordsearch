package com.lolsearch.lolrecordsearch.repository.jpa.custom;

import com.lolsearch.lolrecordsearch.domain.jpa.*;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.time.LocalDateTime;
import java.util.List;

public class PartyRepositoryImpl extends QuerydslRepositorySupport implements PartyRepositoryCustom{

    QParty qParty = QParty.party;

    public PartyRepositoryImpl(){
        super(Party.class);
    }

    @Override
    public Page<Party> getPartiesByCategoryName(CategoryName categoryName, String type, LocalDateTime start, LocalDateTime end, String searchStr, String searchType, Pageable pageable) {
        JPQLQuery<Party> jpqlQuery = from(qParty).where(qParty.board.category.name.eq(categoryName));

        if (type.equals("single")){
            PartyType partyType = PartyType.SOLO;
            jpqlQuery.where(qParty.type.eq(partyType));
        }else if(type.equals("multi")){
            PartyType partyType = PartyType.NORMAL;
            jpqlQuery.where(qParty.type.eq(partyType));
        }

        if(start != null) {
            jpqlQuery.where(qParty.matchDate.after(start));
        }
        if(end != null) {
            jpqlQuery.where(qParty.matchDate.before(end));
        }

        if("제목".equals(searchType)){
            jpqlQuery.where(qParty.board.title.likeIgnoreCase(Expressions.asString("%").concat(searchStr).concat("%")));
        }else if("작성자".equals(searchType)){
            jpqlQuery.where(qParty.board.writer.likeIgnoreCase(Expressions.asString("%").concat(searchStr).concat("%")));

        }
        List<Party> partyList = getQuerydsl().applyPagination(pageable, jpqlQuery).fetch();
        long count = jpqlQuery.fetchCount();

        return new PageImpl<>(partyList, pageable, count);
    }
}


