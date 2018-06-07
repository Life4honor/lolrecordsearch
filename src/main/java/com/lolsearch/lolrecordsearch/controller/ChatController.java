package com.lolsearch.lolrecordsearch.controller;

import com.lolsearch.lolrecordsearch.domain.jpa.ChatRoom;
import com.lolsearch.lolrecordsearch.dto.LoginUser;
import com.lolsearch.lolrecordsearch.dto.Pagination;
import com.lolsearch.lolrecordsearch.service.ChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/chatrooms")
public class ChatController {
    
    @Autowired
    private ChatService chatService;
    
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
    public String createChatRoom(String title, Authentication authentication) {
    
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        
        Long chatRoomId = chatService.createChatRoom(loginUser.getId(), title);
    
        return "redirect:/chatrooms/"+chatRoomId;
    }
    
    @GetMapping
    public String getChatRoomPage(@RequestParam(defaultValue = "1") int page, @RequestParam(required = false) String title
            , Model model) {
    
        Page<ChatRoom> chatRoomPage = chatService.findChatRooms(page, title);
    
        List<ChatRoom> chatRooms = chatRoomPage.getContent();
    
        Pagination pagination = new Pagination(chatRoomPage.getTotalElements(), chatRoomPage.getSize(), page,5);
    
        model.addAttribute("chatRooms", chatRooms);
        model.addAttribute("pagination", pagination);
        model.addAttribute("page", page);
        model.addAttribute("title", title);
        
        return "chat/chatrooms";
    }
    
}
