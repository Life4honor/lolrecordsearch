package com.lolsearch.lolrecordsearch.controller;

import com.lolsearch.lolrecordsearch.domain.jpa.*;
import com.lolsearch.lolrecordsearch.dto.LoginUser;
import com.lolsearch.lolrecordsearch.dto.Pagination;
import com.lolsearch.lolrecordsearch.dto.StartToEndTime;
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
    public String list(@RequestParam(name = "type", defaultValue = "ALL") String type,
                       @RequestParam(name = "page", defaultValue = "1") int page,
                       @RequestParam(name = "searchStr", defaultValue = "") String searchStr,
                       @RequestParam(name = "searchType", defaultValue = "") String searchType,
                       StartToEndTime startToEndTime,
                       ModelMap modelMap){

        startToEndTime.timeCheckAndSwitch();

        modelMap.addAttribute("dateTime", startToEndTime);
        modelMap.addAttribute("searchType", searchType);
        modelMap.addAttribute("type", type);

        Pageable pageable = PageRequest.of(page-1, 5, Sort.by(Sort.Direction.DESC, "id"));
        Page<Party> parties = partyService.getPartiesByCategoryName(CategoryName.PARTY, type, startToEndTime, searchStr, searchType, pageable);
        List<Party> partyList = parties.getContent();

        Pagination pagination = new Pagination(parties.getTotalElements(), page);

        modelMap.addAttribute("pagination", pagination);
        modelMap.addAttribute("parties", partyList);

        return "partyMatch/partyMatchList";
    }

    @PostMapping
    public String createPartyAndBoard(Board board,
                                      @RequestParam(name = "date") String date,
                                      @RequestParam(name = "time") String time,
                                      @RequestParam(name = "qType") String qType,
                                      @RequestParam(name = "position") String position,
                                      Authentication authentication){

        LoginUser loginUser = (LoginUser)authentication.getPrincipal();
        User user = userService.findUser(loginUser.getId());

        boardService.saveBoard(board, user);

        Party party = partyService.createParty(board, date, qType, time);

        partyService.savePartyDetail(party, user, position);

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

        List<PartyType> partyTypeList = PartyType.getPartyTypeList();
        modelMap.addAttribute("typeList", partyTypeList);
        List<PartyPosition> partyPositionList = PartyPosition.getPartyPositionList();
        modelMap.addAttribute("partyPositionList", partyPositionList);
        return "partyMatch/writeForm";
    }

    @GetMapping("/{boardId}")
    public String detail(@PathVariable Long boardId,
                         Authentication authentication,
                         ModelMap modelMap){

        LoginUser loginUser = (LoginUser)authentication.getPrincipal();
        User user = userService.findUser(loginUser.getId());
        modelMap.addAttribute("user", user);

        Party party = partyService.findPartyByBoardId(boardId);
        modelMap.addAttribute("party", party);

        List<PartyDetail> partyDetailList = partyService.getPartyDetailListByParty(party);
        modelMap.addAttribute("partyDetailList", partyDetailList);

        List<PartyPosition> partyPositionList = PartyPosition.getPartyPositionList();
        List<PartyPosition> availablePositionList = partyService.getAvailablePositionList(partyPositionList);
        modelMap.addAttribute("availablePositionList", availablePositionList);

        boolean dupCheck = partyService.dupCheck(partyDetailList, user);
        modelMap.addAttribute("dupCheck", dupCheck);

        return "partyMatch/partyDetail";
    }

    @PutMapping("/{boardId}")
    public String updateContent(@PathVariable Long boardId,
                                @RequestParam String content){
        boardService.updateContent(boardId, content);
        return "redirect:/squads/"+boardId;
    }


    @DeleteMapping("/{boardId}")
    public String deleteBoard(@PathVariable Long boardId){
        boardService.deleteBoard(boardId);
        return "redirect:/squads";
    }
}
