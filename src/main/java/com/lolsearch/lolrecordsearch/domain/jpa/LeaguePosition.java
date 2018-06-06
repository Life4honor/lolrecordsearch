package com.lolsearch.lolrecordsearch.domain.jpa;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "league_positions")
@Getter @Setter
public class LeaguePosition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private Long id;

    private String queueType;

    private String rank;

    private int wins;

    private int losses;

    private String leagueId;

    private String leagueName;

    private String playerOrTeamName;

    private String playerOrTeamId;

    private String tier;

    private int leaguePoints;


}
