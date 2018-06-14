package com.lolsearch.lolrecordsearch.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@ControllerAdvice
public class ExceptionController {
 
    @ExceptionHandler(Exception.class)
    public String handleError(HttpServletRequest request, Exception e)   {
        log.error("", e);
        
        return "exception/error";
    }
    
}
