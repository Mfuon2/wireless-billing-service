package com.softel.mpesa.service.common.impl

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort

import com.softel.mpesa.dto.WalletDto
import com.softel.mpesa.dto.BuyFromWalletDto

import com.softel.mpesa.enums.AccountTransactionType
import com.softel.mpesa.enums.ServiceTypeEnum

// import com.softel.mpesa.entity.StatementAccount
import com.softel.mpesa.entity.Wallet
import com.softel.mpesa.entity.ClientAccount

import com.softel.mpesa.service.common.ISms

// import com.softel.mpesa.repository.StatementAccountRepository
import com.softel.mpesa.repository.WalletRepository
import com.softel.mpesa.repository.ClientAccountRepository
import com.softel.mpesa.repository.ServicePackageRepository
import com.softel.mpesa.repository.VoucherUploadRepository

import com.softel.mpesa.service.common.IWalletService
import com.softel.mpesa.util.Result
import com.softel.mpesa.util.ResultFactory

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import com.github.dozermapper.core.Mapper

import java.time.LocalDateTime

@Service
class WalletService: IWalletService {

    @Autowired
    lateinit var walletRepository: WalletRepository

    @Autowired
    lateinit var clientAccountRepo: ClientAccountRepository

    @Autowired
    lateinit var packageRepository: ServicePackageRepository

    @Autowired
    lateinit var tempVoucherRepo: VoucherUploadRepository

    @Autowired
    lateinit var smsService: ISms

    @Autowired
    lateinit var mapper: Mapper

    override fun findAllPaged(pageable: Pageable): Page<Wallet?>{
        return walletRepository.findAll(pageable);
        }

    override fun getWallet(id: Long): Result<Wallet?> {
        val wallet = walletRepository.findById(id)
        return if(wallet.isPresent())
            ResultFactory.getSuccessResult(msg = "Request successfully processed", data = wallet.get())
        else
            ResultFactory.getFailResult(msg = "No wallet found with the given id")
        }

    override fun createWallet(walletDto: WalletDto): Result<Wallet>{

        var wallet = mapper.map(walletDto, Wallet::class.java)
        
        val clientAccount = clientAccountRepo.findByAccountNumber(walletDto.accountNumber)?: return ResultFactory.getFailResult(msg = "Account not found")

        wallet.clientAccount = clientAccount
        wallet.createdAt = LocalDateTime.now()
        wallet.updatedAt = LocalDateTime.now()

        val newWallet = walletRepository.save(wallet)

        return if (newWallet != null)
            ResultFactory.getSuccessResult(msg = "Request successfully processed", data = newWallet)
        else
            ResultFactory.getFailResult(msg = "Could not create wallet")
        }


    override fun findOrCreateWallet(clientAccount: ClientAccount, balance: Double, serviceType: ServiceTypeEnum): Wallet {
        //return walletRepository.findByClientAccount(clientAccount, serviceType)
        return walletRepository.findByAccountNumber(clientAccount.accountNumber, serviceType)
                ?: walletRepository.save(
                        Wallet(
                                clientAccount   = clientAccount,
                                balance         = balance,
                                serviceType     = serviceType
                        )
                )
        }

        
    override fun buyFromWallet(buyFromWallet: BuyFromWalletDto): Result<String>{

        var msg = "Welcome to vuka wireless"

        //a. validate wallet  
            //- balance should be greater than package to be purchased
        val wallet = walletRepository.findByAccountNumber(buyFromWallet.accountNumber, ServiceTypeEnum.valueOf("PRE_PAID"))
        if(wallet == null)
            return ResultFactory.getFailResult("Wallet not found. Only PRE_PAID wallets are supported")
        else{
            //b. get service package 

            val pack = packageRepository.findByCode(buyFromWallet.servicePackageCode) ?: return ResultFactory.getFailResult(msg = "Package not found")

            if(pack.price > wallet.balance)
                return ResultFactory.getFailResult("Insufficient balance");
            else{
                //c. get voucher
                val voucher = tempVoucherRepo.findOneUnclaimedTempVoucherByPlan(buyFromWallet.servicePackageCode)

                if(voucher == null){
                    msg = "There is currently no voucher available for " + buyFromWallet.servicePackageCode + ". Please try again later"
                    }
                else{
                    msg = voucher.plan + " purchase confirmed. Your voucher code is " + voucher.voucherId + ". It will expire on " + voucher.expiryTime 
                    voucher.claimedTime = LocalDateTime.now().toString()
                    tempVoucherRepo.save(voucher)
                    }

                //d. reduce wallet balance 
                wallet.balance = wallet.balance - pack.price
                walletRepository.save(wallet)

                //e. send sms
                val hashMap:HashMap<String,String> = HashMap<String,String>() //define empty hashmap  
                hashMap.put("to",wallet.clientAccount.msisdn)
                hashMap.put("message",msg)
                smsService.sendAnySms(hashMap)    

                return ResultFactory.getSuccessResult("Successfull purchase from wallet")
                }

            }
        
        }
    
    
    // @Transactional
    // override fun creditWallet(walletDto: WalletDto) {
    //     var wallet = walletRepository.findByAccountNumber(walletDto.accountNumber, walletDto.serviceType)

    //         val newBalance      = wallet.balance.plus(walletDto.amount)
    //         wallet.balance      = newBalance
    //         wallet.updatedAt    = LocalDateTime.now()
    //         wallet              = walletRepository.save(wallet)
  
    //     // val statement = StatementAccount(
    //     //         wallet                  = wallet,
    //     //         transactionAmount       = walletDto.amount,
    //     //         transactionReference    = walletDto.reference,
    //     //         transactionType         = AccountTransactionType.CREDIT.type,
    //     //         description             = walletDto.description,
    //     //         accountBalance          = wallet.balance,
    //     //         tag                     = walletDto.tag
    //     // )
    //     // statementAccountRepository.save(statement)
    // }



    // @Transactional
    // override fun debitWallet(walletDto: WalletDto): Result<String> {
    //     var wallet = walletRepository.findByAccountNumber(walletDto.accountNumber, walletDto.serviceType)

    //     return when (wallet == null) {
    //         true -> ResultFactory.getFailResult(msg = "Wallet does not exist.")

    //         false -> {
    //             val walletBalance = wallet.balance

    //             if (walletBalance >= walletDto.amount) {
    //                 val newBalance = walletBalance.minus(walletDto.amount)
    //                 wallet.balance      = newBalance
    //                 wallet.updatedAt    = LocalDateTime.now()
    //                 wallet              = walletRepository.save(wallet)

    //                 val statement = StatementAccount(
    //                         wallet                  = wallet,
    //                         transactionAmount       = walletDto.amount,
    //                         transactionReference    = walletDto.reference,
    //                         transactionType         = AccountTransactionType.DEBIT.type,
    //                         description             = walletDto.description,
    //                         accountBalance          = wallet.balance,
    //                         tag                     = walletDto.tag
    //                 )
    //                 statementAccountRepository.save(statement)
    //                 ResultFactory.getSuccessResult(msg = "Account successfully debited")
    //             } else {
    //                 ResultFactory.getFailResult(msg = "Insufficient Balance.")
    //             }
    //         }
    //     }

    // }

    // override fun getWalletDetails(accountNumber: String, serviceType: String): Result<Wallet> {
    //     val wallet = walletRepository.findByAccountNumber(
    //             accountNumber = accountNumber,
    //             serviceType = ServiceTypeEnum.valueOf(serviceType.toUpperCase()))

    //     return if (wallet == null)
    //         ResultFactory.getFailResult(msg = "User not found")
    //     else
    //         ResultFactory.getSuccessResult(data = wallet)
    // }

    // override fun findOrCreateWallet(accountNumber: String, balance: Double, serviceType: ServiceTypeEnum): Wallet {
    //     return walletRepository.findByAccountNumber(accountNumber, serviceType)
    //             ?: walletRepository.save(
    //                     Wallet(
    //                             accountNumber   = accountNumber,
    //                             balance         = balance,
    //                             serviceType     = serviceType
    //                     )
    //             )
    // }


}