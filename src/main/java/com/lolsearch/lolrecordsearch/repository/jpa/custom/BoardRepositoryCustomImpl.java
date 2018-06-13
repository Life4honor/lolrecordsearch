package com.lolsearch.lolrecordsearch.repository.jpa.custom;

import com.lolsearch.lolrecordsearch.domain.jpa.Board;
import com.lolsearch.lolrecordsearch.domain.jpa.Category;
import com.lolsearch.lolrecordsearch.domain.jpa.CategoryName;
import com.lolsearch.lolrecordsearch.domain.jpa.QBoard;
import com.querydsl.jpa.JPQLQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

import java.util.List;

public class BoardRepositoryCustomImpl extends QuerydslRepositorySupport implements BoardRepositoryCustom {

    QBoard qBoard = QBoard.board;

    public BoardRepositoryCustomImpl(){
        super(Board.class);
    }

    @Override
    public Page<Board> findBoards(Integer categoryId, Pageable pageable) {
        JPQLQuery<Board> jpqlQuery = from(qBoard).where(qBoard.category.id.eq(categoryId));
        List<Board> boardList = getQuerydsl().applyPagination(pageable, jpqlQuery).fetch();

        long count = jpqlQuery.fetchCount();

        return new PageImpl<>(boardList, pageable, count);
    }

    @Override
    public Board findBoardById(Long boardId) {
        JPQLQuery<Board> jpqlQuery = from(qBoard).where(qBoard.id.eq(boardId));
        return jpqlQuery.fetchOne();
    }
}
