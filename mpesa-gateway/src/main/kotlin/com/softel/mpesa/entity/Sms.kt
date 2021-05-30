package com.softel.mpesa.entity

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnore
import com.softel.mpesa.enums.ServiceTypeEnum
import com.softel.mpesa.enums.SmsStatus

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
@Table( name = "sms_message")
class Sms(

        @JsonIgnore
        @Version
        var version: Long = 0,

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        var id: Long = 0,

        @Column(nullable = false)
        var msisdn: String,

        @Column(nullable = false)
        var message: String,

        @Column(nullable = false)
        var userName: String,

        @Column(nullable = true)
        var senderId: String?,

        @Column(nullable = false)
        var attemptCount: Int = 0,

        @Column(nullable = true)
        var attemptedTime: String?, 

        @Column(nullable = false, columnDefinition = "varchar(20) default 'PENDING'")
        @Enumerated(EnumType.STRING)
        var status: SmsStatus,

        @Column(nullable = true)
        var apiResponse: String?,

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
        @Column(nullable = false, columnDefinition = "timestamp default CURRENT_TIMESTAMP")
        var createdAt: LocalDateTime = LocalDateTime.now(),

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
        @Column(nullable = false, columnDefinition = "timestamp default CURRENT_TIMESTAMP")
        var updatedAt: LocalDateTime = LocalDateTime.now()
)