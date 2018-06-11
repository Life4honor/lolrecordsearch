package com.lolsearch.lolrecordsearch.domain.elasticsearch;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.io.Serializable;

@Getter @Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Document(indexName = "record", type = "summoner")
public class SummonerElastic implements Serializable {

    private String name;

    @Id
    private long accountId;

}
