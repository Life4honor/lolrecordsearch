package com.lolsearch.lolrecordsearch.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/users")
public class UserController {
    
    @GetMapping("/login")
    public String login() {
        
        return "users/login";
    }
    
    @GetMapping("/signup")
    public String signup() {
    
        return "";
    }
    
    @GetMapping("/{id}")
    public String getUserInfo(@PathVariable Long id) {
        
        return "";
    }
    
    @DeleteMapping("/{id}")
    public String withdraw(@PathVariable Long id) {
    
        return "";
    }
    
    @GetMapping("/{id}/friends")
    public String getFriends(@PathVariable Long id) {
    
        return "";
    }
    
    
}
