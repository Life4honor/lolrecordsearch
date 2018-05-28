package com.lolsearch.lolrecordsearch.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter @Setter
@Entity
@Table(name = "persistent_logins")
public class PersistentLogin implements Serializable {

    @Id
    private String series;
    private String username;
    private String token;
    @Column(name = "last_used")
    private LocalDateTime lastUsed;

}
