package com.softel.mpesa.service.common.impl

import com.softel.mpesa.enums.ServiceTypeEnum
import com.softel.mpesa.service.common.IPropertyService
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

@Service
class PropertyService: IPropertyService {

    //MPESA CREDENTIALS
    @Value("\${mpesa.consumer-key}")
    lateinit var consumerKey: String

    @Value("\${mpesa.consumer-secret}")
    lateinit var consumerSecret: String

    @Value("\${mpesa.short-code}")
    lateinit var shortCode: String

    @Value("\${mpesa.passkey}")
    lateinit var passKey: String

    // B2C credentials
    @Value("\${mpesa.b2c.initiator-name}")
    lateinit var b2CInitiatorName: String

    @Value("\${mpesa.b2c.password}")
    lateinit var b2CPassword: String

    @Value("\${mpesa.b2c.short-code}")
    lateinit var b2CShortCode: String

    // //FREE / MYBIZ 
    // @Value("\${mpesa.free.consumer-key}")
    // lateinit var freeConsumerKey: String

    // @Value("\${mpesa.free.consumer-secret}")
    // lateinit var freeConsumerSecret: String

    // @Value("\${mpesa.free.short-code}")
    // lateinit var freeShortCode: String

    // @Value("\${mpesa.free.passkey}")
    // lateinit var freePassKey: String

    // //BASIC / SOFTEL
    // @Value("\${mpesa.basic.consumer-key}")
    // lateinit var basicConsumerKey: String

    // @Value("\${mpesa.basic.consumer-secret}")
    // lateinit var basicConsumerSecret: String

    // @Value("\${mpesa.basic.short-code}")
    // lateinit var basicShortCode: String

    // @Value("\${mpesa.basic.passkey}")
    // lateinit var basicPassKey: String

    // //CORPORATE CLIENTS - VUKA WIRELESS, but this should be persisted per tenant to allow 
    // @Value("\${mpesa.pro.consumer-key}")
    // lateinit var proConsumerKey: String

    // @Value("\${mpesa.pro.consumer-secret}")
    // lateinit var proConsumerSecret: String

    // @Value("\${mpesa.pro.short-code}")
    // lateinit var proShortCode: String

    // @Value("\${mpesa.pro.passkey}")
    // lateinit var proPassKey: String

    // //Motor B2C credentials
    // @Value("\${mpesa.free.b2c.initiator-name}")
    // lateinit var freeB2CInitiatorName: String

    // @Value("\${mpesa.free.b2c.password}")
    // lateinit var freeB2CPassword: String

    // @Value("\${mpesa.free.b2c.short-code}")
    // lateinit var motorB2CShortCode: String

    // //Life B2C credentials
    // @Value("\${mpesa.basic.b2c.initiator-name}")
    // lateinit var basicB2CInitiatorName: String

    // @Value("\${mpesa.basic.b2c.password}")
    // lateinit var basicB2CPassword: String

    // @Value("\${mpesa.basic.b2c.short-code}")
    // lateinit var basicB2CShortCode: String

    // //Health B2C credentials
    // @Value("\${mpesa.pro.b2c.initiator-name}")
    // lateinit var proB2CInitiatorName: String

    // @Value("\${mpesa.pro.b2c.password}")
    // lateinit var proB2CPassword: String

    // @Value("\${mpesa.pro.b2c.short-code}")
    // lateinit var proB2CShortCode: String

    override fun getConsumerKey(serviceType: String): String {
        // return when(ServiceTypeEnum.valueOf(serviceType)) {
        //     ServiceTypeEnum.FREE -> freeConsumerKey
        //     ServiceTypeEnum.BASIC  -> basicConsumerKey
        //     ServiceTypeEnum.PRO -> proConsumerKey
        // }
        return consumerKey
    }

    override fun getConsumerSecret(serviceType: String): String {
        // return when(ServiceTypeEnum.valueOf(serviceType)) {
        //     ServiceTypeEnum.FREE -> freeConsumerSecret
        //     ServiceTypeEnum.BASIC  -> basicConsumerSecret
        //     ServiceTypeEnum.PRO -> proConsumerSecret
        // }
        return consumerSecret
    }

    override fun getBusinessShortCode(serviceType: String): String {
        // return when(ServiceTypeEnum.valueOf(serviceType)) {
        //     ServiceTypeEnum.FREE -> freeShortCode
        //     ServiceTypeEnum.BASIC  -> basicShortCode
        //     ServiceTypeEnum.PRO -> proShortCode
        // }
        return shortCode
    }

    override fun getPassKey(serviceType: String): String {
        // return when(ServiceTypeEnum.valueOf(serviceType)) {
        //     ServiceTypeEnum.FREE -> freePassKey
        //     ServiceTypeEnum.BASIC  -> basicPassKey
        //     ServiceTypeEnum.PRO -> proPassKey
        // }
        return passKey
    }

    override fun getInitiatorName(serviceType: String): String {
        // return when(ServiceTypeEnum.valueOf(serviceType)) {
        //     ServiceTypeEnum.FREE -> freeB2CInitiatorName
        //     ServiceTypeEnum.BASIC  -> basicB2CInitiatorName
        //     ServiceTypeEnum.PRO -> proB2CInitiatorName
        // }

        return b2CInitiatorName
    }

    override fun getB2CPassword(serviceType: String): String {
        // return when(ServiceTypeEnum.valueOf(serviceType)) {
        //     ServiceTypeEnum.FREE -> freeB2CPassword
        //     ServiceTypeEnum.BASIC  -> basicB2CPassword
        //     ServiceTypeEnum.PRO -> proB2CPassword
        // }
        return b2CPassword
    }

    override fun getB2CShortCode(serviceType: String): String {
        // return when(ServiceTypeEnum.valueOf(serviceType)) {
        //     ServiceTypeEnum.FREE -> freeB2CShortCode
        //     ServiceTypeEnum.BASIC  -> basicB2CShortCode
        //     ServiceTypeEnum.PRO -> proB2CShortCode
        // }
        return b2CShortCode
    }

}