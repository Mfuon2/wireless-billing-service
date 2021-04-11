package com.softel.mpesa.exception

import org.hibernate.validator.internal.engine.path.PathImpl
import org.springframework.validation.FieldError
import javax.validation.ConstraintViolation

abstract class ApiSubError

data class ApiValidationError (
        val objectName: String,
        val field:String? = null,
        val rejectedValue:Any? = null,
        val message: String?
) : ApiSubError()

fun fieldValidationErrors(fieldErrors: List<FieldError>):List<ApiSubError> =
        fieldErrors.map {
            fieldError -> ApiValidationError(
                objectName      = fieldError.objectName,
                field           = fieldError.field,
                rejectedValue   = fieldError.rejectedValue,
                message         = fieldError.defaultMessage
        ) }

fun constraintValidationErrors(constraintViolations: MutableSet<ConstraintViolation<*>>) =
        constraintViolations.map { cv ->
            ApiValidationError(
                    objectName      = cv.rootBeanClass.simpleName,
                    field           = (cv.propertyPath as PathImpl).leafNode.asString(),
                    rejectedValue   = cv.invalidValue,
                    message         = cv.message
            ) }