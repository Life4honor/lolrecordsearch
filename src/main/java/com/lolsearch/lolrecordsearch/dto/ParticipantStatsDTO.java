package com.lolsearch.lolrecordsearch.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@EqualsAndHashCode
@ToString
public class ParticipantStatsDTO {

    private long physicalDamageDealt;

    private int totalPlayerScore;

    private int deaths;

    private Boolean win;

    private int kills;

    private int assists;

}
