package com.lolsearch.lolrecordsearch.domain;

import com.lolsearch.lolrecordsearch.dto.PlayerDTO;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "participants_identities")
@Getter @Setter
public class ParticipantIdentity {

    @Id
    private Long participantId;

    private Long gameId;

    @Lob
    private String player;
//    private PlayerDTO player;
}
//{
//        "player": {
//        "currentPlatformId": "KR",
//        "summonerName": "호호호 오오오",
//        "matchHistoryUri": "/v1/stats/player_history/KR/206378125",
//        "platformId": "KR",
//        "currentAccountId": 206378125,
//        "profileIcon": 1297,
//        "summonerId": 38051242,
//        "accountId": 206378125
//        },
//        "participantId": 1
//        },
//        {
//        "player": {
//        "currentPlatformId": "KR",
//        "summonerName": "위폭이야",
//        "matchHistoryUri": "/v1/stats/player_history/KR/206029755",
//        "platformId": "KR",
//        "currentAccountId": 206029755,
//        "profileIcon": 23,
//        "summonerId": 35522125,
//        "accountId": 206029755
//        },
//        "participantId": 2
//        },
//        {
//        "player": {
//        "currentPlatformId": "KR",
//        "summonerName": "북한민주화",
//        "matchHistoryUri": "/v1/stats/player_history/KR/1994615",
//        "platformId": "KR",
//        "currentAccountId": 1994615,
//        "profileIcon": 3414,
//        "summonerId": 2519575,
//        "accountId": 1994615
//        },
//        "participantId": 3
//        },
//        {
//        "player": {
//        "currentPlatformId": "KR",
//        "summonerName": "카레는마이떵",
//        "matchHistoryUri": "/v1/stats/player_history/kr/210462723",
//        "platformId": "kr",
//        "currentAccountId": 210462723,
//        "profileIcon": 6,
//        "summonerId": 69060150,
//        "accountId": 210462723
//        },
//        "participantId": 4
//        },
//        {
//        "player": {
//        "currentPlatformId": "KR",
//        "summonerName": "HOPEBE",
//        "matchHistoryUri": "/v1/stats/player_history/KR/7155558",
//        "platformId": "KR",
//        "currentAccountId": 7155558,
//        "profileIcon": 662,
//        "summonerId": 10260185,
//        "accountId": 7155558
//        },
//        "participantId": 5
//        },
//        {
//        "player": {
//        "currentPlatformId": "KR",
//        "summonerName": "뽑기장인임서폿",
//        "matchHistoryUri": "/v1/stats/player_history/KR/202830717",
//        "platformId": "KR",
//        "currentAccountId": 202830717,
//        "profileIcon": 3196,
//        "summonerId": 21610133,
//        "accountId": 202830717
//        },
//        "participantId": 6
//        },
//        {
//        "player": {
//        "currentPlatformId": "KR",
//        "summonerName": "6PM내고향",
//        "matchHistoryUri": "/v1/stats/player_history/KR/1705482",
//        "platformId": "KR",
//        "currentAccountId": 1705482,
//        "profileIcon": 734,
//        "summonerId": 2271082,
//        "accountId": 1705482
//        },
//        "participantId": 7
//        },
//        {
//        "player": {
//        "currentPlatformId": "KR",
//        "summonerName": "겁나빠른꼬부기",
//        "matchHistoryUri": "/v1/stats/player_history/KR/2897488",
//        "platformId": "KR",
//        "currentAccountId": 2897488,
//        "profileIcon": 3157,
//        "summonerId": 3710925,
//        "accountId": 2897488
//        },
//        "participantId": 8
//        },
//        {
//        "player": {
//        "currentPlatformId": "KR",
//        "summonerName": "궁쿨12초",
//        "matchHistoryUri": "/v1/stats/player_history/KR/6046625",
//        "platformId": "KR",
//        "currentAccountId": 6046625,
//        "profileIcon": 2090,
//        "summonerId": 12151758,
//        "accountId": 6046625
//        },
//        "participantId": 9
//        },
//        {
//        "player": {
//        "currentPlatformId": "KR",
//        "summonerName": "Kiss It better",
//        "matchHistoryUri": "/v1/stats/player_history/KR/201401564",
//        "platformId": "KR",
//        "currentAccountId": 201401564,
//        "profileIcon": 1592,
//        "summonerId": 17701507,
//        "accountId": 201401564
//        },
//        "participantId": 10
//        }
