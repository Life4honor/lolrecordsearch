package com.lolsearch.lolrecordsearch.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class ParticipantDTO {

    private ParticipantStatsDTO stats;

    private int participantId;

//    private List<RuneDTO> runes;

//    private ParticipantTimelineDTO timeline;

    private TeamStatsDTO teams;

    private int spell2Id;

//    private List<MasteryDTO> masteries;

    private String highestAchievedSeasonTier;

    private int spell1Id;

    private int championId;

}
