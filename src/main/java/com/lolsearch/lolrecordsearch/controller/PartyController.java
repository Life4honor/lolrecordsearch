package com.lolsearch.lolrecordsearch.controller;

import com.lolsearch.lolrecordsearch.domain.jpa.*;
import com.lolsearch.lolrecordsearch.dto.LoginUser;
import com.lolsearch.lolrecordsearch.dto.Pagination;
import com.lolsearch.lolrecordsearch.service.BoardService;
import com.lolsearch.lolrecordsearch.service.PartyService;
import com.lolsearch.lolrecordsearch.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.chrono.ChronoLocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/squads")
public class PartyController {

    @Autowired
    BoardService boardService;

    @Autowired
    UserService userService;

    @Autowired
    PartyService partyService;

    @GetMapping
    public String list(@RequestParam(name = "type", required = false, defaultValue = "ALL") String type,
                       @RequestParam(name = "page", required = false, defaultValue = "1") int page,
                       @RequestParam(name = "startDate", required = false, defaultValue = "") String startDate,
                       @RequestParam(name = "startTime", required = false, defaultValue = "") String startTime,
                       @RequestParam(name = "endDate", required = false, defaultValue = "") String endDate,
                       @RequestParam(name = "endTime", required = false, defaultValue = "") String endTime,
                       @RequestParam(name = "searchStr", required = false, defaultValue = "") String searchStr,
                       @RequestParam(name = "searchType", required = false, defaultValue = "") String searchType,
                       ModelMap modelMap){

        LocalDateTime startLocalDateTime = null;
        if(!"".equals(startDate)&&!"".equals(startTime)) {
            startLocalDateTime = LocalDateTime.parse(startDate + " " + startTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        }
        LocalDateTime endLocalDateTime = null;
        if(!"".equals(endDate)&&!"".equals(endTime)) {
            endLocalDateTime = LocalDateTime.parse(endDate + " " + endTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        }

        if(startLocalDateTime != null && endLocalDateTime != null) {
            if (startLocalDateTime.toEpochSecond(ZoneOffset.MAX) - endLocalDateTime.toEpochSecond(ZoneOffset.MAX) > 0) {
                LocalDateTime tempTime = startLocalDateTime;
                startLocalDateTime = endLocalDateTime;
                endLocalDateTime = tempTime;

                String tempStr = startDate;
                startDate = endDate;
                endDate = tempStr;

                tempStr = startTime;
                startTime = endTime;
                endTime = tempStr;
            }
        }
        modelMap.addAttribute("startDate", startDate);
        modelMap.addAttribute("startTime", startTime);
        modelMap.addAttribute("endDate", endDate);
        modelMap.addAttribute("endTime", endTime);
        modelMap.addAttribute("searchType", searchType);
        modelMap.addAttribute("type", type);

        Pageable pageable = PageRequest.of(page-1, 5, Sort.by(Sort.Direction.DESC, "id"));
        Page<Party> parties = partyService.getPartiesByCategoryName(CategoryName.PARTY, type, startLocalDateTime, endLocalDateTime, searchStr, searchType, pageable);
        List<Party> partyList = parties.getContent();

        Pagination pagination = new Pagination(parties.getTotalElements(), page);

        modelMap.addAttribute("pagination", pagination);
        modelMap.addAttribute("parties", partyList);

        modelMap.addAttribute("startDate", startDate);
        modelMap.addAttribute("startTime", startTime);

        modelMap.addAttribute("endDate", endDate);
        modelMap.addAttribute("endTime", endTime);
        return "partyMatch/partyMatchList";
    }

    @PostMapping
    public String createPartyAndBoard(@RequestParam(name = "content") String content,
                                      @RequestParam(name = "date") String date,
                                      @RequestParam(name = "time") String time,
                                      @RequestParam(name = "qType") String qType,
                                      @RequestParam(name = "summonerName") String summonerName,
                                      @RequestParam(name = "position") String position,
                                      @RequestParam(name = "title") String title,
                                      Authentication authentication){
        //id, type, matchDate, board, partyDetail FOR PARTY
        //id, writer, content, regDate, category, partyType, recruit FOR BOARD

        //게시판을 먼저 만들고 파티를 생성 -> 파티 디테일 매칭
        Category category = boardService.findCategoryByName(CategoryName.PARTY);

        Board board = new Board();
        board.setCategory(category);
        board.setContent(content);
        LocalDateTime regDate = LocalDateTime.now();
        board.setRegDate(regDate);
        board.setWriter(summonerName);
        board.setTitle(title);
        boardService.saveBoard(board);


        Party party = new Party();
        party.setBoard(board);
        LocalDateTime matchDate = LocalDateTime.parse(date + " " + time, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        party.setMatchDate(matchDate);
        if(qType.equals("솔로랭크")){
            party.setMaxParticipant(2);
        }else{
            party.setMaxParticipant(5);
        }
        party.setCurrentParticipant(1);
        if(qType.equals("솔로랭크")) {
            party.setType(PartyType.SOLO);
        }else{
            party.setType(PartyType.NORMAL);
        }
        partyService.saveParty(party);

        PartyDetail partyDetail = new PartyDetail();
        partyDetail.setParty(party);
        LoginUser loginUser = (LoginUser)authentication.getPrincipal();
        User user = userService.findUser(loginUser.getId());
        partyDetail.setUser(user);
        switch (position){
            case "탑":
                partyDetail.setPosition(PartyPosition.TOP);
                break;
            case "정글":
                partyDetail.setPosition(PartyPosition.JUNGLE);
                break;
            case "미드":
                partyDetail.setPosition(PartyPosition.MID);
                break;
            case "원거리 딜러":
                partyDetail.setPosition(PartyPosition.AD_CARRY);
                break;
            case "서포터":
                partyDetail.setPosition(PartyPosition.SUPPORTER);
                break;
        }
        party.addPartyDetail(partyDetail);
        partyService.savePartyDetail(partyDetail);
        return "redirect:/squads";
    }

    @GetMapping("/writeform")
    public String wrtieForm(ModelMap modelMap,
                            Authentication authentication){

        LoginUser loginUser = (LoginUser)authentication.getPrincipal();
        User user = userService.findUser(loginUser.getId());
        String summonerName = user.getSummoner();
        modelMap.addAttribute("summonerName", summonerName);

        LocalDateTime localDateTime = LocalDateTime.now();
        String today = localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String time = localDateTime.format(DateTimeFormatter.ofPattern("kk:mm"));
        modelMap.addAttribute("today", today);
        modelMap.addAttribute("time", time);

        List<PartyType> partyTypeList = PartyType.getList();
        modelMap.addAttribute("typeList", partyTypeList);
        List<PartyPosition> partyPositionList = PartyPosition.getList();
        modelMap.addAttribute("partyPositionList", partyPositionList);
        return "partyMatch/writeForm";
    }

    @GetMapping("/{boardId}")
    public String detail(@PathVariable Long boardId,
                         ModelMap modelMap){

        Board board = boardService.findBoardById(boardId);

        modelMap.addAttribute("board", board);

        return "partyMatch/partyDetail";
    }

}
