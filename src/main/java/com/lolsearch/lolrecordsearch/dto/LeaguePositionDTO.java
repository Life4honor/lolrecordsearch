package com.lolsearch.lolrecordsearch.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter @Setter
@EqualsAndHashCode
@ToString
public class LeaguePositionDTO implements Serializable {

    private String rank;

    private int wins;

    private String queueType;

    private int losses;

    private String leagueId;

    private String leagueName;

    private String playerOrTeamName;

    private String playerOrTeamId;

    private String tier;

    private int leaguePoints;

}
