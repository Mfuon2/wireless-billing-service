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
import org.hibernate.annotations.ColumnTransformer

@Entity
@Table( name = "wallet",
        uniqueConstraints = [UniqueConstraint(columnNames = ["accountNumber", "serviceType"])]
)
class Wallet(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        var id: Long = 0,

        @Column(nullable = false)
        var accountNumber: String,

        @Column(nullable = false, columnDefinition = "varchar(255) default 'BASIC'")
        @Enumerated(EnumType.STRING)
        var serviceType: ServiceTypeEnum,

        @Column(nullable = false)
        var balance: Double,

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