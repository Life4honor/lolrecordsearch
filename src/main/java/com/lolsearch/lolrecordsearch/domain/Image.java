package com.lolsearch.lolrecordsearch.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Getter @Setter
@Entity
@Table(name = "images")
public class Image implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String path;
    @Enumerated(value = EnumType.ORDINAL)
    private ImageType type;
    @Column(name = "mime_type")
    private String mimeType;
    private int size;
    
    
}
