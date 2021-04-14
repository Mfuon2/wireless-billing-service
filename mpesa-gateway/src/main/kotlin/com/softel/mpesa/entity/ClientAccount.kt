package com.softel.mpesa.entity

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnore
import com.softel.mpesa.enums.ServiceTypeEnum
import com.softel.mpesa.enums.SubscriptionPlan
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table
import javax.persistence.UniqueConstraint
import javax.persistence.Version
import org.hibernate.annotations.ColumnTransformer
import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.Parameter
import com.softel.mpesa.util.AlphanumericSequenceGenerator

@Entity
@Table( name = "client_account",
        uniqueConstraints = [UniqueConstraint(columnNames = ["accountNumber", "msisdn"])]
)
class ClientAccount(

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        var id: Long = 0,

        @Column(nullable = false)
        var msisdn: String,

        @Column(nullable = true)
        var accountName: String?,

        @Column(nullable = true)
        var shortCode: String?,                 //will assist in differentiating clients across shortcodes

        @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "client_account_seq")
        @GenericGenerator(
                name = "client_account_seq", 
                strategy = "com.softel.mpesa.util.AlphanumericSequenceGenerator", 
                parameters = {
                                Parameter(name = AlphanumericSequenceGenerator.VALUE_PREFIX_PARAMETER, value = "VUKA")
                                Parameter(name = AlphanumericSequenceGenerator.NUMBER_FORMAT_PARAMETER, value = "%05d")
                                }
                )
        @Column(nullable = false)
        var accountNumber: String,

        @Column(nullable = false, columnDefinition = "varchar(255) default 'PRE_PAID'")
        @Enumerated(EnumType.STRING)
        var serviceType: ServiceTypeEnum,

        @Column(nullable = false, columnDefinition = "varchar(255) default 'PERSONAL'")
        @Enumerated(EnumType.STRING)
        var subscriptionPlan: SubscriptionPlan,

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