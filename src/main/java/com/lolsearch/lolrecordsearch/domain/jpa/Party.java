package com.lolsearch.lolrecordsearch.domain.jpa;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@Entity
@Table(name = "party")
public class Party implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(value = EnumType.STRING)
    private PartyType type;
    @Column(name = "match_date")
    private LocalDateTime matchDate;
    
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "boards_id")
    private Board board;
    
    @OneToMany(mappedBy = "party", cascade = CascadeType.ALL)
    private List<PartyDetail> partyDetails = new ArrayList<>();
    
    public void addPartyDetail(PartyDetail partyDetail) {
        if(!this.partyDetails.contains(partyDetail)) {
            this.partyDetails.add(partyDetail);
        }
        partyDetail.setParty(this);
    }
    
}
