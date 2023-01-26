package com.axlebank.APIGatewayAndAuth.client;

import com.axlebank.APIGatewayAndAuth.model.dto.Client;
import com.axlebank.APIGatewayAndAuth.model.dto.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@FeignClient( name="client-service", url = "http://localhost:9001/api/v1/")
public interface ClientServiceClient {

    @PostMapping(value = "client", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    Client register(@RequestBody Client client);
}
