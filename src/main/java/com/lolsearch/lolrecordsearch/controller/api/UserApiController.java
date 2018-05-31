package com.lolsearch.lolrecordsearch.controller.api;

import com.lolsearch.lolrecordsearch.dto.UserInfo;
import com.lolsearch.lolrecordsearch.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.Map;
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
    
    
}
