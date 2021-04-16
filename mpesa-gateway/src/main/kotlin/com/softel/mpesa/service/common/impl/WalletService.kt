// package com.softel.mpesa.service.common.impl

// import com.softel.mpesa.dto.WalletDto

// import com.softel.mpesa.enums.AccountTransactionType
// import com.softel.mpesa.enums.ServiceTypeEnum

// import com.softel.mpesa.entity.StatementAccount
// import com.softel.mpesa.entity.Wallet

// import com.softel.mpesa.repository.StatementAccountRepository
// import com.softel.mpesa.repository.WalletRepository

// import com.softel.mpesa.service.common.IWalletService
// import com.softel.mpesa.util.Result
// import com.softel.mpesa.util.ResultFactory

// import org.springframework.beans.factory.annotation.Autowired
// import org.springframework.stereotype.Service
// import org.springframework.transaction.annotation.Transactional

// import java.time.LocalDateTime

// @Service
// class WalletService: IWalletService {
//     @Autowired
//     lateinit var walletRepository: WalletRepository

//     @Autowired
//     lateinit var statementAccountRepository: StatementAccountRepository

//     @Transactional
//     override fun creditWallet(walletDto: WalletDto) {
//         var wallet = walletRepository.findByAccountNumber(walletDto.accountNumber, walletDto.serviceType)

//         if (wallet == null) {
//             wallet = Wallet(
//                     accountNumber   = walletDto.accountNumber,
//                     balance         = walletDto.amount,
//                     serviceType     = walletDto.serviceType
//             )
//             wallet = walletRepository.save(wallet)
//         } else {
//             val newBalance      = wallet.balance.plus(walletDto.amount)
//             wallet.balance      = newBalance
//             wallet.updatedAt    = LocalDateTime.now()
//             wallet              = walletRepository.save(wallet)
//         }

//         val statement = StatementAccount(
//                 wallet                  = wallet,
//                 transactionAmount       = walletDto.amount,
//                 transactionReference    = walletDto.reference,
//                 transactionType         = AccountTransactionType.CREDIT.type,
//                 description             = walletDto.description,
//                 accountBalance          = wallet.balance,
//                 tag                     = walletDto.tag
//         )
//         statementAccountRepository.save(statement)
//     }

//     @Transactional
//     override fun debitWallet(walletDto: WalletDto): Result<String> {
//         var wallet = walletRepository.findByAccountNumber(walletDto.accountNumber, walletDto.serviceType)

//         return when (wallet == null) {
//             true -> ResultFactory.getFailResult(msg = "Wallet does not exist.")

//             false -> {
//                 val walletBalance = wallet.balance

//                 if (walletBalance >= walletDto.amount) {
//                     val newBalance = walletBalance.minus(walletDto.amount)
//                     wallet.balance      = newBalance
//                     wallet.updatedAt    = LocalDateTime.now()
//                     wallet              = walletRepository.save(wallet)

//                     val statement = StatementAccount(
//                             wallet                  = wallet,
//                             transactionAmount       = walletDto.amount,
//                             transactionReference    = walletDto.reference,
//                             transactionType         = AccountTransactionType.DEBIT.type,
//                             description             = walletDto.description,
//                             accountBalance          = wallet.balance,
//                             tag                     = walletDto.tag
//                     )
//                     statementAccountRepository.save(statement)
//                     ResultFactory.getSuccessResult(msg = "Account successfully debited")
//                 } else {
//                     ResultFactory.getFailResult(msg = "Insufficient Balance.")
//                 }
//             }
//         }

//     }

//     override fun getWalletDetails(accountNumber: String, serviceType: String): Result<Wallet> {
//         val wallet = walletRepository.findByAccountNumber(
//                 accountNumber = accountNumber,
//                 serviceType = ServiceTypeEnum.valueOf(serviceType.toUpperCase()))

//         return if (wallet == null)
//             ResultFactory.getFailResult(msg = "User not found")
//         else
//             ResultFactory.getSuccessResult(data = wallet)
//     }

//     override fun findOrCreateWallet(accountNumber: String, balance: Double, serviceType: ServiceTypeEnum): Wallet {
//         return walletRepository.findByAccountNumber(accountNumber, serviceType)
//                 ?: walletRepository.save(
//                         Wallet(
//                                 accountNumber   = accountNumber,
//                                 balance         = balance,
//                                 serviceType     = serviceType
//                         )
//                 )
//     }
// }