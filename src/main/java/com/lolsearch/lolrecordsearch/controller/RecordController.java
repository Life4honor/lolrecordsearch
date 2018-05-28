package com.lolsearch.lolrecordsearch.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping("/records")
public class RecordController {

    @GetMapping
    public String records(){
        log.info("helloInfo");
        log.info("testInfo{}{}",1,"testagain");
        log.debug("helloDeBug");
        log.trace("helloTrace");
        log.warn("helloWarn");
        log.error("helloError");
        return "recordSearchForm";
    }
}
