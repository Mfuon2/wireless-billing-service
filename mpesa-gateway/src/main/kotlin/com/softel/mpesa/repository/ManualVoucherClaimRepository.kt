package com.softel.mpesa.repository


import com.softel.mpesa.entity.ManualVoucherClaim

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort

interface ManualVoucherClaimRepository: JpaRepository<ManualVoucherClaim, Long> {
    override fun findAll(pageable: Pageable): Page<ManualVoucherClaim?>
    }