package com.lolsearch.lolrecordsearch.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

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
    
        return "users/signup";
    }
    
    @GetMapping("/{id}")
    public String getUserInfo(@PathVariable Long id) {
        
        return "users/userinfo";
    }
    
    @DeleteMapping("/{id}")
    public String withdraw(@PathVariable Long id) {
    
        return "";
    }
    
    @GetMapping("/{id}/friends")
    public String getFriends(@PathVariable Long id) {
    
        return "";
    }
    
    @GetMapping("/api")
    @ResponseBody
    public Map<String, String> test() {
        Map<String, String> map = new HashMap<>();
        map.put("aaa", "aaa");
        map.put("bbb", "bbb");
    
        return map;
    }
    
    
}
