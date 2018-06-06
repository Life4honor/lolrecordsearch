package com.lolsearch.lolrecordsearch.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
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
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
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

    @GetMapping("/result")
    public String recordResult(@RequestParam(name = "type") String type,
                               @RequestParam(name = "summoner") List<String> summoners,
                               ModelMap modelMap) {
        // DB 에서 소환사 명 조회
        RestTemplate restTemplate = new RestTemplate();

        if(summoners == null){
            summoners = new ArrayList<>();
        }
        modelMap.addAttribute("type", type);

        if("single".equals(type) && summoners.size()>0) {

            //검색어로 입력한 소환사명 name에 입력.
            String name = summoners.get(0).replaceAll(" ","");
            //소환사 통계 정보 출력
            //LeaguePosition Entity에서 가져와서 출력.
            LeaguePosition leaguePositionSolo = recordService.getLeaguePositionByNameAndQueueType(name, "RANKED_SOLO_5x5");
            LeaguePosition leaguePositionFlex = recordService.getLeaguePositionByNameAndQueueType(name, "RANKED_FLEX_SR");
            List<LeaguePosition> leaguePositionList = new ArrayList<>();
            if(leaguePositionSolo != null){
                leaguePositionList.add(leaguePositionSolo);
            }
            if(leaguePositionFlex != null){
                leaguePositionList.add(leaguePositionFlex);
            }

            //데이터베이스에 소환사명 조회 결과 없을 시 api에 정보 요청
            if(leaguePositionList==null){
                SummonerDTO summoner = restTemplate.getForObject(summonerPath + name + "?api_key=" + apiKey, SummonerDTO.class);
                leaguePositionList = restTemplate.getForObject(leaguePositionPath + summoner.getId()+"?api_key=" + apiKey, List.class);
                modelMap.addAttribute("leaguePositions", leaguePositionList);
                return "recordSearch/recordSearchResult";
            }

            modelMap.addAttribute("leaguePositions", leaguePositionList);

            //게임 상세 정보 출력
            Summoner summoner = summonerService.getSummonerByName(name);
            List<RecordDTO> recordDTOList = new ArrayList<>();
            List<ResultDTO> resultDTOList = new ArrayList<>();
            List<PlayerDTO> playerDTOList = new ArrayList<>();
            if(summoner==null){
                SummonerDTO summonerDTO = restTemplate.getForObject(summonerPath + name + "?api_key=" + apiKey, SummonerDTO.class);
                summoner = summonerService.addSummoner(summonerDTO);
//                MatchlistDTO matchlistDTO = restTemplate.getForObject("https://kr.api.riotgames.com/lol/match/v3/matchlists/by-account/"+ summonerDTO.getAccountId() + "?beginIndex=0&endIndex=5&api_key="+apiKey, MatchlistDTO.class);
                MatchlistDTO matchlistDTO = restTemplate.getForObject(matchListPath + summonerDTO.getAccountId() + "?beginIndex=0&endIndex=5&api_key="+apiKey, MatchlistDTO.class);
                for(MatchReferenceDTO matchReferenceDTO :matchlistDTO.getMatches()) {
                    MatchReference matchReference = matchReferenceService.addMatchReference(matchReferenceDTO);
                    Match match = new Match();
                    match.setSummoner(summoner);
                    match.setMatchReference(matchReference);

                    MatchDTO matchDTO = restTemplate.getForObject(matchPath + matchReference.getGameId() + "?api_key=" + apiKey,MatchDTO.class);
                    List<ParticipantDTO> participantDTOList = matchDTO.getParticipants();
                    for(ParticipantDTO participantDTO : participantDTOList){
                        if(participantDTO.getChampionId() == match.getMatchReference().getChampionId()) {
                            match.setKills(participantDTO.getStats().getKills());
                            match.setDeaths(participantDTO.getStats().getDeaths());
                            match.setAssists(participantDTO.getStats().getAssists());
                            match.setWin("WIN!!!");
                        }
                        Participant participant = new Participant();
                        participant.setChampionId(participantDTO.getChampionId());
                        participant.setParticipantId(Long.valueOf(participantDTO.getParticipantId()));
                        ObjectMapper mapper = new ObjectMapper();
                        ParticipantStatsDTO participantStatsDTO = new ParticipantStatsDTO();
                        String jsonInString = null;
                        try {
                            jsonInString = mapper.writeValueAsString(participantStatsDTO);
                        } catch (JsonProcessingException e) {
                            e.printStackTrace();
                        }
                        participant.setStats(jsonInString);
                        participant.setGameId(matchDTO.getGameId());
                        //participant Service로 데이터베이스 저장하기
                        recordService.addParticipant(participant);
                    }
                    List<ParticipantIdentityDTO> participantIdentityDTOList = matchDTO.getParticipantIdentities();
                    for(ParticipantIdentityDTO participantIdentityDTO: participantIdentityDTOList){
                        ParticipantIdentity participantIdentity = new ParticipantIdentity();
                        participantIdentity.setGameId(matchDTO.getGameId());
                        participantIdentity.setParticipantId(Long.valueOf(participantIdentityDTO.getParticipantId()));
                        participantIdentity.setPlayer(participantIdentityDTO.getPlayer().toString());

                        //participantIdentity Service로 데이터베이스 저장하기
                        recordService.addParticipantIdentity(participantIdentity);
                    }
                    matchReference.addMatch(match);
                    summoner.addMatch(match);
                    matchService.addMatch(match);
                }
                summoner.getMatches().forEach(match -> {
                    ResultDTO resultDTO = new ResultDTO();
                    recordService.setResultDTO(match, resultDTO);
                    resultDTOList.add(resultDTO);
                });
                resultDTOList.forEach(resultDTO -> {
                    Long gameId = resultDTO.getGameId();
                    //DB 조회
                    List<ParticipantIdentity> participantIdentities = recordService.getParticipantIdentitiesByGameId(gameId);
                    if(participantIdentities==null){

                    }
                    //DB 조회
                    List<Participant> participants = recordService.getParticipantsByGameId(gameId);
                    if (participants.size() > 0) {
                        for (int i = 0; i < participants.size(); i++) {
                            RecordDTO recordDTO = new RecordDTO();
                            recordService.setRecordDTO(recordDTO, participantIdentities.get(i), participants.get(i));
                            recordDTOList.add(recordDTO);

                            //String -> JSON -> Object
                            String jsonString = recordDTO.getParticipantIdentity().getPlayer();
                            ObjectMapper mapper = new ObjectMapper();
                            try {
                                JsonNode actualObj = mapper.readTree(jsonString);
                                PlayerDTO playerDTO = mapper.readValue(actualObj.traverse(), PlayerDTO.class);
                                playerDTOList.add(playerDTO);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });

                modelMap.addAttribute("players", playerDTOList);
                modelMap.addAttribute("matches", resultDTOList);
                modelMap.addAttribute("participants", recordDTOList);
                return "recordSearch/recordSearchResult";
            }else {
                summoner.getMatches().forEach(match -> {
                    ResultDTO resultDTO = new ResultDTO();
                    recordService.setResultDTO(match, resultDTO);
                    resultDTOList.add(resultDTO);
                });
//                RecordSearch(recordDTOList, resultDTOList, playerDTOList);
            }

            modelMap.addAttribute("players", playerDTOList);
            modelMap.addAttribute("matches", resultDTOList);
            modelMap.addAttribute("participants", recordDTOList);
            return "recordSearch/recordSearchResult";
        }else if("multi".equals(type) && summoners.size() >0){

            // 멀티서치 -> 소환사들 통계 정보 출력
            List<LeaguePosition> leaguePositionList = new ArrayList<>();
            summoners.forEach(s -> {
                LeaguePosition leaguePositionSolo = recordService.getLeaguePositionByNameAndQueueType(s.replaceAll(" ",""),"RANKED_SOLO_5x5");
                LeaguePosition leaguePositionFlex = recordService.getLeaguePositionByNameAndQueueType(s.replaceAll(" ",""),"RANKED_FLEX_SR");
                if(leaguePositionSolo != null){
                    leaguePositionList.add(leaguePositionSolo);
                }
                if(leaguePositionFlex != null){
                    leaguePositionList.add(leaguePositionFlex);
                }
            });
            if(leaguePositionList.size()==0){
                return "recordSearch/recordSearchError";
            }
            modelMap.addAttribute("leaguePositions", leaguePositionList);
            return "recordSearch/recordSearchResult";
        }else{
            return "redirect:/records";
        }

        // DB에서 소환사 명 조회 결과 있을 시 DB 내용 보여주기

        // DB에서 소환사 명 조회 결과 없을 시 실행.


    }

//    private void RecordSearch(List<RecordDTO> recordDTOList, List<ResultDTO> resultDTOList, List<PlayerDTO> playerDTOList) {
//        resultDTOList.forEach(resultDTO -> {
//            Long gameId = resultDTO.getGameId();
//            //DB 조회
//            List<ParticipantIdentity> participantIdentities = recordService.getParticipantIdentitiesByGameId(gameId);
//            //DB 조회
//            List<Participant> participants = recordService.getParticipantsByGameId(gameId);
//            if (participants.size() > 0) {
//                for (int i = 0; i < participants.size(); i++) {
//                    RecordDTO recordDTO = new RecordDTO();
//                    recordService.setRecordDTO(recordDTO, participantIdentities.get(i), participants.get(i));
//                    recordDTOList.add(recordDTO);
//
//                    //String -> JSON -> Object
//                    String jsonString = recordDTO.getParticipantIdentity().getPlayer();
//                    ObjectMapper mapper = new ObjectMapper();
//                    try {
//                        JsonNode actualObj = mapper.readTree(jsonString);
//                        PlayerDTO playerDTO = mapper.readValue(actualObj.traverse(), PlayerDTO.class);
//                        playerDTOList.add(playerDTO);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        });
//    }
}