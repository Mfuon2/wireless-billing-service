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
import com.softel.mpesa.enums.VoucherClaimReason

@Entity
@Table( name = "voucher_upload",
        uniqueConstraints = [UniqueConstraint(columnNames = ["voucherId", "plan"])]
)
class VoucherUpload(

        @JsonIgnore
        @Version
        var version: Long = 0,

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        var id: Long = 0,

        @Column(nullable = false)
        var voucherId: String,

        @Column(nullable = true)
        var portal: String,

        @Column(nullable = true)
        var plan: String,                       //aka service product

        @Column(nullable = true)
        var creationTime: String,  

        @Column(nullable = true)
        var claimedTime: String, 
        
        @Column(nullable = true)
        var expiryTime: String,  

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
        @Column(nullable = false, columnDefinition = "timestamp default CURRENT_TIMESTAMP")
        var createdAt: LocalDateTime = LocalDateTime.now(),

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
        @Column(nullable = false, columnDefinition = "timestamp default CURRENT_TIMESTAMP")
        var updatedAt: LocalDateTime = LocalDateTime.now()
)



@Entity
@Table( name = "manual_voucher_claim")
data class ManualVoucherClaim(
    
        @JsonIgnore
        @Version
        var version: Long = 0,

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        var id: Long = 0,

        @Column(nullable = false)
        val msisdn: String,

        @Column(nullable = false)
        var voucherId: String,

        @Column(nullable = false)
        val reason: VoucherClaimReason,

        @Column(nullable = true)
        val mpesaMessage: String,        

        @Column(nullable = true)
        val remarks: String,
        
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
        @Column(nullable = false, columnDefinition = "timestamp default CURRENT_TIMESTAMP")
        var createdAt: LocalDateTime = LocalDateTime.now(),

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
        @Column(nullable = false, columnDefinition = "timestamp default CURRENT_TIMESTAMP")
        var updatedAt: LocalDateTime = LocalDateTime.now()

        )