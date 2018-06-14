package com.lolsearch.lolrecordsearch.controller.api;

import com.lolsearch.lolrecordsearch.domain.jpa.Party;
import com.lolsearch.lolrecordsearch.domain.jpa.PartyDetail;
import com.lolsearch.lolrecordsearch.domain.jpa.PartyPosition;
import com.lolsearch.lolrecordsearch.domain.jpa.User;
import com.lolsearch.lolrecordsearch.dto.LoginUser;
import com.lolsearch.lolrecordsearch.service.PartyService;
import com.lolsearch.lolrecordsearch.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/api/squads")
public class PartyApiController {

    @Autowired
    PartyService partyService;

    @Autowired
    UserService userService;

    @PostMapping("/{boardId}")
    public String getInToParty(@PathVariable Long boardId,
                               @RequestParam String position,
                               Authentication authentication){

        LoginUser userinfo = (LoginUser)authentication.getPrincipal();
        User user = userService.findUser(userinfo.getId());

        Party party = partyService.findPartyByBoardId(boardId);
        partyService.getInToParty(party, user, position);

        return "redirect:/squads/"+boardId.toString();

    }

    @DeleteMapping("/{boardId}")
    public String getOutFromParty(@PathVariable Long boardId,
                                  Authentication authentication){

        LoginUser userinfo = (LoginUser)authentication.getPrincipal();
        User user = userService.findUser(userinfo.getId());

        Party party = partyService.findPartyByBoardId(boardId);
        partyService.getOutFromParty(party, user);

        return "redirect:/squads/"+boardId.toString();
    }
}
