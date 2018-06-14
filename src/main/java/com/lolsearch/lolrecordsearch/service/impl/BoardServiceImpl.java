package com.lolsearch.lolrecordsearch.service.impl;

import com.lolsearch.lolrecordsearch.domain.jpa.*;
import com.lolsearch.lolrecordsearch.repository.jpa.BoardRepository;
import com.lolsearch.lolrecordsearch.repository.jpa.CategoryRepository;
import com.lolsearch.lolrecordsearch.repository.jpa.PartyRepository;
import com.lolsearch.lolrecordsearch.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class BoardServiceImpl implements BoardService {

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    PartyRepository partyRepository;

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
    public Board saveBoard(Board board, User user) {
        String writer = user.getSummoner();
        board.setWriter(writer);
        Category category = categoryRepository.findCategoryByName(CategoryName.PARTY);
        board.setCategory(category);
        LocalDateTime regDate = LocalDateTime.now();
        board.setRegDate(regDate);
        return boardRepository.save(board);
    }

    @Override
    @Transactional(readOnly = true)
    public Category findCategoryByName(CategoryName name) {
        return categoryRepository.findCategoryByName(name);
    }

    @Override
    @Transactional
    public void deleteBoard(Long boardId) {
        Party party = partyRepository.findPartyByBoardId(boardId);
        partyRepository.delete(party);
        Board board = findBoardById(boardId);
        boardRepository.delete(board);
    }

    @Override
    @Transactional
    public void updateContent(Long boardId, String content) {
        Board board = boardRepository.findBoardById(boardId);
        board.setContent(content);
    }

}
