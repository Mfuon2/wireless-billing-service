package com.softel.mpesa.controller

//import java.util.List
import kotlin.collections.List
import java.util.stream.Collectors

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.http.MediaType
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import io.swagger.v3.oas.annotations.Parameter

// import com.softel.mpesa.dto.FileInfo
// import com.softel.mpesa.dto.FileResponseMessage
// import com.softel.mpesa.service.files.IFileStorage
import com.google.gson.JsonObject;
import com.softel.mpesa.feign.SmsApi
import com.softel.mpesa.dto.AtSms

@RestController
@CrossOrigin(origins = ["http://localhost","http://localhost:4200","http://127.0.0.1:4200","http://127.0.0.1", "http://68.183.217.137","http://68.183.217.137:4200" ])
@RequestMapping("/sms")
@Tag(name = "SMS", description = "File upload and download api")
public class SmsController {

  @Autowired
  lateinit var smsClient: SmsApi

  @PostMapping(value = ["/send"], consumes = ["application/x-www-form-urlencoded"], produces = ["application/json"]
  )
  fun sendSms(@RequestBody smsRequest: AtSms
  ): ResponseEntity<String> {

    var atResponse: String = ""

    try {
      smsClient.postSms(smsRequest)
      return ResponseEntity.status(HttpStatus.OK).body(atResponse)
      } 
    catch (e: Exception) {
      atResponse = "Could not send the sms: " + e.message
      return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(atResponse)
      }
      
    }

}