package com.softel.mpesa.enums

enum class ServiceTypeEnum{
    FREE,
    BASIC,
    PRO
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