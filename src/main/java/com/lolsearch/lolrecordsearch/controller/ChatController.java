package com.lolsearch.lolrecordsearch.controller;

import com.lolsearch.lolrecordsearch.dto.LoginUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/chatroom")
public class ChatController {
    
    
    @GetMapping("/{id}")
    public String getChatRoom(@PathVariable Long id, Authentication authentication, HttpSession session, Model model) {
        
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        session.setAttribute("userId", loginUser.getId());
        
        model.addAttribute("chatRoomId", id);
        model.addAttribute("userId", loginUser.getId());
        model.addAttribute("nickname", loginUser.getNickname());
        
        return "chat/chatroom";
    }
    
    @PostMapping
    public String createChatRoom(Authentication authentication) {
    
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        
        
        
        Long chatRoomId = 1L;
        
        return "redirect:/chatroom/"+chatRoomId;
    }
    
}
