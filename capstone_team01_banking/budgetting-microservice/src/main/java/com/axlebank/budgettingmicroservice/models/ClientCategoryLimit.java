package com.axlebank.budgettingmicroservice.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import javax.persistence.*;

@Entity

@Getter
@Setter
@IdClass(ClientCustomInfoId.class)
public class ClientCategoryLimit {

//    @EmbeddedId
//    private ClientCustomInfoId id;

    @Id
    private String category;
    @Id
    private int clientId;

    private double percentageLimit;

    public ClientCategoryLimit(String category, int clientId, double percentageLimit) {
        this.category = category;
        this.clientId = clientId;
        this.percentageLimit = percentageLimit;
    }

    public ClientCategoryLimit() {}


}
