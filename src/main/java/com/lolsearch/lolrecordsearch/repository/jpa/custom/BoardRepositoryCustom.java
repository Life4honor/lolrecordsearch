package com.lolsearch.lolrecordsearch.repository.jpa.custom;

import com.lolsearch.lolrecordsearch.domain.jpa.Board;
import com.lolsearch.lolrecordsearch.domain.jpa.Category;
import com.lolsearch.lolrecordsearch.domain.jpa.CategoryName;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

public interface BoardRepositoryCustom{

    Page<Board> findBoards(Integer categoryId, Pageable pageable);

    Board findBoardById(Long boardId);
}
