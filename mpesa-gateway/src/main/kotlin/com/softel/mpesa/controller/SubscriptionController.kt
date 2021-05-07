package com.softel.mpesa.controller

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import com.softel.mpesa.dto.SubscriptionDto
import com.softel.mpesa.entity.Subscription
import com.softel.mpesa.entity.ClientAccount
import com.softel.mpesa.service.common.ISubscription
import com.softel.mpesa.util.Result
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

//@Hidden
@RestController
@CrossOrigin(origins = ["http://localhost","http://localhost:4200","http://127.0.0.1:4200","http://127.0.0.1"])
@RequestMapping("/subscription")
@Tag(name = "Subscription API", description = "Subscription management")
class SubscriptionController {

    @Autowired
    lateinit var subscriptionService: ISubscription

    @Operation(summary = "Get paged list", description = "Get a paged list of subscriptions")
    @GetMapping(value = ["/paged"], produces = ["application/json"])
    fun getPagedSubscriptions(
        //@Parameter(name = "pageable",description = "Paging and sorting parameters", required = false)
        //@PageableDefault(page=0, size=50, sort = ["accountName"], direction = Sort.Direction.ASC)
        pageable: Pageable): Page<Subscription?> = subscriptionService.findAllPaged(pageable)

    @Operation(summary = "Get Subscription", description = "Get Subscription")
    @GetMapping(value = ["/get"], produces = ["application/json"])
    fun getSubscription(
        @Parameter(name = "id",description = "Identifier", required = true)
        @RequestParam id: Long): Result<Subscription> = subscriptionService.getSubscription(id)

    @Operation(summary = "Create a new subscription", description = "Allows creation of a new subscription")
    @PostMapping(value = ["/create"], produces = ["application/json"])      
    fun createSubscription(@Valid @RequestBody subscriptionDto: SubscriptionDto
    ):Result<Subscription> = subscriptionService.createSubscription(subscriptionDto)

    //update
}