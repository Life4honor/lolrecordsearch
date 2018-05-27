package com.lolsearch.lolrecordsearch.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@Entity
@Table(name = "party_detail")
public class PartyDetail implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(value = EnumType.ORDINAL)
    private PartyType type;
    @Column(name = "match_date")
    private LocalDateTime matchDate;
    
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "boards_id")
    private Board board;
    
    @OneToMany(mappedBy = "partyDetail", cascade = CascadeType.ALL)
    private List<Party> parties = new ArrayList<>();
    
    public void addParty(Party party) {
        if(!this.parties.contains(party)) {
            this.parties.add(party);
        }
        party.setPartyDetail(this);
    }
    
    
}
