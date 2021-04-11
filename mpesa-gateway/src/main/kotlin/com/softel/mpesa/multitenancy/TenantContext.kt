package com.softel.mpesa.multitenancy

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import org.springframework.context.annotation.Profile

object TenantContext {
 
    val DEFAULT_TENANT_IDENTIFIER: String = "public"
    val TENANT_IDENTIFIER: ThreadLocal<String>? = InheritableThreadLocal<String>()      //allow nulls to accomodate `jpa.generate-ddl:true`

    fun setTenant(tenantIdentifier:String) {
        TENANT_IDENTIFIER?.set(tenantIdentifier);
        }

    fun reset() = TENANT_IDENTIFIER?.remove();

}