package com.softel.mpesa.enums

enum class ServiceTypeEnum{
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
    MPESA_EXPRESS,
    WALLET
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

