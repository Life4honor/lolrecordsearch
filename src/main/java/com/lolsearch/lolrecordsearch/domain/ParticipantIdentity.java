package com.lolsearch.lolrecordsearch.domain;

import com.lolsearch.lolrecordsearch.dto.PlayerDTO;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "participants_identities")
@Getter @Setter
public class ParticipantIdentity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long participantId;
//    @Id
//    private Long id;

//    @OneToOne(mappedBy = "participantIdentity")
//    private Participant participant;

    private Long gameId;

    @Lob
    private String player;
//    private PlayerDTO player;
}
