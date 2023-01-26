package com.axlebank.budgettingmicroservice.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
@Embeddable
@Data
@NoArgsConstructor
public class ClientCustomInfoId implements Serializable {

    private int clientId;

    private String category;

    public ClientCustomInfoId(int clientId, String category) {
        this.clientId = clientId;
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ClientCustomInfoId)) return false;
        ClientCustomInfoId that = (ClientCustomInfoId) o;
        return clientId == that.clientId && category.equals(that.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clientId, category);
    }
}
