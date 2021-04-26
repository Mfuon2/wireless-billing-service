package com.softel.mpesa.repository

import com.softel.mpesa.entity.ServicePackage
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

interface ServicePackageRepository: JpaRepository<ServicePackage, Long> {

    @Query("SELECT p FROM ServicePackage p WHERE p.code=:code")
    fun findByCode(code: String): ServicePackage?

    override fun findAll(pageable: Pageable): Page<ServicePackage?>

}