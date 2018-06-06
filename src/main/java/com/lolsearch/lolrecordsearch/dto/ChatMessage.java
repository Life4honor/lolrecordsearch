package com.lolsearch.lolrecordsearch.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Transient;

import java.io.Serializable;
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
    
}
