package com.lolsearch.lolrecordsearch.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lolsearch.lolrecordsearch.domain.jpa.*;
import com.lolsearch.lolrecordsearch.dto.*;
import com.lolsearch.lolrecordsearch.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/records")
public class RecordController {

    @Value("${apiKey}")
    private String apiKey;

    @Value("${summonerPath}")
    private String summonerPath;

    @Value("${matchListPath}")
    private String matchListPath;

    @Value("${matchPath}")
    private String matchPath;

    @Value("${championPath}")
    private String championPath;

    @Value("${leaguePositionPath}")
    private String leaguePositionPath;

    @Autowired
    SummonerService summonerService;

    @Autowired
    RecordService recordService;

    @Autowired
    ChampionService championService;

    @Autowired
    MatchReferenceService matchReferenceService;

    @Autowired
    MatchService matchService;

    @GetMapping
    public String recordSearch(){
        return "recordSearch/recordSearchForm";
    }

    @PostMapping("/result")
    public String saveRecords(@RequestParam(name = "type") String type,
                              @RequestParam(name = "summoner") List<String> summoners,
                              ModelMap modelMap){
        // API요청해서 DB 저장
        // 필요한 객체 : ResultDTOList, PlayerDTOList, // LeaguePosition, Summoner, Match, ParticipantIdentity, Participant
        List<String> savedSummonerList = recordService.saveRecords(type, summoners);
        if(savedSummonerList.isEmpty())
            return "recordSearch/recordSearchError";

        return recordResult(type, summoners, modelMap);
    }

    @GetMapping("/result")
    public String recordResult(@RequestParam(name = "type") String type,
                               @RequestParam(name = "summoner") List<String> summoners,
                               ModelMap modelMap) {
        // DB 에서 소환사 명 조회
        if(summoners == null){
            return "recordSearch/recordSearchError";
        }

        modelMap.addAttribute("type", type);
        modelMap.addAttribute("summoner", summoners.get(0));
        Summoner summoner;

        if("single".equals(type)) {
            //게임 상세 정보 출력
            summoner = summonerService.getSummonerByName(summoners.get(0));
            if(summoner == null){
                try {
                    recordService.saveRecords(type, summoners);
                    summoner = summonerService.getSummonerByName(summoners.get(0));
                } catch (Exception e) {
                    log.error("",e);
                    return "recordSearch/recordSearchError";
                }
            }

            List<ResultDTO> resultDTOList = recordService.getResultDTOList(summoner);
            resultDTOList.sort((o1, o2) -> Long.compare(o1.getTimestamp(), o2.getTimestamp()));
            Collections.reverse(resultDTOList);
            modelMap.addAttribute("matches", resultDTOList);

            List<List<PlayerDTO>> playerDTOListResult = recordService.getPlayerDTOListResult(resultDTOList);
            modelMap.addAttribute("playersListResult", playerDTOListResult);

            //소환사 통계 정보 출력
            //LeaguePosition Entity에서 가져와서 출력.
            List<List<LeaguePosition>> leaguePositionListResult = recordService.getLeaguePositionListResult(summoners);
            modelMap.addAttribute("leaguePositionsResult", leaguePositionListResult);

            return "recordSearch/recordSearchResult";
        }else if("multi".equals(type) && summoners.size() >0){
            // 멀티서치 -> 소환사들 통계 정보 출력
            summoners = recordService.saveRecords(type, summoners);

            List<List<LeaguePosition>> leaguePositionListResult = recordService.getLeaguePositionListResult(summoners);
            modelMap.addAttribute("leaguePositionsResult", leaguePositionListResult);

            return "recordSearch/recordSearchResult";
        }else{
            return "recordSearch/recordSearchError";
        }
    }
}