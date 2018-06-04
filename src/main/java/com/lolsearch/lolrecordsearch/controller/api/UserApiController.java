package com.lolsearch.lolrecordsearch.controller.api;

import com.lolsearch.lolrecordsearch.domain.User;
import com.lolsearch.lolrecordsearch.dto.UserInfo;
import com.lolsearch.lolrecordsearch.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

@RequestMapping("/api/users")
@RestController
public class UserApiController {
    
    @Autowired
    private UserService userService;
    
    @PostMapping("/checkEmail")
    public ResponseEntity<Map<String, String>> checkEmail(@RequestBody UserInfo userInfo) {
        
        if(StringUtils.isBlank(userInfo.getEmail())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        
        Pattern pattern = Pattern.compile("(.){4,20}@(.){2,10}\\.(.){2,20}");
        if(!pattern.matcher(userInfo.getEmail()).find()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        
        boolean exist = userService.isExistEmail(userInfo.getEmail());
        
        if(exist) {
            Map<String, String> map = Collections.singletonMap("message", "이미 등록된 이메일 입니다.");
            return new ResponseEntity<>(map, HttpStatus.CONFLICT );
        }
    
        Map<String, String> map = Collections.singletonMap("message", "사용 가능한 이메일 입니다.");
        return new ResponseEntity<>(map, HttpStatus.OK);
    }
    
    @PostMapping("/checkNickname")
    public ResponseEntity<Map<String, String>> checkNickname(@RequestBody UserInfo userInfo) {
    
        if(StringUtils.isBlank(userInfo.getNickname())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        
        boolean exist = userService.isExistNickname(userInfo.getNickname());
        
        if(exist) {
            Map<String, String> map = Collections.singletonMap("message", "이미 등록된 닉네임 입니다.");
            return new ResponseEntity<>(map, HttpStatus.CONFLICT );
        }
    
        Map<String, String> map = Collections.singletonMap("message", "사용 가능한 닉네임 입니다.");
        return new ResponseEntity<>(map, HttpStatus.OK);
    }
    
    @PostMapping("/checkSummoner")
    public ResponseEntity<Map<String, String>> checkSummoner(@RequestBody UserInfo userInfo) {
    
        if(StringUtils.isBlank(userInfo.getSummoner())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        
        boolean exist = userService.isExistSummoner(userInfo.getSummoner());
        if(exist) {
            Map<String, String> map = Collections.singletonMap("message", "이미 등록된 소환사명 입니다.");
            return new ResponseEntity<>(map, HttpStatus.CONFLICT );
        }
    
        Map<String, String> map = Collections.singletonMap("message", "사용 가능한 소환사명 입니다.");
        return new ResponseEntity<>(map, HttpStatus.OK);
    }
    
    @PreAuthorize("#id == authentication.principal.id")
    @PutMapping("/{id}/nickname")
    public ResponseEntity<Map<String, String>> changeNickname(@PathVariable Long id, @RequestBody UserInfo userInfo) {
        
        if(StringUtils.isBlank(userInfo.getNickname())) {
            Map<String, String> map = Collections.singletonMap("message", "닉네임을 입력해 주세요.");
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }
        
        boolean existNickname = userService.isExistNickname(userInfo.getNickname());
        if(existNickname) {
            Map<String, String> map = Collections.singletonMap("message", "이미 존재하는 닉네임 입니다.");
            return new ResponseEntity<>(map, HttpStatus.CONFLICT);
        }
    
        userService.modifyNickname(id, userInfo.getNickname());
        
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    
    @PreAuthorize("#id == authentication.principal.id")
    @PutMapping("/{id}/summoner")
    public ResponseEntity<Map<String, String>> changeSummoner(@PathVariable Long id, @RequestBody UserInfo userInfo) {
    
        if(StringUtils.isBlank(userInfo.getSummoner())) {
            Map<String, String> map = Collections.singletonMap("message", "소환사명을 입력해 주세요.");
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }
    
        boolean existSummoner = userService.isExistSummoner(userInfo.getSummoner());
        if(existSummoner) {
            Map<String, String> map = Collections.singletonMap("message", "이미 존재하는 소환사명 입니다.");
            return new ResponseEntity<>(map, HttpStatus.CONFLICT);
        }
    
        userService.modifySummoner(id, userInfo.getSummoner());
        
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    
    @PreAuthorize("#id == authentication.principal.id")
    @PutMapping("/{id}/password")
    public ResponseEntity<Map<String, String>> changePassword(@PathVariable Long id, @RequestBody UserInfo userInfo) {
    
        if(StringUtils.isBlank(userInfo.getPassword())) {
            Map<String, String> map = Collections.singletonMap("message", "비밀번호를 입력해 주세요.");
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }
        
        if(!userInfo.getPassword().equals(userInfo.getRePassword())) {
            Map<String, String> map = Collections.singletonMap("message", "비밀번호가 일치하지 않습니다.");
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }
        
        long count = userService.modifyPassword(id, userInfo);
        if(count == 0) {
            Map<String, String> map = Collections.singletonMap("message", "잘못된 비밀번호 입니다.");
            return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
        }
        
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    
}
