package com.lolsearch.lolrecordsearch.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "parties")
public class Parties {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String position;

    @ManyToOne
    @JoinColumn(name = "users_id")
    private Users users;

    public void setUsers(Users users){
        this.users = users;
        if(!users.getPartiesList().contains(this)){
            users.getPartiesList().add(this);
        }
    }

    @ManyToOne
    @JoinColumn(name = "party_detail_id")
    private PartyDetail partyDetail;

    public void setPartyDetail(PartyDetail partyDetail){
        this.partyDetail = partyDetail;
        if(!partyDetail.getPartiesList().contains(this)){
            partyDetail.getPartiesList().add(this);
        }
    }

}
