package com.lolsearch.lolrecordsearch.domain.jpa;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter @Setter
@Entity
@Table(name = "party_detail")
public class PartyDetail implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Enumerated(value = EnumType.STRING)
    private PartyPosition position;

    public void setPosition(String position){
        switch (position){
            case "탑":
                this.position = PartyPosition.TOP;
                break;
            case "정글":
                this.position = PartyPosition.JUNGLE;
                break;
            case "미드":
                this.position = PartyPosition.MID;
                break;
            case "원거리 딜러":
                this.position = PartyPosition.AD_CARRY;
                break;
            case "서포터":
                this.position = PartyPosition.SUPPORTER;
                break;
        }
    }
    
    @ManyToOne(cascade = {CascadeType.PERSIST,CascadeType.REFRESH, CascadeType.MERGE, CascadeType.DETACH}, fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id")
    private User user;
    
    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "parties_id")
    private Party party;
    
    public void setUser(User user) {
        if(this.user != null) {
            this.user.getPartyDetails().remove(this);
        }
        this.user = user;
        if(!this.user.getPartyDetails().contains(this)) {
            this.user.getPartyDetails().add(this);
        }
    }
    
    public void setParty(Party party) {
        if(this.party != null) {
            this.party.getPartyDetails().remove(this);
        }
        this.party = party;
        
        if(!this.party.getPartyDetails().contains(this)) {
            this.party.getPartyDetails().add(this);
        }
    }
    
}