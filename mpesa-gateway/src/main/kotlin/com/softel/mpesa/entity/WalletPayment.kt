package com.softel.mpesa.entity

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnore
import com.softel.mpesa.enums.ServiceRequestStatusEnum
import com.softel.mpesa.enums.ServiceTypeEnum
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
@Table(name = "client_wallet_payment")
class WalletPayment (

        @JsonIgnore
        @Version
        var version: Long = 0,

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        var id: Long = 0,

        @JsonIgnore
        @ManyToOne(fetch = FetchType.EAGER)
        @JoinColumn(name = "wallet_id", nullable = false, referencedColumnName = "id")
        var wallet: Wallet,

        @JsonIgnore
        @ManyToOne(fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
        @JoinColumn(name = "client_subscription_id", nullable = false, referencedColumnName = "id")
        var subscription: Subscription,

        @Column(nullable = false)
        var amount: Double,

        @Column(nullable = true)
        var accountReference: String?,

        @Column(nullable = true)
        var transactionDescription: String?,

        @Column(nullable = true)
        @Enumerated(EnumType.STRING)
        var serviceRequestStatus: ServiceRequestStatusEnum?,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
        @Column(nullable = false, columnDefinition = "timestamp default CURRENT_TIMESTAMP")
        var createdAt: LocalDateTime = LocalDateTime.now(),

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
        @Column(nullable = false, columnDefinition = "timestamp default CURRENT_TIMESTAMP")
        var updatedAt: LocalDateTime = LocalDateTime.now()
)