// /**
//  * Fetches policy details from actisure API
//  */
// package com.softel.mpesa.feign;

// import com.google.gson.Gson;
// import com.google.gson.JsonElement;
// import com.google.gson.JsonObject;
// import com.google.gson.JsonParser;

// import org.springframework.cloud.openfeign.SpringQueryMap;
// import org.springframework.cloud.openfeign.FeignClient;
// import org.springframework.stereotype.Component;
// import org.springframework.web.bind.annotation.PathVariable;
// import org.springframework.web.bind.annotation.RequestParam;

// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestMethod;
// import org.springframework.http.MediaType;
// import org.springframework.web.bind.annotation.RequestBody;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PostMapping;

// import feign.Client
// import feign.Feign
// import feign.Headers
// import feign.Param
// import feign.RequestLine
// import feign.RequestTemplate
// import feign.Response
// import feign.auth.BasicAuthRequestInterceptor;
// import feign.codec.EncodeException
// import feign.codec.Encoder
// // import feign.gson.GsonDecoder;

// import org.springframework.http.ResponseEntity;
// import com.softel.mpesa.config.FeignConfig
// import com.softel.mpesa.dto.AtSms


// @FeignClient(name = "smsapi", 
//             url = "https://softwareelegance.net/api",
//             configuration = [FeignConfig::class])
// interface SmsApi {

//         // @RequestMapping(
//         //         method = [RequestMethod.GET], 
//         //         value = ["/sms"], 
//         //         headers = ["X-Gravitee-Api-Key=68c1f195-4d7d-4476-8813-82760a11189d"]
//         //         )
//         // fun sendSMS(
//         //         @RequestParam to: String,
//         //         @RequestParam message: String,
//         //         @RequestParam sender_id: String
//         //                 ): ResponseEntity<JsonObject>

//         // @RequestLine("POST /sms")
//         // @Headers("X-Gravitee-Api-Key: 68c1f195-4d7d-4476-8813-82760a11189d", "Accept: application/json", "Content-Type: application/x-www-form-urlencoded")
//         // fun postSmsRequestLine(@Param("to") to: String, @Param("message") message: String, @Param("username") username: String): ResponseEntity<String>

//         // @RequestMapping(
//         //         method = [RequestMethod.POST], 
//         //         value = ["/sms"], 
//         //         headers = ["X-Gravitee-Api-Key=68c1f195-4d7d-4476-8813-82760a11189d"]
//         //         )
//         //fun postSms(@RequestBody json: String):ResponseEntity<String>
//         //fun postSms(@Param("to") to: String, @Param("message") message: String, @Param("username") username: String): ResponseEntity<String>


//         // @PostMapping(value = "/echo/post",consumes = {"application/x-www-form-urlencoded"})
//         // TestModel echoPostForm(Map<String, ?> formParams);


//         @PostMapping(
//                 value = ["/sms"], 
//                 consumes = ["application/x-www-form-urlencoded"],
//                 produces = ["application/json"]                
//                 )
//         //@Headers("Content-Type: application/x-www-form-urlencoded")
//         fun postSms(body: AtSms):ResponseEntity<String>
//         //fun postSms(formParams: Map<String, String>):ResponseEntity<String>
//         //fun postSms(@Param("to") to: String, @Param("message") message: String, @Param("username") username: String): ResponseEntity<String>

// }