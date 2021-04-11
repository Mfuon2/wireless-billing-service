package com.softel.mpesa.repository

import com.softel.mpesa.enums.PaymentStatusEnum
import com.softel.mpesa.entity.mpesa.MpesaExpress
import org.springframework.data.jpa.repository.JpaRepository
import java.util.stream.Stream

interface MpesaExpressRepository: JpaRepository<MpesaExpress, Long> {
    fun findByCheckoutRequestId(checkoutRequestId: String): MpesaExpress?
    fun findByPaymentStatus(paymentStatus: PaymentStatusEnum): Stream<MpesaExpress>
}