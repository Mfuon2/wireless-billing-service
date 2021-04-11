package com.softel.mpesa.entity 

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnore
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Version
import org.hibernate.annotations.ColumnTransformer

@Entity
@Table(name = "statement_account")
class StatementAccount (
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        var id: Long = 0,

        @JsonIgnore
        @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn(name = "wallet_id", nullable = false, referencedColumnName = "id")
        var wallet: Wallet,

        @Column(nullable = false)
        var transactionAmount: Double,

        @Column(nullable = false)
        @ColumnTransformer(
                write = "pgp_sym_encrypt(?, 'mySecretKey')",
                read = "pgp_sym_decrypt(transaction_reference::bytea, 'mySecretKey')"
                )
        var transactionReference: String,

        @Column(nullable = false)
        var transactionType: String,

        @Column(nullable = false)
        var description: String,

        @Column(nullable = false)
        var accountBalance: Double,

        @Column(nullable = true)
        var tag: String,

        @JsonIgnore
        @Version
        var version: Long = 0,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
        @Column(nullable = false, columnDefinition = "timestamp default CURRENT_TIMESTAMP")
        var createdAt: LocalDateTime = LocalDateTime.now(),

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
        @Column(nullable = false, columnDefinition = "timestamp default CURRENT_TIMESTAMP")
        var updatedAt: LocalDateTime = LocalDateTime.now()
)