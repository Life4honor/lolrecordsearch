package com.lolsearch.lolrecordsearch.dto;

import com.lolsearch.lolrecordsearch.domain.jpa.Champion;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

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

    private String strDatetime;

    public void setStrDatetime(Long timestamp){
        Date date = new Date(timestamp);
        LocalDateTime localDateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        String localdate = localDateTime.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 hh:mm"));
        this.strDatetime = localdate;
    }
}
