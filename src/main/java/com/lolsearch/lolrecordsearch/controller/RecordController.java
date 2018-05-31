package com.lolsearch.lolrecordsearch.controller;

import com.lolsearch.lolrecordsearch.domain.Participant;
import com.lolsearch.lolrecordsearch.domain.ParticipantIdentity;
import com.lolsearch.lolrecordsearch.domain.Summoner;
import com.lolsearch.lolrecordsearch.dto.*;
import com.lolsearch.lolrecordsearch.service.ParticipantIdentityService;
import com.lolsearch.lolrecordsearch.service.ParticipantService;
import com.lolsearch.lolrecordsearch.service.SummonerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@Slf4j
@Controller
@RequestMapping("/records")
public class RecordController {

    @Value("${apiKey}")
    private String apiKey;

    @Value("${summonerPath}")
    private String summonerPath;

    @Value("${matchListPath")
    private String matchListPath;

    @Value("${matchPath}")
    private String matchPath;

    @Value("${championPath}")
    private String championPath;

    @Autowired
    SummonerService summonerService;

    @Autowired
    ParticipantIdentityService participantIdentityService;

    @Autowired
    ParticipantService participantService;

    @GetMapping
    public String recordSearch(){
        return "recordSearch/recordSearchForm";
    }

    @GetMapping("/result")
    public String recordResult(@RequestParam(name = "type") String type,
                               @RequestParam(name = "summoner") List<String> summoners,
                               ModelMap modelMap) {
        // DB 에서 소환사 명 조회
        if(summoners == null){
            summoners = new ArrayList<>();
        }

        if("single".equals(type) && summoners.size()>0) {
            String name = summoners.get(0);
            Summoner summoner = summonerService.getSummonerByName(name);
            List<RecordDTO> recordDTOList = new ArrayList<>();
            List<ResultDTO> resultDTOList = new ArrayList<>();
            summoner.getMatches().forEach(match -> {
                ResultDTO resultDTO = new ResultDTO();
                resultDTO.setGameId(match.getMatchReference().getGameId());
                resultDTO.setChampion(match.getMatchReference().getChampion());
                resultDTO.setRole(match.getMatchReference().getRole());
                resultDTOList.add(resultDTO);
            });
            resultDTOList.forEach(resultDTO -> {
                Long gameId = resultDTO.getGameId();
                List<ParticipantIdentity> participantIdentities = participantIdentityService.getParticipantIdentitiesByGameId(gameId);
                List<Participant> participants = participantService.getParticipantsByGameId(gameId);;
                if(resultDTOList.size()>0) {
                    for (int i = 0; i < resultDTOList.size(); i++) {
                        RecordDTO recordDTO = new RecordDTO();
                        recordDTO.setParticipantIdentity(participantIdentities.get(i));
                        recordDTO.setParticipant(participants.get(i));
                        recordDTOList.add(recordDTO);
                    }
                }
            });
            modelMap.addAttribute("summoner")
            return "recordSearch/recordSearchResult";
        }else if("multi".equals(type) && summoners.size() >0){
            // 멀티서치
            List<SummonerDTO> summonerDTOList = new ArrayList<>();
            summoners.forEach(s -> {
                RestTemplate restTemplate = new RestTemplate();
                SummonerDTO summonerDTO = restTemplate.getForObject(summonerPath + s + "?api_key="+apiKey, SummonerDTO.class);
                summonerDTOList.add(summonerDTO);
            });

            summonerDTOList.forEach(s -> {
                long accountId = s.getAccountId();
                RestTemplate restTemplate = new RestTemplate();
                MatchlistDTO matchlistDTO = restTemplate.getForObject(matchListPath + accountId + "?api_key="+ apiKey, MatchlistDTO.class);
            });
            modelMap.addAttribute("summoners", summonerDTOList);
            return "recordSearch/recordSearchResult";
        }else{
            return "redirect:/records";
        }

        // DB에서 소환사 명 조회 결과 있을 시 DB 내용 보여주기

        // DB에서 소환사 명 조회 결과 없을 시 실행.


    }
}