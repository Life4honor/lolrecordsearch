package com.lolsearch.lolrecordsearch.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@Table(name = "users_roles")
public class UsersRoles {

    @ManyToOne
    private Users users;

    public void setUsers(Users users){
        this.users = users;
        if(!users.getUsersRolesList().contains(this)){
            users.getUsersRolesList().add(this);
        }
    }

    @ManyToOne
    public Roles roles;

    public void setRoles(Roles roles){
        this.roles = roles;
        if(!roles.getUsersRolesList().contains(this)){
            roles.getUsersRolesList().add(this);
        }
    }

}
