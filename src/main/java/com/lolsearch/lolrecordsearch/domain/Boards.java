package com.lolsearch.lolrecordsearch.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "boards")
public class Boards {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String wirter;

    private String content;

    private LocalDateTime regDate;

    @OneToOne(mappedBy = "boards")
    private PartyDetail partyDetail;

    @ManyToOne
    @JoinColumn(name = "categories_id")
    private Categories categories;

    public void setCategories(Categories categories){
        this.categories = categories;
        if(!categories.getBoardsList().contains(this)){
            categories.getBoardsList().add(this);
        }
    }
}
