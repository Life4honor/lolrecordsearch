package com.lolsearch.lolrecordsearch.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "matches")
@Getter
@Setter
public class Match {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private Long id;

    private String win;

    private int kills;

    private int deaths;

    private int assists;

    @ManyToOne
    @JoinColumn(name = "match_references_id")
    private MatchReference matchReference;

    public void setMatchReference(MatchReference matchReference){
        this.matchReference = matchReference;
        if(!matchReference.getMatches().contains(this)){
            matchReference.getMatches().add(this);
        }
    }

    @ManyToOne
    @JoinColumn(name = "summoners_id")
    private Summoner summoner;

    public void setSummoner(Summoner summoner){
        this.summoner = summoner;
        if(!summoner.getMatches().contains(this)){
            summoner.getMatches().add(this);
        }
    }
}
