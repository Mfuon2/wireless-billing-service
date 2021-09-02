package com.softel.mpesa.feign;

import java.util.Map;

import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.http.ResponseEntity;

@FeignClient(name = "cambiumFeign", 
            url = "https://us-e1-s20-ka3rjf6s09.cloud.cambiumnetworks.com/api/v2",
            configuration = CambiumFeign.Configuration.class
            )
public interface CambiumFeign {

    @PostMapping(value = "/access/token", consumes = "application/x-www-form-urlencoded")
    ResponseEntity<String> getAccessToken(
            @RequestHeader("Authorization") String header,
            @RequestBody Map<String, ?> form
            );

    class Configuration {
        @Bean
        Encoder feignFormEncoder(ObjectFactory<HttpMessageConverters> converters) {
            return new SpringFormEncoder(new SpringEncoder(converters));
            }
        }
}