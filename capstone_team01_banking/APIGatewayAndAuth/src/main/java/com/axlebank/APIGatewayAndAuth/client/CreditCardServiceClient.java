package com.axlebank.APIGatewayAndAuth.client;

import com.axlebank.APIGatewayAndAuth.model.dto.Client;
import com.axlebank.APIGatewayAndAuth.model.dto.CreditCardCompany;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient( name="company-service", url = "http://localhost:9003/api/v1/")
public interface CreditCardServiceClient {

    @PostMapping(value = "company", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    CreditCardCompany register(@RequestBody CreditCardCompany creditCardCompany);

}
