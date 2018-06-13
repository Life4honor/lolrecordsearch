package com.lolsearch.lolrecordsearch.domain.jpa;

import java.util.ArrayList;
import java.util.List;

public enum PartyType {
    SOLO("솔로랭크"), NORMAL("일반랭크");

    private String queueType;

    PartyType(String queueType){
        this.queueType = queueType;
    }

    public String getQueueType(){
        return queueType;
    }

    public static List<PartyType> getList(){
        List<PartyType> partyTypeList = new ArrayList<>();
        for (PartyType partyType : PartyType.values()) {
            partyTypeList.add(partyType);
        }
        return partyTypeList;
    }
}
