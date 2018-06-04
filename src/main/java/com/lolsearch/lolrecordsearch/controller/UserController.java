package com.lolsearch.lolrecordsearch.controller;

import com.lolsearch.lolrecordsearch.domain.User;
import com.lolsearch.lolrecordsearch.dto.UserInfo;
import com.lolsearch.lolrecordsearch.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/users")
public class UserController {
    
    @Autowired
    private UserService userService;
    
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
    
        if(!userInfo.getPassword().equals(userInfo.getRePassword())){
            result.addError(new FieldError("userInfo","password","패스워드가 일치하지 않습니다."));
            return "members/join";
        }
    
        userService.registUser(userInfo);
        
        return "redirect:/users/login";
    }
    
    @PreAuthorize("#id == authentication.principal.id")
    @GetMapping("/{id}")
    public String getUserInfo(@PathVariable Long id, Model model) {
    
        User user = userService.findUser(id);
    
        UserInfo userInfo = new UserInfo();
        BeanUtils.copyProperties(user, userInfo,"password");
    
        model.addAttribute("userInfo", userInfo);
        
        return "users/userinfo";
    }
    
    @PreAuthorize("#id == authentication.principal.id")
    @DeleteMapping("/{id}")
    public String withdraw(@PathVariable Long id) {
    
        userService.withdrawUser(id);
        
        SecurityContextHolder.clearContext();
        
        return "redirect:/";
    }
    
    @PreAuthorize("#id == authentication.principal.id")
    @GetMapping("/{id}/friends")
    public String getFriends(@PathVariable Long id) {
        
        //TODO 친구목록
        
        return "users/friends";
    }
    
    @PreAuthorize("#id == authentication.principal.id")
    @DeleteMapping("/{id}/friends")
    public String deleteFriends(@PathVariable Long id) {
        
        return "users/friends";
    }
    
    @GetMapping("/findEmail")
    public String getFindEmailForm() {
        
        return "users/findEmail";
    }
    
    @PostMapping("/findEmail")
    public String findEmail(String nickname, Model model) {
        
        Optional<String> optional = userService.findUserEmail(nickname);
        String result = "가입되지 않은 닉네임 입니다.";
        if(optional.isPresent()) {
            result = optional.get();
        }
        
        model.addAttribute("title", "아이디 찾기");
        model.addAttribute("msg", "아이디 찾기 결과");
        model.addAttribute("result", result);
        
        return "users/findResult";
    }
    
    @GetMapping("/findPassword")
    public String getFindPasswordForm() {
        
        return "users/findPassword";
    }
    
    @PostMapping("/findPassword")
    public String findPassword(String email, String nickname, Model model) {
        
        String result = "비밀번호를 찾을 수 없습니다.";
        Optional<String> optional = userService.findUserPassword(email, nickname);
        if(optional.isPresent()) {
            result = optional.get();
        }
        
        model.addAttribute("title", "비밀번호 찾기");
        model.addAttribute("msg", "임시 비밀번호");
        model.addAttribute("result", result);
        
        return"users/findResult";
    }
    
}
