package com.softel.mpesa.multitenancy

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Component
import org.hibernate.HibernateException
import org.hibernate.cfg.Environment
import org.hibernate.engine.config.spi.ConfigurationService
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider
import org.hibernate.service.spi.ServiceRegistryAwareService
import org.hibernate.service.spi.ServiceRegistryImplementor

import java.sql.SQLException
import javax.sql.DataSource
import java.sql.Connection
import kotlin.collections.Map
import kotlin.collections.MutableMap
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Profile

//@Profile("uat,prod")
class AnotherMultiTenantConnectionProvider: MultiTenantConnectionProvider, ServiceRegistryAwareService {

    val log: Logger = LoggerFactory.getLogger(AnotherMultiTenantConnectionProvider::class.java)

    companion object {
        private val serialVersionUID = 1234L // unique id
      }

    lateinit var lazyDatasource: DataSource

    override fun injectServices( serviceRegistry: ServiceRegistryImplementor) {
        val lSettings = serviceRegistry.getService(ConfigurationService::class.java).getSettings()
        lazyDatasource = lSettings.get(Environment.DATASOURCE) as DataSource
        }

    @Throws(SQLException::class)
    override fun getAnyConnection(): Connection {
        return lazyDatasource.getConnection()
    }

    @Throws(SQLException::class)
    override fun releaseAnyConnection( connection: Connection) {
        connection.close()
    }

    @Throws(SQLException::class)
    override fun getConnection( tenantIdentifier: String): Connection{
        val connection: Connection = getAnyConnection()

        try {
            connection.createStatement().execute("SET SCHEMA '" + tenantIdentifier + "'")
            }
        catch (e: SQLException) {
            throw HibernateException("Could not alter JDBC connection to specified schema [" + tenantIdentifier + "]", e)
        }

        return connection;
    }
    
    @Throws(SQLException::class)
    override fun releaseConnection( tenantIdentifier: String,  connection: Connection) {
        log.info("Release connection for tenant {}", tenantIdentifier)
        connection.setSchema(TenantContext.DEFAULT_TENANT_IDENTIFIER)
        releaseAnyConnection(connection)
        }

    override fun supportsAggressiveRelease(): Boolean {
        return false;
        }

    @SuppressWarnings("rawtypes")
    override fun isUnwrappableAs(aClass:Class<*>):Boolean {
        return false
      }

    override fun <T> unwrap(aClass:Class<T>):T? {
        return null
        }

}