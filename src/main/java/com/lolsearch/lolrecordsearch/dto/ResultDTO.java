package com.lolsearch.lolrecordsearch.dto;

import com.lolsearch.lolrecordsearch.domain.jpa.Champion;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@EqualsAndHashCode
@ToString
public class ResultDTO {

    private Long gameId;

    private Champion champion;
//    private ChampionDTO champion;

    private String role;

    private String result;

    private String win;

    private int kills;

    private int assists;

    private int deaths;

    private Long timestamp;
}
