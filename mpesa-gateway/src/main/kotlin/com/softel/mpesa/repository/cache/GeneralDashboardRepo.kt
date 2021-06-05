


package com.softel.mpesa.repository.cache

import com.softel.mpesa.enums.MpesaCallbackEnum

import com.softel.mpesa.entity.cache.GeneralDashboard

import org.springframework.data.repository.CrudRepository

import org.springframework.data.jpa.repository.Query

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort

interface GeneralDashboardRepository: CrudRepository<GeneralDashboard, String> {
}