package com.softel.mpesa.service.common


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.softel.mpesa.dto.WalletPaymentDto
import com.softel.mpesa.entity.WalletPayment
import com.softel.mpesa.util.Result

interface IWalletPaymentService {
    // fun processWalletPayment(request: WalletPaymentDto): Result<WalletPayment>
    // fun getPaymentDetail(transactionId: String): Result<WalletPayment>
    fun findAllPaged(pageable: Pageable): Page<WalletPayment?>

}