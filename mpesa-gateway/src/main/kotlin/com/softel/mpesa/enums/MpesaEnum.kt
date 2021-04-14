package com.softel.mpesa.enums

enum class MpesaTransactionType{
    CustomerPayBillOnline,
    CustomerBuyGoodsOnline
}

enum class MpesaCommandID(val command: String){
    BUSINESS_PAYMENT("BusinessPayment")
}

enum class MpesaTokenCacheEnum(val type: String){
    ADHOC_TOKEN("motorMpesaAuthToken"),
    PRE_PAID_TOKEN("lifeMpesaAuthToken"),
    POST_PAID_TOKEN("healthMpesaAuthToken");
    companion object {
        fun getCacheType(service: ServiceTypeEnum): MpesaTokenCacheEnum =
                when(service) {
                    ServiceTypeEnum.ADHOC -> ADHOC_TOKEN
                    ServiceTypeEnum.PRE_PAID -> PRE_PAID_TOKEN
                    ServiceTypeEnum.POST_PAID -> POST_PAID_TOKEN
                }
    }
}

enum class StkRequestType{
    DEPOSIT,            //wallet deposit
    PAYMENT
}