package com.lolsearch.lolrecordsearch.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class PlayerDTO {

    private String currentPlatformId;

    private String summonerName;

    private String matchHistoryUri;

    private String platformId;

    private long currentAccountId;

    private int profileIcon;

    private long summonerId;

    private long accountId;

}
