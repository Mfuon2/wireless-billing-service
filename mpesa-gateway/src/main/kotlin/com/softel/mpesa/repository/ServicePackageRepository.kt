package com.softel.mpesa.repository

import com.softel.mpesa.entity.ServicePackage
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface ServicePackageRepository: JpaRepository<ServicePackage, Long> {

    @Query("SELECT p FROM ServicePackage p WHERE p.code=:code")
    fun findByCode(code: String): ServicePackage?

   
}