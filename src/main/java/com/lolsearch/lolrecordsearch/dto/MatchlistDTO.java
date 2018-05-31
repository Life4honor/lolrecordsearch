package com.lolsearch.lolrecordsearch.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter @Setter
@EqualsAndHashCode
@ToString
public class MatchlistDTO {

    private List<MatchReferenceDTO> matches;

    private int totalGames;

    private int startIndex;

    private int endIndex;

}
