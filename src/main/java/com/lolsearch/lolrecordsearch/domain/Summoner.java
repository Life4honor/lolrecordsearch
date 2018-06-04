package com.lolsearch.lolrecordsearch.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "summoners")
@Getter @Setter
public class Summoner {

    @Id
    private Long id;

    private Integer profileIconId;

    private String name;

    private Long summonerLevel;

    private Long revisionDate;

    private Long accountId;

    @OneToMany(mappedBy = "summoner", fetch = FetchType.LAZY ,cascade = CascadeType.ALL)
    private List<Match> matches = new ArrayList<>();

    public void addMatch(Match match){
        this.matches.add(match);
        match.setSummoner(this);
    }

}
