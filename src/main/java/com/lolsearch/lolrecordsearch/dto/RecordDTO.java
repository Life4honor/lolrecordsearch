package com.lolsearch.lolrecordsearch.dto;

import com.lolsearch.lolrecordsearch.domain.jpa.Participant;
import com.lolsearch.lolrecordsearch.domain.jpa.ParticipantIdentity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@EqualsAndHashCode
@ToString
public class RecordDTO {

    private ParticipantIdentity participantIdentity;

    private Participant participant;
}
