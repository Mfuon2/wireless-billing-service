package com.softel.mpesa.enums

enum class ServiceTypeEnum{ //TODO refactor to BillingTypeEnum
    ADHOC,          
    PRE_PAID,       
    POST_PAID       
}

enum class SubscriptionPlan(val plan: String) {
    DEMO("Demo"),
    PERSONAL("Personal"),
    BUSINESS("Business"),
    PLATINUM("Platinum")        //Big biz, trusted and very flexible payments... cheque etc
    }

enum class MpesaCallbackEnum(val type: String){
    C2B_VALIDATION("Validation"),
    C2B_CONFIRMATION("Confirmation") 
    }

enum class BillingCycle{
    ADHOC,          //works with USAGE_*      
    WEEKLY,       
    MONTHLY, 
    ANNUAL       
    }

enum class RenewalCycleType{            //move to Subscription
    FIXED,       
    ANNIVERSARY,
    USAGE_ONCE,             //use with usage
    USAGE_AUTORENEW         //usage auto renew
    }

enum class AccountTransactionType(val type: String){
    DEBIT("DR"),
    CREDIT("CR")
}

enum class ServiceRequestStatusEnum {
    PENDING,
    COMPLETED,
    FAILED
}

enum class PaymentType {
    MPESA_EXPRESS,  //stk push
    MPESA_C2B,      //paybill
    WALLET          //from wallet
    //cash, cheque ?
}

enum class StatementTag(val type: String) {
    WITHDRAWAL("Withdrawal"),
    TOP_UP("Top Up"),
    REFUND("Refund"),
    PAYMENT("Payment")
}

enum class WithdrawalType {
    MPESA_B2C
}

enum class EnvironmentProfile {
    DEV,
    UAT,
    PROD
}

enum class PaymentStatusEnum {
    PENDING,
    SUCCESSFUL,
    FAILED
}

