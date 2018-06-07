package com.lolsearch.lolrecordsearch.domain.jpa;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "participants")
@Getter @Setter
public class Participant {

//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "participants_id")
    private Long participantId;

//    @OneToOne
//    @JoinColumn(name = "participant_id")
//    private ParticipantIdentity participantIdentity;

    private Long gameId;

    @Lob
    private String stats;
//    private ParticipantStatsDTO stats;

    private int team_id;

    private Integer championId;

}