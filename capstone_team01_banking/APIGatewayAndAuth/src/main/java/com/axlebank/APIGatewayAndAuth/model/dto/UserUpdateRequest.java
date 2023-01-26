package com.axlebank.APIGatewayAndAuth.model.dto;

public class UserUpdateRequest {

            private Status status;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public UserUpdateRequest(Status status) {
        this.status = status;
    }
}
