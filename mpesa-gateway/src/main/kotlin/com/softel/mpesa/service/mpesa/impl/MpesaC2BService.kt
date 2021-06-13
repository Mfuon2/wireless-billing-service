package com.softel.mpesa.service.mpesa.impl


import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort

import com.softel.mpesa.config.WebClientConfig
import com.softel.mpesa.config.getJsonObject
import com.softel.mpesa.config.gson
import com.softel.mpesa.dto.MpesaB2CRequestDto
import com.softel.mpesa.dto.WalletDto
import com.softel.mpesa.enums.EnvironmentProfile
import com.softel.mpesa.enums.MpesaCommandID
import com.softel.mpesa.enums.PaymentStatusEnum
import com.softel.mpesa.enums.ServiceRequestStatusEnum
import com.softel.mpesa.enums.ServiceTypeEnum
import com.softel.mpesa.enums.SubscriptionPlan
import com.softel.mpesa.enums.StatementTag
import com.softel.mpesa.enums.WithdrawalType
import com.softel.mpesa.enums.MpesaCallbackEnum
import com.softel.mpesa.entity.Wallet
import com.softel.mpesa.entity.ClientAccount
import com.softel.mpesa.entity.ServicePackage

import java.util.regex.Pattern
import com.softel.mpesa.entity.mpesa.MpesaC2BCallback
import com.softel.mpesa.entity.mpesa.MpesaB2C

import com.softel.mpesa.remote.mpesa.MpesaB2CRequest
import com.softel.mpesa.remote.mpesa.MpesaB2CResponse
import com.softel.mpesa.remote.mpesa.MpesaB2CResult
import com.softel.mpesa.remote.mpesa.PaybillCallback
import com.softel.mpesa.remote.mpesa.MpesaC2BConfirmationResponse
import com.softel.mpesa.remote.mpesa.MpesaC2BValidationResponse

import com.softel.mpesa.repository.MpesaB2CRepository
import com.softel.mpesa.repository.WalletRepository
import com.softel.mpesa.repository.ClientAccountRepository
import com.softel.mpesa.repository.C2BCallbackRepository
import com.softel.mpesa.repository.ServicePackageRepository
import com.softel.mpesa.repository.VoucherUploadRepository

import com.softel.mpesa.service.common.ISms
import com.softel.mpesa.service.common.IPropertyService
import com.softel.mpesa.service.common.IWalletService
import com.softel.mpesa.service.common.IClientAccountService
import com.softel.mpesa.service.mpesa.ICacheService
// import com.softel.mpesa.service.mpesa.IMpesaB2CService
import com.softel.mpesa.service.mpesa.IMpesaC2BService
import com.softel.mpesa.feign.SmsClient

import com.softel.mpesa.util.Helper
import com.softel.mpesa.util.MpesaEncryption
import com.softel.mpesa.util.Result
import com.softel.mpesa.util.ResultFactory
import com.softel.mpesa.util.phoneToLong

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Async
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.reactive.function.client.WebClient
import com.github.dozermapper.core.Mapper
import reactor.core.publisher.Mono
import java.time.LocalDateTime
import com.softel.mpesa.util.DateFormatter
@Service
class MpesaC2BService: IMpesaC2BService {
    val logger: Logger = LoggerFactory.getLogger(MpesaC2BService::class.java)

    @Autowired
    lateinit var clientAccountRepository: ClientAccountRepository

    @Autowired
    lateinit var callbackRepo: C2BCallbackRepository
        
    @Autowired
    lateinit var walletRepository: WalletRepository

    @Autowired
    lateinit var servicePackageRepo: ServicePackageRepository

    @Autowired
    lateinit var tempVoucherRepo: VoucherUploadRepository

    @Autowired
    lateinit var clientAccountService: IClientAccountService

    @Autowired
    lateinit var walletService: IWalletService

    // @Autowired
    // lateinit var smsClient: SmsClient

    @Autowired
    lateinit var smsService: ISms
    

    @Autowired
    lateinit var mapper: Mapper

    @Value("\${softel.api.base-url}")
    lateinit var softelBaseUrl: String

    @Value("\${mpesa.base-url}")
    lateinit var mpesaBaseUrl: String

    @Value("\${mpesa.b2c.queue-timeout-url}")
    lateinit var queueTimeoutUrl: String

    @Value("\${mpesa.b2c.request-url}")
    lateinit var requestUrl: String

    @Value("\${mpesa.b2c.result-url}")
    lateinit var resultUrl: String

    @Value("\${spring.profiles.active:dev}")
    lateinit var activeProfile: String

    @Value("\${mpesa.b2c.security-credential}")
    lateinit var securityCredential: String

    @Value("\${mpesa.b2c.msisdn}")
    lateinit var defaultPartyB: String

    override fun findAllPaged(type: MpesaCallbackEnum, pageable: Pageable): Page<MpesaC2BCallback?>{
        return callbackRepo.findAllPagedCallback(type, pageable);
        }

    override fun validatePaybillPayment(paybillCallback: String): MpesaC2BValidationResponse {
        logCallback(paybillCallback, MpesaCallbackEnum.C2B_VALIDATION)

        val validationResponse = MpesaC2BValidationResponse(
            resultCode = 0,
            resultDesc = "Validated"
            )

        return validationResponse
        }

    override fun confirmPaybillPayment(paybillCallback: String): MpesaC2BConfirmationResponse {

        logCallback(paybillCallback, MpesaCallbackEnum.C2B_CONFIRMATION)

        processVoucher(paybillCallback)

        val confirmResponse = MpesaC2BConfirmationResponse(
            paymentConfirmationResult = "Success"
            )

        return confirmResponse
        }

    @Async
    fun processVoucher(paybillCallback: String) {
        
        val callback: PaybillCallback  = gson.fromJson(paybillCallback, PaybillCallback::class.java)

        var msg = "Welcome to vuka wireless"

        // if(callback.transAmount.toLong() == 1L){
        //     msg = "This is a test"
        //     hashMap.put("to",callback.msisdn)
        //     hashMap.put("message",msg)
        //     hashMap.put("username","VUKA")  

        //     smsClient.postSms(hashMap)  

        //     return
        //     }

        //preparation locate the wallet
        val client    = clientAccountService.findOrCreateClientAccount(
            msisdn = callback.msisdn,
            accountName = callback.firstName + " " + callback.middleName + " " + callback.lastName,
            shortCode = callback.businessShortCode,
            accountNumber = "VUKA", //needs to be autogenerated
            emailAddress = "test@example.com",
            serviceType = ServiceTypeEnum.valueOf("PRE_PAID")
            )
        
        val wallet = walletService.findOrCreateWallet(
            clientAccount   = client,
            serviceType = ServiceTypeEnum.valueOf("PRE_PAID"),
            balance         = 0.0
            )

        //1. get amount
        val delim = "\\."
        val arr = Pattern.compile(delim).split(callback.transAmount)

        val amount = arr[0].toLong()
        logger.info("amount = " + amount)
        //2. search for service matching that amount
        val product = servicePackageRepo.findByPrice(amount)
        if(product == null){

            val nearestProduct = servicePackageRepo.findNearestPackage(amount)
            if(nearestProduct == null){
                msg = "There is no product for that amount (" + amount + "). Payment sent to your Vuka eWallet"

                //wallet.balance = wallet.balance.plus(amount.toDouble())
                wallet.balance = wallet.balance + amount
                walletRepository.save(wallet)

                }
            else{
                val voucher = tempVoucherRepo.findOneUnclaimedTempVoucherByPlan(nearestProduct.code)
                if(voucher == null){
                    msg = "There is currently no voucher available for " + nearestProduct.name + ". Payment sent to your Vuka eWallet. The code will be sent to you after a short while"
                    
                    //send an alert for generation of new vouchers

                    // wallet.balance = wallet.balance.plus(amount.toDouble())
                    wallet.balance = wallet.balance + amount
                    walletRepository.save(wallet)
                    }
                else{
                    val balance = amount - nearestProduct.price
                    msg = voucher.plan + " purchase confirmed. Your voucher code is " + voucher.voucherId + ". It will expire on " + voucher.expiryTime + ". Extra payment of (" + balance + ") has been sent to your Vuka eWallet"
                    voucher.claimedTime = LocalDateTime.now().toString()
                    tempVoucherRepo.save(voucher)

                    // wallet.balance = wallet.balance.plus(balance.toDouble())
                    wallet.balance = wallet.balance + balance
                    walletRepository.save(wallet)
                    }
                }
            }
        else{
            //3. get voucher 
            val voucher = tempVoucherRepo.findOneUnclaimedTempVoucherByPlan(product.code)

            if(voucher == null){
                msg = "There is currently no voucher available for " + product.name + ". Payment sent to your Vuka eWallet. The code will be sent to you after a short while"
                // wallet.balance = wallet.balance.plus(amount.toDouble())
                wallet.balance = wallet.balance + amount
                walletRepository.save(wallet)
                }
            else{
                msg = voucher.plan + " purchase confirmed. Your voucher code is " + voucher.voucherId + ". It will expire on " + voucher.expiryTime 
                voucher.claimedTime = LocalDateTime.now().toString()
                tempVoucherRepo.save(voucher)
                }
            
            }
       
        val hashMap:HashMap<String,String> = HashMap<String,String>() //define empty hashmap  
        hashMap.put("to",callback.msisdn)
        hashMap.put("message",msg)
        smsService.sendAnySms(hashMap)
        }

    @Async
    fun logCallback(paybillCallback: String, type: MpesaCallbackEnum) {

                val result              = gson.fromJson(paybillCallback, PaybillCallback::class.java)
                logger.info("###result json = {}", result)

                val businessShortCode   = result.businessShortCode
                val msisdn              = result.msisdn
                val firstName            = result.firstName
                val middleName           = result.middleName
                val lastName             = result.lastName
                val transTime            = LocalDateTime.parse(result.transTime,DateFormatter.mpesaDateTimeFormatter())

                val client    = clientAccountService.findOrCreateClientAccount(
                        msisdn = msisdn,
                        accountName = firstName + " " + middleName + " " + lastName,
                        shortCode = businessShortCode,
                        accountNumber = "VUKA", //needs to be autogenerated
                        emailAddress = "test@example.com",
                        serviceType = ServiceTypeEnum.valueOf("PRE_PAID")
                    )
                
                val wallet = walletService.findOrCreateWallet(
                    clientAccount   = client,
                    serviceType = ServiceTypeEnum.valueOf("PRE_PAID"),
                    balance         = 0.0
                )

                var callbackObject = mapper.map(result, MpesaC2BCallback::class.java)

                callbackObject.wallet = wallet
                callbackObject.callbackType = type
                callbackObject.transTime = transTime.format(DateFormatter.simpleDateTimeFormatter()).toString()
                callbackObject.createdAt = LocalDateTime.now()
                callbackObject.updatedAt = LocalDateTime.now()

                callbackRepo.save(callbackObject)

                }
 
    
        override fun getPaybillBalance(): Double{

            val cb: MpesaC2BCallback? = callbackRepo.findFirstByCallbackTypeOrderByIdDesc(MpesaCallbackEnum.C2B_CONFIRMATION)

            val b = cb?.orgAccountBalance

            return if(!b.isNullOrEmpty()) 
                b.toDouble()
            else  
                0.0
            }
            

}