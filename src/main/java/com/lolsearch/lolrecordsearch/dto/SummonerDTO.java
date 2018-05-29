package com.lolsearch.lolrecordsearch.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter @Setter
@EqualsAndHashCode
@ToString
public class SummonerDTO implements Serializable {

    private int profileIconId;

    private String name;

    private long summonerLevel;

    private long revisionDate;

    private long id;

    private long accountId;

}
