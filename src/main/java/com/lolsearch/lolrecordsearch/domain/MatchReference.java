package com.lolsearch.lolrecordsearch.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "match_references")
@Getter @Setter
public class MatchReference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private Long id;

    private String lane;

    private Long gameId;

    private Long championId;

    private String platformId;

    private Long timestamp;

    private Long queue;

    private String role;

    private Long season;

    @OneToMany(mappedBy = "matchReference", fetch = FetchType.LAZY)
    private List<Match> matches = new ArrayList<>();

    public void addMatch(Match match){
        this.matches.add(match);
        match.setMatchReference(this);
    }

}
