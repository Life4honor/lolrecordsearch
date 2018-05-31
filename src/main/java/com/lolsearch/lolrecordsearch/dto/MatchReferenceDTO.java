package com.lolsearch.lolrecordsearch.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@EqualsAndHashCode
@ToString
public class MatchReferenceDTO {

    private String lane;

    private long gameId;

    private long champion;

    private String platformId;

    private long timestamp;

    private long queue;

    private String role;

    private long season;

}