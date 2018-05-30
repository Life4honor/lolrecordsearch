package com.lolsearch.lolrecordsearch.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Controller
@RequestMapping("/users")
public class UserController {
    
    @GetMapping("/login")
    public String login(HttpServletRequest request) {
    
        HttpSession session = request.getSession();
        String referer = (String) session.getAttribute("referer");
        
        if(referer == null) {
            referer = request.getHeader(HttpHeaders.REFERER);
        }
    
        session.setAttribute("referer", referer);
        
        return "users/login";
    }
    
    @GetMapping("/signup")
    public String signup(HttpServletRequest request) {
    
        String referer = request.getHeader(HttpHeaders.REFERER);
    
        request.getSession().setAttribute("referer", referer);
        
        return "users/signup";
    }
    
    @GetMapping("/{id}")
    public String getUserInfo(@PathVariable Long id) {
        
        return "users/userinfo";
    }
    
    @DeleteMapping("/{id}")
    public String withdraw(@PathVariable Long id) {
    
        return "redirect:/";
    }
    
    @GetMapping("/{id}/friends")
    public String getFriends(@PathVariable Long id) {
    
        return "users/friends";
    }
    
    @DeleteMapping("/{id}/friends")
    public String deleteFriends(@PathVariable Long id) {
        
        return "";
    }
}
