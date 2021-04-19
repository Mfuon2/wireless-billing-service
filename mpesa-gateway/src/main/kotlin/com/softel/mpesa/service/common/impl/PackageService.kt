package com.softel.mpesa.service.common.impl

import com.softel.mpesa.enums.AccountTransactionType
import com.softel.mpesa.enums.ServiceTypeEnum

import com.softel.mpesa.entity.ServicePackage
import com.softel.mpesa.dto.PackageDto

import com.softel.mpesa.repository.ServicePackageRepository

import com.softel.mpesa.service.common.IPackage
import com.softel.mpesa.util.Result
import com.softel.mpesa.util.ResultFactory

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import com.github.dozermapper.core.Mapper

import java.time.LocalDateTime

@Service
class PackageService: IPackage {

    @Autowired
    lateinit var packageRepository: ServicePackageRepository

    @Autowired
    lateinit var mapper: Mapper

    override fun createPackage(packageDto: PackageDto): Result<ServicePackage>{

        var pack = mapper.map(packageDto, ServicePackage::class.java)

        pack.createdAt = LocalDateTime.now()
        pack.updatedAt = LocalDateTime.now()
        val newPackage = packageRepository.save(pack);

        return if (newPackage != null)
            ResultFactory.getSuccessResult(msg = "Request successfully processed", data = newPackage)
        else
            ResultFactory.getFailResult(msg = "Could not create package")
        }


    override fun getPackage(id: Long): Result<ServicePackage> {
        val pack = packageRepository.findById(id)
        return if(pack.isPresent())
            ResultFactory.getSuccessResult(msg = "Request successfully processed", data = pack.get())
        else
            ResultFactory.getFailResult(msg = "No pack found with the given id")
        }


    override fun getPackageByCode(code: String): Result<ServicePackage> {
        val pack = packageRepository.findByCode(code)
        return if (pack != null)
            ResultFactory.getSuccessResult(msg = "Request successfully processed", data = pack)
        else
            ResultFactory.getFailResult(msg = "No package found with the given code")
        }



}