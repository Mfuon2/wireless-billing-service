package com.softel.mpesa.job

import com.softel.mpesa.service.mpesa.IMpesaExpressService
import com.softel.mpesa.enums.SmsStatus
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import com.softel.mpesa.repository.SmsRepository
import org.springframework.transaction.annotation.Transactional

import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.softel.mpesa.service.common.ISms
import com.softel.mpesa.feign.SmsClient
import com.softel.mpesa.entity.Sms

@Component
class SmsWorker {
    val logger: Logger = LoggerFactory.getLogger(SmsWorker::class.java)

    @Autowired
    lateinit var smsRepository: SmsRepository

    @Autowired
    lateinit var smsService: ISms

    @Scheduled(fixedDelay = 1000 * 60 * 1)  //Run every 5 minutes
    @Transactional
    fun resendFailedSms() {

        logger.info("Resending failed/pending SMSs" );

        var hashMap:HashMap<String,String> = HashMap<String,String>() //define empty hashmap  
        val smsStream = smsRepository.findPendingAndFailedSms()
        smsStream.forEach{sms -> 
                smsService.resendSms(sms)
                }       
        }



    // @Transactional
    // override fun processPendingTransactions() {
    //     val pendingTransactions = mpesaExpressRepository.findByPaymentStatus(PaymentStatusEnum.PENDING)
    //     pendingTransactions.forEach { transaction: MpesaExpress? ->
    //         transaction?.let {
    //             val currentDateTime     = LocalDateTime.now()
    //             val transactionDateTime = transaction.createdAt
    //             if (transactionDateTime.until(currentDateTime, ChronoUnit.MINUTES) > 1)
    //                 queryTransactionStatus(checkoutRequestId = transaction.checkoutRequestId)
    //         }
    //     }
    // }



}