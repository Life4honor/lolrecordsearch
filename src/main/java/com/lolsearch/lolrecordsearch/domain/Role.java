package com.lolsearch.lolrecordsearch.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@Entity
@Table(name = "roles")
public class Role implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    
    @ManyToMany(mappedBy = "roles")
    private List<User> users = new ArrayList<>();
    
    public void addUser(User user) {
        if(!this.users.contains(user)) {
            this.users.add(user);
        }
        
        if(!user.getRoles().contains(this)) {
            user.getRoles().add(this);
        }
    }
    
}
