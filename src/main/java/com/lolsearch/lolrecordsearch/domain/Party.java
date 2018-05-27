package com.lolsearch.lolrecordsearch.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter @Setter
@Entity
@Table(name = "parties")
public class Party implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Enumerated
    private PartyPosition position;
    
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id")
    private User user;
    
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "party_detail_id")
    private PartyDetail partyDetail;
    
    public void setUser(User user) {
        if(this.user != null) {
            this.user.getParties().remove(this);
        }
        this.user = user;
        if(!this.user.getParties().contains(this)) {
            this.user.getParties().add(this);
        }
    }
    
//    public void setPartyDetail(PartyDetail partyDetail) {
//        if(this.partyDetail != null) {
//            this.partyDetail.get.remove(this);
//        }
//        this.partyDetail = partyDetail;
//        if(!this.partyDetail.getParties().contains(this)) {
//            this.partyDetail.getParties().add(this);
//        }
//    }
    
}