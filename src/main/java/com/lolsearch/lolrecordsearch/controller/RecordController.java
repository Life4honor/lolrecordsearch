package com.lolsearch.lolrecordsearch.controller;

import com.lolsearch.lolrecordsearch.dto.SummonerDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/records")
public class RecordController {

    @GetMapping
    public String recordSearch(){
        return "recordSearch/recordSearchForm";
    }

    @GetMapping("/result")
    public String recordResult(@RequestParam(name = "type") String type,
                               @RequestParam(name = "summoner") List<String> summoners,
                               ModelMap modelMap){
        List<SummonerDTO> summonerDTOList = new ArrayList<>();
        summoners.forEach(s -> {
            RestTemplate restTemplate = new RestTemplate();
            SummonerDTO summonerDTO = restTemplate.getForObject("https://kr.api.riotgames.com/lol/summoner/v3/summoners/by-name/"+s+"?api_key=RGAPI-ae9dcdfb-50f1-4abf-bfae-134f6ca75038",
                    SummonerDTO.class);
            summonerDTOList.add(summonerDTO);
            });

        summonerDTOList.forEach(s ->log.info(s.toString()));
        modelMap.addAttribute("summoners", summonerDTOList);
        return "recordSearch/recordSearchResult";
    }
}