package com.lolsearch.lolrecordsearch.service.impl;

import com.lolsearch.lolrecordsearch.domain.jpa.Board;
import com.lolsearch.lolrecordsearch.domain.jpa.Category;
import com.lolsearch.lolrecordsearch.domain.jpa.CategoryName;
import com.lolsearch.lolrecordsearch.repository.jpa.BoardRepository;
import com.lolsearch.lolrecordsearch.repository.jpa.CategoryRepository;
import com.lolsearch.lolrecordsearch.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BoardServiceImpl implements BoardService {

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Override
    @Transactional(readOnly = true)
    public Page<Board> findBoards(Integer categoryId, String type, Pageable pageable) {
        return boardRepository.findBoards(categoryId, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Board findBoardById(Long boardId) {
        return boardRepository.findBoardById(boardId);
    }

    @Override
    @Transactional
    public Board saveBoard(Board board) {
        return boardRepository.save(board);
    }

    @Override
    @Transactional(readOnly = true)
    public Category findCategoryByName(CategoryName name) {
        return categoryRepository.findCategoryByName(name);
    }
}
