package com.axlebank.APIGatewayAndAuth.client;

import com.axlebank.APIGatewayAndAuth.model.dto.Admin;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient( name="admin-service", url = "http://localhost:9002/api/v1/")
public interface AdminServiceClient {

    @PostMapping(value = "admin", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    Admin register(@RequestBody Admin admin);

}

