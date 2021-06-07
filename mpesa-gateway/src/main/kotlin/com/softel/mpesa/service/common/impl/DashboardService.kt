package com.softel.mpesa.service.common.impl

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.softel.mpesa.enums.AccountTransactionType
import com.softel.mpesa.enums.ServiceTypeEnum

import com.softel.mpesa.entity.cache.GeneralDashboard
import com.softel.mpesa.dto.PackageDto

import com.softel.mpesa.repository.cache.GeneralDashboardRepository

import com.softel.mpesa.service.common.IDashboard
import com.softel.mpesa.service.common.IClientAccountService
import com.softel.mpesa.service.mpesa.IMpesaC2BService


import com.softel.mpesa.util.Result
import com.softel.mpesa.util.ResultFactory

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import com.github.dozermapper.core.Mapper

import java.time.LocalDateTime

@Service
class DashboardService: IDashboard {

    @Autowired
    lateinit var generalDashboardRepo: GeneralDashboardRepository

    @Autowired
    lateinit var clientService: IClientAccountService
    
    @Autowired
    lateinit var mpesaC2BService: IMpesaC2BService


    override fun initializeGeneralDashboard(){
        val dashboard: GeneralDashboard = GeneralDashboard(
            id = "general",

            countClients = clientService.countClientAccounts(), 
            paybillBalance = mpesaC2BService.getPaybillBalance(),
            incomeLast24hours = 0.0,

            totalPayment = 0,                         //total amount
            totalVoucherPurchase = 0,                 //total amount that went to purchase vouchers
            totalWalletDeposit = 0,                   //total liability
            )

        generalDashboardRepo.save(dashboard)

        }

    override fun getGeneralDashboard(): Result<GeneralDashboard>{
        val dash = generalDashboardRepo.findById("general")
        return if(dash.isPresent())
            ResultFactory.getSuccessResult(msg = "Request successfully processed", data = dash.get())
        else
            ResultFactory.getFailResult(msg = "No dashboard found with the given id")
        }
    

}