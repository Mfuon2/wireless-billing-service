package com.softel.mpesa.service.common.impl

import com.softel.mpesa.enums.AccountTransactionType
import com.softel.mpesa.enums.ServiceTypeEnum

import com.softel.mpesa.entity.Subscription
import com.softel.mpesa.dto.SubscriptionDto

import com.softel.mpesa.repository.SubscriptionRepository

import com.softel.mpesa.service.common.ISubscription
import com.softel.mpesa.util.Result
import com.softel.mpesa.util.ResultFactory

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import com.github.dozermapper.core.Mapper

import java.time.LocalDateTime

@Service
class SubscriptionService: ISubscription {

    @Autowired
    lateinit var subscriptionRepo: SubscriptionRepository

    @Autowired
    lateinit var mapper: Mapper

    override fun createSubscription(subscriptionDto: SubscriptionDto): Result<Subscription>{

        var sub = mapper.map(subscriptionDto, Subscription::class.java)
        //need to get client and service first...
        
        sub.createdAt = LocalDateTime.now()
        sub.updatedAt = LocalDateTime.now()

        val newSub = subscriptionRepo.save(sub)

        return if (newSub != null)
            ResultFactory.getSuccessResult(msg = "Request successfully processed", data = newSub)
        else
            ResultFactory.getFailResult(msg = "Could not create package")
        }


    override fun getSubscription(id: Long): Result<Subscription> {

        val sub = subscriptionRepo.findById(id)

        return if(sub.isPresent())
            ResultFactory.getSuccessResult(msg = "Request successfully processed", data = sub.get())
        else
            ResultFactory.getFailResult(msg = "No package found with the given code")

        }



}