package com.lolsearch.lolrecordsearch.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "participants")
@Getter @Setter
public class Participant {

    @Id
    private Long participantId;

    private Long gameId;

    private String stats;
//    private ParticipantStatsDTO stats;

    private Integer teamId;

    private Integer championId;

}
