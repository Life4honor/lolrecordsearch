package com.lolsearch.lolrecordsearch.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@Entity
@Table(name = "categories")
public class Category implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Enumerated(value = EnumType.STRING)
    private CategoryName name;
    
    @OneToMany(cascade = CascadeType.ALL)
    private List<Board> boards = new ArrayList<>();
    
    public void addBoard(Board board) {
        if(!this.boards.contains(board)) {
            this.boards.add(board);
        }
        board.setCategory(this);
    }
    
}
