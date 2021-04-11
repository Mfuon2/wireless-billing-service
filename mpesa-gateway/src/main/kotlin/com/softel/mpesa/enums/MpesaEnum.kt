package com.softel.mpesa.enums

enum class MpesaTransactionType{
    CustomerPayBillOnline,
    CustomerBuyGoodsOnline
}

enum class MpesaCommandID(val command: String){
    BUSINESS_PAYMENT("BusinessPayment")
}

enum class MpesaTokenCacheEnum(val type: String){
    FREE_TOKEN("motorMpesaAuthToken"),
    BASIC_TOKEN("lifeMpesaAuthToken"),
    PRO_TOKEN("healthMpesaAuthToken");
    companion object {
        fun getCacheType(service: ServiceTypeEnum): MpesaTokenCacheEnum =
                when(service) {
                    ServiceTypeEnum.FREE -> FREE_TOKEN
                    ServiceTypeEnum.BASIC -> BASIC_TOKEN
                    ServiceTypeEnum.PRO -> PRO_TOKEN
                }
    }
}

enum class StkRequestType{
    DEPOSIT,
    PAYMENT
}