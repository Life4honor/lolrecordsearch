package com.lolsearch.lolrecordsearch.controller;

import com.lolsearch.lolrecordsearch.domain.elasticsearch.SummonerElastic;
import com.lolsearch.lolrecordsearch.domain.jpa.LeaguePosition;
import com.lolsearch.lolrecordsearch.domain.jpa.Summoner;
import com.lolsearch.lolrecordsearch.dto.PlayerDTO;
import com.lolsearch.lolrecordsearch.dto.ResultDTO;
import com.lolsearch.lolrecordsearch.service.RecordService;
import com.lolsearch.lolrecordsearch.service.impl.SummonerElasticServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
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
    RecordService recordService;

    @Autowired
    SummonerElasticServiceImpl summonerElasticService;

    @GetMapping
    public String recordSearch(){
        return "recordSearch/recordSearchForm";
    }

    @PostMapping("/result")
    public String saveRecords(@RequestParam(name = "type") String type,
                              @RequestParam(name = "summoner") List<String> summoners,
                              @RequestParam(name = "beginIndex", required = false, defaultValue = "0")  int beginIndex){
        // API요청해서 DB 저장
        List<String> savedSummonerList = recordService.saveRecords(type, summoners, beginIndex);
        if(savedSummonerList.isEmpty())
            return "recordSearch/recordSearchError";

        StringBuilder stringBuilder = new StringBuilder();
        for (String summoner : summoners) {
            stringBuilder.append("summoner=");
            try {
                String summonerEncoded = URLEncoder.encode(summoner, StandardCharsets.UTF_8.toString());
                stringBuilder.append(summonerEncoded);
                stringBuilder.append("&");
            } catch (UnsupportedEncodingException e) {
                log.error("",e);
            }
        }

        return "redirect:/records/result?"+stringBuilder.toString()+"type="+type+"&beginIndex="+beginIndex;
    }

    @GetMapping("/result")
    public String recordResult(@RequestParam(name = "type") String type,
                               @RequestParam(name = "summoner") List<String> summoners,
                               @RequestParam(name = "beginIndex", required = false, defaultValue = "0")  int beginIndex,
                               ModelMap modelMap) {

        modelMap.addAttribute("type", type);
        modelMap.addAttribute("beginIndex", beginIndex);
        modelMap.addAttribute("summoner", summoners.get(0));

        // DB 에서 소환사 명 조회
        if(summoners == null){
            return "recordSearch/recordSearchError";
        }

        Summoner summoner;

        if("single".equals(type)) {
            //싱글서치
            //게임 상세 정보 출력
            SummonerElastic summonerElastic = summonerElasticService.findByName(summoners.get(0));
            if(summonerElastic != null){
                summoner = recordService.getSummonerByName(summonerElastic.getName());
            }else{
                summoner = recordService.getSummonerByName(summoners.get(0));
            }
            if(summoner == null){
                String summonerName = trySaveRecords(type, summoners, summoner, beginIndex);
                summoner = recordService.getSummonerByName(summonerName);
            }else{
                long revisionDate = summoner.getRevisionDate();
                long refreshDate = LocalDateTime.now().minusDays(7L).toInstant(ZoneOffset.MAX).toEpochMilli();
                if(Long.compare(revisionDate, refreshDate)<0){
                    String summonerName = trySaveRecords(type, summoners, summoner, beginIndex);
                    summoner = recordService.getSummonerByName(summonerName);
                }
            }

            List<ResultDTO> resultDTOList = recordService.getResultDTOList(summoner);
            resultDTOList.sort((o1, o2) -> Long.compare(o1.getTimestamp(), o2.getTimestamp())*-1);
            modelMap.addAttribute("matches", resultDTOList);

            List<List<PlayerDTO>> playerDTOListResult = recordService.getPlayerDTOListResult(resultDTOList);
            modelMap.addAttribute("playersListResult", playerDTOListResult);

            //소환사 통계 정보 출력
            List<String> summonerNameList = new ArrayList<>();
            for(String summonerName : summoners){
                summonerElastic = summonerElasticService.findByName(summonerName);
                summonerNameList.add(summonerElastic.getName());
            }
            List<List<LeaguePosition>> leaguePositionListResult = recordService.getLeaguePositionListResult(summonerNameList);
            modelMap.addAttribute("leaguePositionsResult", leaguePositionListResult);

            return "recordSearch/recordSearchResult";

        }else if("multi".equals(type) && summoners.size() >0){
            //멀티서치

            List<String> summonerNameList = new ArrayList<>();
            for(String summonerName : summoners){
                SummonerElastic summonerElastic = summonerElasticService.findByName(summonerName);
                summonerNameList.add(summonerElastic.getName());
            }
            recordService.saveRecords(type, summonerNameList, beginIndex);

            List<List<LeaguePosition>> leaguePositionListResult = recordService.getLeaguePositionListResult(summonerNameList);
            modelMap.addAttribute("leaguePositionsResult", leaguePositionListResult);

            return "recordSearch/recordSearchResult";
        }else{
            return "recordSearch/recordSearchError";
        }
    }

    private String trySaveRecords(String type, List<String> summoners, Summoner summoner, int beginIndex){
        try {
            List<String> summonerName = recordService.saveRecords(type, summoners, beginIndex);
            if(summonerName.isEmpty()){
                return "recordSearch/recordSearchError";
            }
            summoner = recordService.getSummonerByName(summonerName.get(0));
        } catch (RuntimeException e) {
            log.error("",e);
            throw e;
        }
        return summoner.getName();
    }
}