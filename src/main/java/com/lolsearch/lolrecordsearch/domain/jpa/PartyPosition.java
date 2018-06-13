package com.lolsearch.lolrecordsearch.domain.jpa;

import java.util.ArrayList;
import java.util.List;

public enum PartyPosition {
    
    TOP("탑"), JUNGLE("정글"), MID("미드"), AD_CARRY("원거리 딜러"), SUPPORTER("서포터");

    private String position;

    PartyPosition(String position){
        this.position = position;
    }

    public String getPosition(){
        return position;
    }

    public static List<PartyPosition> getList(){
        List<PartyPosition> partyPositionList = new ArrayList<>();
        for (PartyPosition partyPosition : PartyPosition.values()) {
            partyPositionList.add(partyPosition);
        }
        return partyPositionList;

    }
}
