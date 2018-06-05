package com.lolsearch.lolrecordsearch.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lolsearch.lolrecordsearch.domain.*;
import com.lolsearch.lolrecordsearch.dto.*;
import com.lolsearch.lolrecordsearch.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.LinkedHashMap;
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
    public String refreshResult(@RequestParam(name = "type") String type,
                                @RequestParam(name = "summoner") List<String> summoners,
                                ModelMap modelMap){

        // API요청해서 DB 저장
        // 필요한 객체 : ResultDTOList, PlayerDTOList, // LeaguePosition, Summoner, Match, ParticipantIdentity, Participant
        RestTemplate restTemplate = new RestTemplate();
        for(String summonerName : summoners) {
            SummonerDTO summonerDTO = restTemplate.getForObject(summonerPath + summonerName + "?api_key=" + apiKey, SummonerDTO.class);
            Summoner summoner = summonerService.getSummonerByName(summonerName);
            // List로 한꺼번에 저장하기??
            if(summoner == null) {
                summoner = summonerService.saveSummoner(summonerDTO);
            }

            MatchlistDTO matchlistDTO = restTemplate.getForObject(matchListPath+summoner.getAccountId()+"?beginIndex=0&endIndex=3&api_key=" + apiKey,MatchlistDTO.class);
            List<MatchReferenceDTO> matchReferenceDTOList = matchlistDTO.getMatches();
            for(MatchReferenceDTO matchReferenceDTO: matchReferenceDTOList) {
                MatchReference matchReference = matchReferenceService.getMatchReferencByGameId(matchReferenceDTO.getGameId());
                // List로 한꺼번에 저장하기??
                if(matchReference == null) {
                    matchReference = matchReferenceService.saveMatchReference(matchReferenceDTO);
                }
                MatchDTO matchDTO = restTemplate.getForObject(matchPath+matchReference.getGameId()+"?api_key="+apiKey, MatchDTO.class);

                List<ParticipantIdentityDTO> participantIdentityDTOList = matchDTO.getParticipantIdentities();
                for(ParticipantIdentityDTO participantIdentityDTO : participantIdentityDTOList){
                    ParticipantIdentity participantIdentity = new ParticipantIdentity();
                    participantIdentity.setGameId(matchReference.getGameId());
                    participantIdentity.setParticipantId(Long.valueOf(participantIdentityDTO.getParticipantId()));
                    PlayerDTO playerDTO = participantIdentityDTO.getPlayer();
                    ObjectMapper mapper = new ObjectMapper();
                    try {
                        participantIdentity.setPlayer(mapper.writeValueAsString(playerDTO));
                    } catch (JsonProcessingException e) {
                        e.printStackTrace();
                    }
                    // List로 한꺼번에 저장하기
                    if(recordService.getParticipantIdentitiesByGameId(participantIdentity.getGameId()) == null) {
                        recordService.saveParticipantIdentity(participantIdentity);
                    }
                }

                Match match = new Match();
                List<ParticipantDTO> participantDTOList = matchDTO.getParticipants();
                for(ParticipantDTO participantDTO : participantDTOList){
                    if(matchReferenceDTO.getChampion() == participantDTO.getChampionId()){
//                        ChampionDTO championDTO = restTemplate.getForObject(championPath+matchReferenceDTO.getChampion()+"?api_key="+apiKey, ChampionDTO.class);
                        if(championService.getChampionById(matchReferenceDTO.getChampion()) == null) {
//                            championService.saveChampion(championDTO);
                        }
                        match.setKills(participantDTO.getStats().getKills());
                        match.setAssists(participantDTO.getStats().getAssists());
                        match.setDeaths(participantDTO.getStats().getDeaths());

                        if(participantDTO.getStats().getWin()){
                            match.setWin("Win!!!");
                        }else{
                            match.setWin("LoseT-T");
                        }
                    }
                }
                match.setMatchReference(matchReference);
                match.setSummoner(summoner);
                Match matchCheck = matchService.getMatch(matchReference.getGameId());
                if(matchCheck == null) {
                    matchService.saveMatch(match);
                }
            }

            ParameterizedTypeReference<List<LeaguePositionDTO>> myBean =
                    new ParameterizedTypeReference<List<LeaguePositionDTO>>() {};

            ResponseEntity<List<LeaguePositionDTO>> response =
                    restTemplate.exchange(leaguePositionPath + summoner.getId() + "?api_key=" + apiKey, HttpMethod.GET, null, myBean);

            List<LeaguePositionDTO> leaguePositionDTOList = response.getBody();
            //List로 한꺼번에 저장하기
            for(LeaguePositionDTO leaguePositionDTO: leaguePositionDTOList){
                if(recordService.getLeaguePositionByNameAndQueueType(leaguePositionDTO.getPlayerOrTeamName(), leaguePositionDTO.getQueueType()) == null) {
                    recordService.saveLeaguePosition(leaguePositionDTO);
                }
            }
        }
        return recordResult(type, summoners, modelMap);
    }

    @GetMapping("/result")
    public String recordResult(@RequestParam(name = "type") String type,
                               @RequestParam(name = "summoner") List<String> summoners,
                               ModelMap modelMap) {
        // DB 에서 소환사 명 조회
        if(summoners == null){
            summoners = new ArrayList<>();
        }
        modelMap.addAttribute("type", type);

        if("single".equals(type) && summoners.size()>0) {

            //소환사 통계 정보 출력
            //LeaguePosition Entity에서 가져와서 출력.
            List<LeaguePosition> leaguePositionList = recordService.getLeaguePositionList(summoners);
            if(leaguePositionList.size()==0){
                return "recordSearch/recordSearchError";
            }
            modelMap.addAttribute("leaguePositions", leaguePositionList);

            //게임 상세 정보 출력
            Summoner summoner = summonerService.getSummonerByName(summoners.get(0));
            if(summoner == null){
                return "recordSearch/recordSearchError";
            }

            List<ResultDTO> resultDTOList = recordService.getResultDTOList(summoner);
            List<PlayerDTO> playerDTOList = recordService.getPlayerDTOList(resultDTOList);
            modelMap.addAttribute("players", playerDTOList);
            modelMap.addAttribute("matches", resultDTOList);
            return "recordSearch/recordSearchResult";
        }else if("multi".equals(type) && summoners.size() >0){
            // 멀티서치 -> 소환사들 통계 정보 출력
            List<LeaguePosition> leaguePositionList = recordService.getLeaguePositionList(summoners);
            if(leaguePositionList.size()==0){
                return "recordSearch/recordSearchError";
            }
            modelMap.addAttribute("leaguePositions", leaguePositionList);
            return "recordSearch/recordSearchResult";
        }else{
            return "recrdSearch/reocrdSearchError";
        }
    }
}


//데이터베이스에 소환사명 조회 결과 없을 시 api에 정보 요청
//            if(leaguePositionList==null){
//                SummonerDTO summoner = restTemplate.getForObject(summonerPath + name + "?api_key=" + apiKey, SummonerDTO.class);
//                leaguePositionList = restTemplate.getForObject(leaguePositionPath + summoner.getId()+"?api_key=" + apiKey, List.class);
//                modelMap.addAttribute("leaguePositions", leaguePositionList);
//                return "recordSearch/recordSearchResult";
//            }


//                SummonerDTO summonerDTO = restTemplate.getForObject(summonerPath + name + "?api_key=" + apiKey, SummonerDTO.class);
//                summoner = summonerService.saveSummoner(summonerDTO);
////                MatchlistDTO matchlistDTO = restTemplate.getForObject("https://kr.api.riotgames.com/lol/match/v3/matchlists/by-account/"+ summonerDTO.getAccountId() + "?beginIndex=0&endIndex=5&api_key="+apiKey, MatchlistDTO.class);
//                MatchlistDTO matchlistDTO = restTemplate.getForObject(matchListPath + summonerDTO.getAccountId() + "?beginIndex=0&endIndex=5&api_key="+apiKey, MatchlistDTO.class);
//                for(MatchReferenceDTO matchReferenceDTO :matchlistDTO.getMatches()) {
//                    MatchReference matchReference = matchReferenceService.saveMatchReference(matchReferenceDTO);
//                    Match match = new Match();
//                    match.setSummoner(summoner);
//                    match.setMatchReference(matchReference);
//
//                    MatchDTO matchDTO = restTemplate.getForObject(matchPath + matchReference.getGameId() + "?api_key=" + apiKey,MatchDTO.class);
//                    List<ParticipantDTO> participantDTOList = matchDTO.getParticipants();
//                    for(ParticipantDTO participantDTO : participantDTOList){
//                        if(participantDTO.getChampionId() == match.getMatchReference().getChampionId()) {
//                            match.setKills(participantDTO.getStats().getKills());
//                            match.setDeaths(participantDTO.getStats().getDeaths());
//                            match.setAssists(participantDTO.getStats().getAssists());
//                            match.setWin("WIN!!!");
//                        }
//                        Participant participant = new Participant();
//                        participant.setChampionId(participantDTO.getChampionId());
//                        participant.setParticipantId(Long.valueOf(participantDTO.getParticipantId()));
//                        ObjectMapper mapper = new ObjectMapper();
//                        ParticipantStatsDTO participantStatsDTO = new ParticipantStatsDTO();
//                        String jsonInString = null;
//                        try {
//                            jsonInString = mapper.writeValueAsString(participantStatsDTO);
//                        } catch (JsonProcessingException e) {
//                            e.printStackTrace();
//                        }
//                        participant.setStats(jsonInString);
//                        participant.setGameId(matchDTO.getGameId());
//                        //participant Service로 데이터베이스 저장하기
//                        recordService.saveParticipant(participant);
//                    }
//                    List<ParticipantIdentityDTO> participantIdentityDTOList = matchDTO.getParticipantIdentities();
//                    for(ParticipantIdentityDTO participantIdentityDTO: participantIdentityDTOList){
//                        ParticipantIdentity participantIdentity = new ParticipantIdentity();
//                        participantIdentity.setGameId(matchDTO.getGameId());
//                        participantIdentity.setParticipantId(Long.valueOf(participantIdentityDTO.getParticipantId()));
//                        participantIdentity.setPlayer(participantIdentityDTO.getPlayer().toString());
//
//                        //participantIdentity Service로 데이터베이스 저장하기
//                        recordService.saveParticipantIdentity(participantIdentity);
//                    }
//                    matchReference.saveMatch(match);
//                    summoner.saveMatch(match);
//                    matchService.saveMatch(match);
//                }
//                summoner.getMatches().forEach(match -> {
//                    ResultDTO resultDTO = new ResultDTO();
//                    recordService.setResultDTO(match, resultDTO);
//                    resultDTOList.add(resultDTO);
//                });
//                RecordSearch(recordDTOList, resultDTOList, playerDTOList);
//
//                modelMap.addAttribute("players", playerDTOList);
//                modelMap.addAttribute("matches", resultDTOList);
//                modelMap.addAttribute("participants", recordDTOList);
//                return "recordSearch/recordSearchResult";
//            }else {