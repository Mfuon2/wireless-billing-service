package com.softel.mpesa.entity.cache

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
import java.io.Serializable
import org.springframework.data.redis.core.RedisHash

// @Getter
// @Setter
// @NoArgsConstructor
// @AllArgsConstructor
@RedisHash(value="dashboard")     //five minutes
data class GeneralDashboard (
       
        // var INSTANCE: GeneralDashboard,

        // // name of the companion object is omitted
        // companion object {
        //         fun getInstance(): GeneralDashboard {
        //                 if(INSTANCE == null) {
        //                         INSTANCE =  GeneralDashboard();
        //                         }
        //                 return INSTANCE;
        //                 }
        // },
        @JsonIgnore
        var serialVersionUID: Long = 0,

        var id: String = "general", 

        var countClients: Long = 1,
        var incomeLast24hours: Double = 1.0,
        var paybillBalance: Double = 0.0,

        var totalPayment: Long = 0,                         //total amount
        var totalVoucherPurchase: Long = 0,                 //total amount that went to purchase vouchers
        var totalWalletDeposit: Long = 0                   //amount in wallet aka liabilities
    
)