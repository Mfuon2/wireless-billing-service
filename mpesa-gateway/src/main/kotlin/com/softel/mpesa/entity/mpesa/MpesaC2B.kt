package com.softel.mpesa.entity.mpesa

import com.fasterxml.jackson.annotation.JsonFormat
import com.softel.mpesa.enums.PaymentStatusEnum
import com.softel.mpesa.enums.ServiceRequestStatusEnum
import com.softel.mpesa.enums.ServiceTypeEnum
import com.softel.mpesa.enums.MpesaCallbackEnum
import com.softel.mpesa.entity.Wallet
import java.time.LocalDateTime
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
@Table(name = "mpesa_c2b_callback")
class MpesaC2BCallback (

        @Version
        var version: Long = 0,

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        var id: Long = 0,

        @Column(nullable = false)
        @Enumerated(EnumType.STRING)
        var callbackType: MpesaCallbackEnum,

        @Column(nullable = false)
        var transactionType: String,

        @Column(nullable = false)
        var transactionID: String,

        @Column(nullable = false)
        var transTime: String,

        @Column(nullable = false)
        var transAmount: String,

        @Column(nullable = false)
        var businessShortCode: Long,

        @Column(nullable = false)
        var billRefNumber: String,

        @Column(nullable = true)
        var invoiceNumber: String?,

        @Column(nullable = true)
        var orgAccountBalance: String?,

        @Column(nullable = true)
        var thirdPartyTransID: String?,

        @Column(nullable = true)
        var msisdn: String,

        @Column(nullable = false)
        var firstName: String,

        @Column(nullable = true)
        var middleName: String?,

        @Column(nullable = true)
        var lastName: String,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
        @Column(nullable = false, columnDefinition = "timestamp default CURRENT_TIMESTAMP")
        var createdAt: LocalDateTime = LocalDateTime.now(),

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
        @Column(nullable = false, columnDefinition = "timestamp default CURRENT_TIMESTAMP")
        var updatedAt: LocalDateTime = LocalDateTime.now()

)