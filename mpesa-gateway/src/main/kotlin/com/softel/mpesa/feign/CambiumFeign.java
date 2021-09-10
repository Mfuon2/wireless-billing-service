package com.softel.mpesa.feign;

import java.util.Map;

import feign.codec.Encoder;
import feign.form.spring.SpringFormEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.http.ResponseEntity;

@FeignClient(name = "cambiumFeign", 
            url = "https://us-e1-s20-ka3rjf6s09.cloud.cambiumnetworks.com/api/v2",
            configuration = CambiumFeign.Configuration.class
            )
public interface CambiumFeign {

    @PostMapping(value = "/access/token", consumes = "application/x-www-form-urlencoded")
    String getAccessToken(@RequestHeader("Authorization") String header,@RequestBody Map<String, ?> form );

    @GetMapping(value = "/devices", consumes = "application/json", produces = "application/json")
    String getDevices(@RequestHeader("Authorization") String header);

    @GetMapping(value = "/portals", consumes = "application/json", produces = "application/json")
    String getAllPortals(@RequestHeader("Authorization") String header);

    @GetMapping(value = "/portals/{portal_id}", consumes = "application/json", produces = "application/json")
    String getPortal(@RequestHeader("Authorization") String header, @PathVariable String portal_id);

    @GetMapping(value = "/portals/{portal_id}/vouchers/{voucher_plan}", consumes = "application/json", produces = "application/json")
    String getVouchers(@RequestHeader("Authorization") String header, @PathVariable String portal_id, @PathVariable String voucher_plan);

    @PostMapping(value = "/portals/{portal_id}/vouchers/{voucher_plan}/generate", consumes = "application/json", produces = "application/json")
    String generateVouchers(@RequestHeader("Authorization") String header, 
                            @PathVariable String portal_id, 
                            @PathVariable String voucher_plan, 
                            @RequestBody String dto);



    class Configuration {
        @Bean
        Encoder feignFormEncoder(ObjectFactory<HttpMessageConverters> converters) {
            return new SpringFormEncoder(new SpringEncoder(converters));
            }
        }
}