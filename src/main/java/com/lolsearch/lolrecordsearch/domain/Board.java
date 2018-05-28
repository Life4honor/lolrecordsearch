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
@Table(name = "boards")
public class Board implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String writer;
    private String content;
    @Column(name = "reg_date")
    private LocalDateTime regDate;
    
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "categories_id")
    private Category category;
    
    public void setCategory(Category category) {
        if(this.category != null) {
            this.category.getBoards().remove(this);
        }
        this.category = category;
        if(!this.category.getBoards().contains(this)) {
            this.category.getBoards().add(this);
        }
    }
    
}
