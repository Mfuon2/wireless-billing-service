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
import javax.persistence.ManyToOne
import javax.persistence.FetchType
import javax.persistence.CascadeType
import javax.persistence.JoinColumn

import org.hibernate.annotations.ColumnTransformer
import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.Parameter
import com.softel.mpesa.util.AlphanumericSequenceGenerator

@Entity
@Table( name = "client_subscription",
        uniqueConstraints = [UniqueConstraint(columnNames = ["client_account_id", "service_package_id"])]
)
class Subscription(

        @JsonIgnore
        @Version
        var version: Long = 0,

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        var id: Long = 0,

        // @JsonIgnore
        @ManyToOne(fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
        @JoinColumn(name = "client_account_id", nullable = false, referencedColumnName = "accountNumber")
        var clientAccount: ClientAccount,

        // @JsonIgnore
        @ManyToOne(fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
        @JoinColumn(name = "service_package_id", nullable = false, referencedColumnName = "id")
        var servicePackage: ServicePackage,

        @Column(nullable = false, columnDefinition = "varchar(20) default 'PERSONAL'")
        @Enumerated(EnumType.STRING)
        var subscriptionPlan: SubscriptionPlan,      

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
        @Column(nullable = false, columnDefinition = "timestamp default CURRENT_TIMESTAMP")
        var createdAt: LocalDateTime = LocalDateTime.now(),

        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
        @Column(nullable = false, columnDefinition = "timestamp default CURRENT_TIMESTAMP")
        var updatedAt: LocalDateTime = LocalDateTime.now()
)