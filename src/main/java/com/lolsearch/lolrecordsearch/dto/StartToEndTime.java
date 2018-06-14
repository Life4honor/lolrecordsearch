package com.lolsearch.lolrecordsearch.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@Getter @Setter
public class StartToEndTime {

    private String startDate;

    private String startTime;

    private String endDate;

    private String endTime;

    private LocalDateTime start;

    private LocalDateTime end;

    public StartToEndTime(String startDate, String startTime, String endDate, String endTime){
        if((!"".equals(startDate)&&!"".equals(startTime)) && (startDate != null && startTime != null)) {
            this.start = LocalDateTime.parse(startDate + " " + startTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        }
        if((!"".equals(endDate)&&!"".equals(endTime)) && (endDate != null && endTime != null)) {
            this.end = LocalDateTime.parse(endDate + " " + endTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        }
    }

    public void timeCheckAndSwitch(){

        if(start != null && end != null) {
            if (start.toEpochSecond(ZoneOffset.MAX) - end.toEpochSecond(ZoneOffset.MAX) > 0) {
                LocalDateTime tempTime = start;
                start = end;
                end = tempTime;

                String tempStr = startDate;
                startDate = endDate;
                endDate = tempStr;

                tempStr = startTime;
                startTime = endTime;
                endTime = tempStr;
            }
        }

    }
}
