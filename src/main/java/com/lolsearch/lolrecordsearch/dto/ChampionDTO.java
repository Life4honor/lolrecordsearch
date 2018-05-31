package com.lolsearch.lolrecordsearch.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@EqualsAndHashCode
@ToString
public class ChampionDTO {

    private int id;

    private String key;

    private String title;

    private String name;

    private ImageDTO image;

}
