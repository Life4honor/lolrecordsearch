package com.lolsearch.lolrecordsearch.controller;

import com.lolsearch.lolrecordsearch.dto.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Slf4j
@Controller
@RequestMapping("/users")
public class UserController {
    
    
    @PreAuthorize("isAnonymous()")
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
    
    @PreAuthorize("isAnonymous()")
    @GetMapping("/signup")
    public String signup(UserInfo userInfo, HttpServletRequest request) {
        
        String referer = request.getHeader(HttpHeaders.REFERER);
    
        request.getSession().setAttribute("referer", referer);
        
        return "users/signup";
    }
    
    @PreAuthorize("isAnonymous()")
    @PostMapping
    public String registUser(@Valid UserInfo userInfo, BindingResult result) {
        if(result.hasErrors()) {
            return "users/signup";
        }
        
        //TODO 아이디 중복 체크
        //TODO 닉네임 중복 체크
        //TODO 소환사명 중복 체크
        
        return "redirect:/users/login";
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
