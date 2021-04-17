// package com.softel.mpesa.repository

// import com.softel.mpesa.entity.StatementAccount

// import org.springframework.data.domain.Page
// import org.springframework.data.domain.Pageable
// import org.springframework.data.jpa.repository.JpaRepository
// import org.springframework.data.jpa.repository.Query
// import org.springframework.data.repository.query.Param

// import java.time.LocalDateTime

// interface StatementAccountRepository: JpaRepository<StatementAccount, Long> {
//     @Query("SELECT s FROM StatementAccount s WHERE s.wallet.id=:walletId ORDER BY s.createdAt DESC")
//     fun findByWallet(@Param("walletId")walletId: Long, pageable: Pageable): Page<StatementAccount>

//     @Query("SELECT s FROM StatementAccount s WHERE s.wallet.id=:walletId AND s.createdAt BETWEEN :startDate AND :endDate ORDER BY s.createdAt DESC")
//     fun findByWalletAndCreatedAtDateBetween(
//             walletId: Long,
//             startDate: LocalDateTime,
//             endDate: LocalDateTime,
//             pageable: Pageable
//     ): Page<StatementAccount>
// }