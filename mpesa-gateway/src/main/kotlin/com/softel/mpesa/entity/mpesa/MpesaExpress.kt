package com.softel.mpesa.entity.mpesa

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnore
import com.softel.mpesa.enums.PaymentStatusEnum
import com.softel.mpesa.enums.ServiceRequestStatusEnum
import com.softel.mpesa.enums.ServiceTypeEnum
import com.softel.mpesa.enums.StkRequestType
import com.softel.mpesa.entity.Wallet
import java.time.LocalDateTime
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
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
@Table(name = "mpesa_express")
class MpesaExpress(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        var id: Long = 0,

        @JsonIgnore
        @ManyToOne(fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
        @JoinColumn(name = "wallet_id", nullable = false, referencedColumnName = "id")
        var wallet: Wallet,

        @Column(nullable = false)
        var shortCode: Int,

        @Column(nullable = false)
        var amount: Double,

        @Column(nullable = false)
        @ColumnTransformer(
                write = "pgp_sym_encrypt(?, 'mySecretKey')",
                read = "pgp_sym_decrypt(msisdn::bytea, 'mySecretKey')"
                )
        var msisdn: String,

        @Column(nullable = false)
        var transactionType: String,

        @Column(nullable = false)
        var transactionDescription: String,

        @Column(nullable = false)
        @ColumnTransformer(
                write = "pgp_sym_encrypt(?, 'mySecretKey')",
                read = "pgp_sym_decrypt(account_reference::bytea, 'mySecretKey')"
                )
        var accountReference: String,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
        @Column(nullable = true)
        var transactionDate: LocalDateTime,

        @Column(nullable = false, unique = true)
        var merchantRequestId: String,

        @Column(nullable = false, unique = true)
        var checkoutRequestId: String,

        @Column(nullable = false)
        var responseCode: Int,

        @Column(nullable = true)
        var responseDescription: String?,

        @Column(nullable = true)
        var customerMessage: String?,

        @Column(nullable = true)
        var resultCode: Int?,

        @Column(nullable = true)
        var resultDescription: String?,

        @Column(nullable = true)
        var mpesaReceiptNumber: String?,

        @Column(nullable = true)
        @Enumerated(EnumType.STRING)
        var serviceType: ServiceTypeEnum?,

        @Column(nullable = true)
        @Enumerated(EnumType.STRING)
        var paymentStatus: PaymentStatusEnum?,

        @Column(nullable = true)
        @Enumerated(EnumType.STRING)
        var serviceRequestStatus: ServiceRequestStatusEnum?,

        @Column(nullable = true)
        @Enumerated(EnumType.STRING)
        var servicePaymentStatus: PaymentStatusEnum?,

        @Column(nullable = true)
        @Enumerated(EnumType.STRING)
        var requestType: StkRequestType,


  


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