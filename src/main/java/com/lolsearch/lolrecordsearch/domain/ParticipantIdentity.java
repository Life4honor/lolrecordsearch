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
    private Long participantId;

    private Long gameId;

    private String player;
//    private PlayerDTO player;

}
