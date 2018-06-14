package com.lolsearch.lolrecordsearch.service;

import com.lolsearch.lolrecordsearch.domain.jpa.Board;
import com.lolsearch.lolrecordsearch.domain.jpa.Category;
import com.lolsearch.lolrecordsearch.domain.jpa.CategoryName;
import com.lolsearch.lolrecordsearch.domain.jpa.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardService {

    Page<Board> findBoards(Integer categoryId, String type, Pageable pageable);

    Board findBoardById(Long boardId);

    Board saveBoard(Board board, User user);

    Category findCategoryByName(CategoryName name);

    void deleteBoard(Long boardId);

    void updateContent(Long boardId, String content);

}
