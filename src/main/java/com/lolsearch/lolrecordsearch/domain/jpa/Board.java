package com.lolsearch.lolrecordsearch.domain.jpa;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter @Setter
@Entity
@Table(name = "boards")
public class Board implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String writer;

    private String title;

    private String content;
    @Column(name = "reg_date")
    private LocalDateTime regDate;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "categories_id")
    private Category category;

//    @Enumerated(value = EnumType.STRING)
//    private PartyType partyType;
}
