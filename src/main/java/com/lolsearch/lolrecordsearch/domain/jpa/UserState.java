package com.lolsearch.lolrecordsearch.domain.jpa;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter @Setter
@Entity
@Table(name = "user_state")
public class UserState implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(unique = true)
    @Enumerated(value = EnumType.STRING)
    private UserStatus name;
}
