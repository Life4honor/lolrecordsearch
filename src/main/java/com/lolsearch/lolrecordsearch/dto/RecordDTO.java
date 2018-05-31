package com.lolsearch.lolrecordsearch.dto;

import com.lolsearch.lolrecordsearch.domain.Participant;
import com.lolsearch.lolrecordsearch.domain.ParticipantIdentity;
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
