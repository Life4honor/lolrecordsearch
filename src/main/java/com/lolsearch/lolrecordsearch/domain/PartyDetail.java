package com.lolsearch.lolrecordsearch.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "party_detail")
public class PartyDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;

    private LocalDateTime matchDate;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "partyDetail")
    private List<Parties> partiesList;

    public void addParties(Parties parties){
        this.partiesList.add(parties);
        if(parties.getPartyDetail() != this){
            parties.setPartyDetail(this);
        }
    }

    @OneToOne
    @JoinColumn(name = "boards_id")
    private Boards boards;
}
