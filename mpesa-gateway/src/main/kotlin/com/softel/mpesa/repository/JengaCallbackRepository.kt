package com.softel.mpesa.repository


import com.softel.mpesa.entity.jenga.JengaCallback

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

interface JengaCallbackRepository: JpaRepository<JengaCallback, Long> {

}