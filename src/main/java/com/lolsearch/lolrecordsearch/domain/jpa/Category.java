package com.lolsearch.lolrecordsearch.domain.jpa;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter @Setter
@Entity
@Table(name = "categories")
public class Category implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Enumerated(value = EnumType.STRING)
    private CategoryName name;
    
}
