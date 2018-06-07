package com.lolsearch.lolrecordsearch.domain.jpa;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "champions")
@Getter @Setter
public class Champion {

    @Id
    private Long id;

    private String name;

}
