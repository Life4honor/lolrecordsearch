package com.lolsearch.lolrecordsearch.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.lolsearch.lolrecordsearch.jackson.LocalDateTimeDeserializer;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Transient;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@ToString
@Getter @Setter
@EqualsAndHashCode
public class ChatMessage implements Serializable {
    
    @Transient
    private Long chatRoomId;
    private Long userId;
    private String nickname;
    private String content;
    private Date regDate;
    
//    public LocalDateTime getRegDate() {
//
//        return this.regDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
//    }
//
//    public void setRegDate(LocalDateTime localDateTime) {
//        this.regDate = Timestamp.valueOf(localDateTime);
//    }
    
}
