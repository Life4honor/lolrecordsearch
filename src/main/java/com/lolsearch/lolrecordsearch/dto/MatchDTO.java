package com.lolsearch.lolrecordsearch.dto;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter @Setter
@EqualsAndHashCode
@ToString
public class MatchDTO {

    private int seasonId;

    private int queueId;

    private long gameId;

    private List<ParticipantIdentityDTO> participantIdentities;

    private String gameVersion;

    private String platformId;

    private String gameMode;

    private int mapId;

    private String gameType;

    private List<TeamStatsDTO> teams;

    private List<ParticipantDTO> participants;

    private long gameDuration;

    private long gameCreation;


}
