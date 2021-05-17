package com.softel.mpesa.repository


import com.softel.mpesa.entity.VoucherUpload

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort

interface VoucherUploadRepository: JpaRepository<VoucherUpload, Long> {

}