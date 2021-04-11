package com.softel.mpesa.multitenancy

import org.hibernate.context.spi.CurrentTenantIdentifierResolver
import org.springframework.context.annotation.Profile

import org.slf4j.Logger
import org.slf4j.LoggerFactory

//@Profile("uat,prod")
class TenantIdentifierResolver : CurrentTenantIdentifierResolver {

        val log: Logger = LoggerFactory.getLogger(TenantIdentifierResolver::class.java)

        override fun resolveCurrentTenantIdentifier(): String {            
            
            val currentTenantId: String? = TenantContext.TENANT_IDENTIFIER?.get()       //allowing nulls inorder to accomodate `jpa.generate-ddl:true`
            log.info("Resolving current tenant. currentTenantId = {}", currentTenantId);
            
            return if (currentTenantId != null) currentTenantId
            else TenantContext.DEFAULT_TENANT_IDENTIFIER
            }
 
        override fun validateExistingCurrentSessions(): Boolean {
            return false
            }
        
    }