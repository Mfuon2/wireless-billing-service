package com.softel.mpesa.service.common

interface IPropertyService {
    fun getConsumerKey(serviceType: String): String
    fun getConsumerSecret(serviceType: String): String
    fun getBusinessShortCode(serviceType: String): String
    fun getPassKey(serviceType: String): String
    fun getInitiatorName(serviceType: String): String
    fun getB2CPassword(serviceType: String): String
    fun getB2CShortCode(serviceType: String): String
}