package com.softel.mpesa.entity

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnore
import com.softel.mpesa.enums.ServiceTypeEnum
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
import javax.persistence.ManyToOne
import javax.persistence.FetchType
import javax.persistence.CascadeType
import javax.persistence.JoinColumn
import org.hibernate.annotations.ColumnTransformer

@Entity
@Table( name = "client_wallet",
        uniqueConstraints = [UniqueConstraint(columnNames = ["client_account_id", "serviceType"])]
)
class Wallet(

        @JsonIgnore
        @Version
        var version: Long = 0,

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        var id: Long = 0,

        @JsonIgnore
        @ManyToOne(fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
        @JoinColumn(name = "client_account_id", nullable = false, referencedColumnName = "accountNumber")
        var clientAccount: ClientAccount,

        @Column(nullable = false, columnDefinition = "varchar(255) default 'PRE_PAID'")
        @Enumerated(EnumType.STRING)
        var serviceType: ServiceTypeEnum,

        @Column(nullable = false)
        var balance: Double,

        // @Column(nullable = true)
        // var description: String?,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
        @Column(nullable = false, columnDefinition = "timestamp default CURRENT_TIMESTAMP")
        var createdAt: LocalDateTime = LocalDateTime.now(),

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
        @Column(nullable = false, columnDefinition = "timestamp default CURRENT_TIMESTAMP")
        var updatedAt: LocalDateTime = LocalDateTime.now()
)