package com.lolsearch.lolrecordsearch.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "roles")
public class Roles {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "roles")
    private List<UsersRoles> usersRolesList;

    public void addUserRoles(UsersRoles usersRoles){
        this.usersRolesList.add(usersRoles);
        if(usersRoles.getRoles() != this){
            usersRoles.setRoles(this);
        }
    }
}
