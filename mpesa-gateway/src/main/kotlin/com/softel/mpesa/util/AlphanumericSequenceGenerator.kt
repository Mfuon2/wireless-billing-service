package com.softel.mpesa.util

import java.io.Serializable
import java.util.Properties
import org.hibernate.HibernateException
import org.hibernate.MappingException
import org.hibernate.engine.spi.SharedSessionContractImplementor
import org.hibernate.id.enhanced.SequenceStyleGenerator
import org.hibernate.internal.util.config.ConfigurationHelper
import org.hibernate.service.ServiceRegistry
import org.hibernate.type.LongType
import org.hibernate.type.Type

class AlphanumericSequenceGenerator:SequenceStyleGenerator() {

  lateinit var valuePrefix:String
  lateinit var numberFormat:String
  lateinit var incrementParam:String


  @Throws(HibernateException::class)
  override fun generate(session:SharedSessionContractImplementor, `object`:Any):Serializable {
    return valuePrefix + String.format(numberFormat, super.generate(session, `object`))
  }

  @Throws(MappingException::class)
  override fun configure(type:Type, params:Properties, serviceRegistry:ServiceRegistry) {
    super.configure(LongType.INSTANCE, params, serviceRegistry)
    valuePrefix = ConfigurationHelper.getString(VALUE_PREFIX_PARAMETER, params, VALUE_PREFIX_DEFAULT)
    numberFormat = ConfigurationHelper.getString(NUMBER_FORMAT_PARAMETER, params, NUMBER_FORMAT_DEFAULT)
    incrementParam = ConfigurationHelper.getString(INCREMENT_PARAM_PARAMETER, params, INCREMENT_PARAM_DEFAULT)
  }
  
  companion object {
    val VALUE_PREFIX_PARAMETER = "valuePrefix"
    val VALUE_PREFIX_DEFAULT = "VUKA"

    val NUMBER_FORMAT_PARAMETER = "numberFormat"
    val NUMBER_FORMAT_DEFAULT = "%05d"

    val INCREMENT_PARAM_PARAMETER = "incrementParam"
    val INCREMENT_PARAM_DEFAULT = "50"
  }
}