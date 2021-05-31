package com.softel.mpesa.service.common.impl

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.softel.mpesa.enums.AccountTransactionType
import com.softel.mpesa.enums.ServiceTypeEnum
import com.softel.mpesa.enums.SmsStatus

import com.softel.mpesa.entity.VoucherUpload
import com.softel.mpesa.dto.PackageDto

import com.softel.mpesa.repository.SmsRepository

import com.softel.mpesa.service.common.ISms
import com.softel.mpesa.feign.SmsClient

import com.softel.mpesa.util.Result
import com.softel.mpesa.util.ResultFactory

import com.softel.mpesa.entity.Sms
import com.softel.mpesa.entity.ClientAccount

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import com.github.dozermapper.core.Mapper

import java.time.LocalDateTime

@Service
class SmsService: ISms {

    @Autowired
    lateinit var smsRepository: SmsRepository

    @Autowired
    lateinit var smsClient: SmsClient
   
    override fun findAllPaged(pageable: Pageable): Page<Sms?>{
        return smsRepository.findAll(pageable);
        }
        

    override fun getSms(id: Long): Result<Sms?> {
        val sms = smsRepository.findById(id)
        return if(sms.isPresent())
            ResultFactory.getSuccessResult(msg = "Request successfully processed", data = sms.get())
        else
            ResultFactory.getFailResult(msg = "No sms found with the given id")
        }

    override fun sendWelcomeSms(clientAccount: ClientAccount){
        val txt = "Welcome to Vuka Wireless. Your account number is " + clientAccount.accountNumber

        val hashMap:HashMap<String,String> = HashMap<String,String>() //define empty hashmap  
        hashMap.put("to",clientAccount.msisdn)
        hashMap.put("message",txt)
        hashMap.put("username","VUKA")    
        //hashMap.put("sender","VUKA")    

        val resp: ResponseEntity<String> = smsClient.postSms(hashMap)

        if(resp.statusCode == HttpStatus.OK || resp.statusCode == HttpStatus.CREATED){ //or  getStatusCodeValue , 200 , etc
            hashMap.put("attemptTime", LocalDateTime.now().toString())
            hashMap.put("status", "SENT")
            }
        else{
            hashMap.put("attemptTime", LocalDateTime.now().toString())
            hashMap.put("status", "PENDING")
            hashMap.put("apiResponse", resp.statusCode.toString())
            }
        persistSms(hashMap)
        }

        
    override fun sendAnySms(smsMap: HashMap<String,String>){

        val resp: ResponseEntity<String> = smsClient.postSms(smsMap)

        if(resp.statusCode == HttpStatus.OK || resp.statusCode == HttpStatus.CREATED){ //or  getStatusCodeValue , 200 , etc
            smsMap.put("attemptedTime", LocalDateTime.now().toString())
            smsMap.put("status", "SENT")
            }
        else{
            smsMap.put("attemptedTime", LocalDateTime.now().toString())
            smsMap.put("status", "PENDING")
            smsMap.put("apiResponse", resp.statusCode.toString())
            }
        persistSms(smsMap)
        }

    override fun persistSms(smsMap: Map<String,String>): Sms{
        return smsRepository.save(
            Sms(
                msisdn = smsMap.get("to")?:"",
                message = smsMap.get("message")?:"",
                userName = smsMap.get("userName")?:"",
                senderId = smsMap.get("senderId"),
                attemptCount = 1,
                attemptedTime = smsMap.get("attemptedTime"),
                status = SmsStatus.valueOf(smsMap.get("status")?:""),
                apiResponse = smsMap.get("apiResponse")
                )
            )
        }
    
    
    override fun resendById(id: Long): Result<String>{
        var oSms = smsRepository.findById(id)

        if(oSms.isPresent()){
            var sms = oSms.get()

            val hashMap:HashMap<String,String> = HashMap<String,String>() //define empty hashmap  
            hashMap.put("to",sms.msisdn)
            hashMap.put("message",sms.message)
            hashMap.put("username","VUKA")  
    
            val resp: ResponseEntity<String> = smsClient.postSms(hashMap)
    
            sms.attemptCount = sms.attemptCount + 1
            sms.attemptedTime = LocalDateTime.now().toString()
    
            if(resp.statusCode == HttpStatus.OK || resp.statusCode == HttpStatus.CREATED){ //or  getStatusCodeValue , 200 , etc
                sms.status = SmsStatus.SENT
                smsRepository.save(sms)
                return ResultFactory.getSuccessResult(msg = "Sms resent successfuly", data = sms.msisdn)
                }
            else{
                sms.status = SmsStatus.FAILED
                sms.apiResponse = resp.statusCode.toString()
                smsRepository.save(sms)
                return ResultFactory.getFailResult(msg = "Resend failed: code " + resp.statusCode.toString())
                }
            }
        else{
            return ResultFactory.getFailResult(msg = "Sms with that id not found : " + id )
            }

        }
}