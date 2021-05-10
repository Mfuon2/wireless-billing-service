package com.softel.mpesa.entity.jenga

import com.fasterxml.jackson.annotation.JsonFormat
import com.softel.mpesa.enums.PaymentStatusEnum
import com.softel.mpesa.enums.ServiceRequestStatusEnum
import com.softel.mpesa.enums.ServiceTypeEnum
import com.softel.mpesa.enums.MpesaCallbackEnum
import com.softel.mpesa.enums.PaymentServiceProvider
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
import com.fasterxml.jackson.annotation.JsonIgnore
import org.hibernate.annotations.ColumnTransformer

@Entity
@Table(name = "jenga_callback")
class JengaCallback (

        @Version
        var version: Long = 0,

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        var id: Long = 0,

        @JsonIgnore
        @ManyToOne(fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
        @JoinColumn(name = "wallet_id", nullable = false, referencedColumnName = "id")
        var wallet: Wallet,

        @Column(nullable = false)
        var mobileNumber: String,

        @Column(nullable = false)
        var message: String,

        @Column(nullable = false)
        var rrn: String,

        @Column(nullable = false)
        @Enumerated(EnumType.STRING)
        var service: PaymentServiceProvider,


        @Column(nullable = false)
        var tranID: String,

        @Column(nullable = false)
        var resultCode: String,

        @Column(nullable = true)
        var resultDescription: String?,

        @Column(nullable = true)
        var additionalInfo: String?,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
        @Column(nullable = false, columnDefinition = "timestamp default CURRENT_TIMESTAMP")
        var createdAt: LocalDateTime = LocalDateTime.now(),

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
        @Column(nullable = false, columnDefinition = "timestamp default CURRENT_TIMESTAMP")
        var updatedAt: LocalDateTime = LocalDateTime.now()

)