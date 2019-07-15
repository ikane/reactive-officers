package com.oreilly.reactiveofficers.entities;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data @Builder
@Document
public class Officer {

    @Id
    private String id;
    private Rank rank;
    private String first;
    private String last;
}
