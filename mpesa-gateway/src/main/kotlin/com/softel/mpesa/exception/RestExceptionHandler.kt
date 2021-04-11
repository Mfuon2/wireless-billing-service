package com.softel.mpesa.exception

import org.slf4j.Logger
import org.slf4j.LoggerFactory

import org.springframework.beans.ConversionNotSupportedException
import org.springframework.beans.TypeMismatchException
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.http.converter.HttpMessageNotWritableException
import org.springframework.validation.BindException

import org.springframework.web.HttpMediaTypeNotAcceptableException
import org.springframework.web.HttpMediaTypeNotSupportedException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.MissingPathVariableException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.ServletRequestBindingException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.ServletWebRequest
import org.springframework.web.context.request.WebRequest
import org.springframework.web.context.request.async.AsyncRequestTimeoutException
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import org.springframework.web.multipart.support.MissingServletRequestPartException
import org.springframework.web.servlet.NoHandlerFoundException
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

import javax.persistence.EntityNotFoundException
import javax.validation.ConstraintViolationException

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
class RestExceptionHandler: ResponseEntityExceptionHandler() {
    val log: Logger = LoggerFactory.getLogger(RestExceptionHandler::class.java)

    override fun handleHttpMessageNotReadable(ex: HttpMessageNotReadableException, headers: HttpHeaders, status: HttpStatus, request: WebRequest): ResponseEntity<Any> {
        val servletWebRequest: ServletWebRequest = request as ServletWebRequest
        log.info("{} to {}", servletWebRequest.httpMethod, servletWebRequest.request.servletPath)
        val error = "Malformed JSON request"
        return buildResponseEntity(
                ApiError(
                        status          = HttpStatus.BAD_REQUEST,
                        message         = error,
                        debugMessage    = ex.localizedMessage
                )
        )
    }

    override fun handleMissingServletRequestParameter(ex: MissingServletRequestParameterException, headers: HttpHeaders, status: HttpStatus, request: WebRequest): ResponseEntity<Any> {
        val error = ex.parameterName + " parameter is missing"
        return buildResponseEntity(
                ApiError(
                        status          = HttpStatus.BAD_REQUEST,
                        message         = error,
                        debugMessage    = ex.localizedMessage
                )
        )
    }

    override fun handleMissingPathVariable(ex: MissingPathVariableException, headers: HttpHeaders, status: HttpStatus, request: WebRequest): ResponseEntity<Any> {
        return super.handleMissingPathVariable(ex, headers, status, request)
    }

    override fun handleMethodArgumentNotValid(ex: MethodArgumentNotValidException, headers: HttpHeaders, status: HttpStatus, request: WebRequest): ResponseEntity<Any> {
        return buildResponseEntity(
                ApiError(
                        status      = HttpStatus.BAD_REQUEST,
                        message     = "Validation error",
                        subErrors   = fieldValidationErrors(ex.bindingResult.fieldErrors)
                )
        )
    }

    override fun handleExceptionInternal(ex: Exception, body: Any?, headers: HttpHeaders, status: HttpStatus, request: WebRequest): ResponseEntity<Any> {
        return super.handleExceptionInternal(ex, body, headers, status, request)
    }

    override fun handleServletRequestBindingException(ex: ServletRequestBindingException, headers: HttpHeaders, status: HttpStatus, request: WebRequest): ResponseEntity<Any> {
        return super.handleServletRequestBindingException(ex, headers, status, request)
    }

    override fun handleHttpMediaTypeNotSupported(ex: HttpMediaTypeNotSupportedException, headers: HttpHeaders, status: HttpStatus, request: WebRequest): ResponseEntity<Any> {
        val builder = StringBuilder()
        builder.append(ex.contentType)
        builder.append(" media type is not supported. Supported media types are ")
        ex.supportedMediaTypes.forEach { t -> builder.append(t).append(", ") }
        return buildResponseEntity(
                ApiError(
                        status          = HttpStatus.UNSUPPORTED_MEDIA_TYPE,
                        message         = builder.substring(0,builder.length - 2),
                        debugMessage    = ex.localizedMessage
                )
        )
    }

    override fun handleNoHandlerFoundException(ex: NoHandlerFoundException, headers: HttpHeaders, status: HttpStatus, request: WebRequest): ResponseEntity<Any> {
        return buildResponseEntity(
                ApiError(
                        status          = HttpStatus.BAD_REQUEST,
                        message         = String.format("Could not find the %s method for URL %s", ex.httpMethod, ex.requestURL),
                        debugMessage    = ex.message
                )
        )
    }

    override fun handleHttpMediaTypeNotAcceptable(ex: HttpMediaTypeNotAcceptableException, headers: HttpHeaders, status: HttpStatus, request: WebRequest): ResponseEntity<Any> {
        return super.handleHttpMediaTypeNotAcceptable(ex, headers, status, request)
    }

    override fun handleBindException(ex: BindException, headers: HttpHeaders, status: HttpStatus, request: WebRequest): ResponseEntity<Any> {
        return super.handleBindException(ex, headers, status, request)
    }

    override fun handleHttpRequestMethodNotSupported(ex: HttpRequestMethodNotSupportedException, headers: HttpHeaders, status: HttpStatus, request: WebRequest): ResponseEntity<Any> {
        return super.handleHttpRequestMethodNotSupported(ex, headers, status, request)
    }

    override fun handleMissingServletRequestPart(ex: MissingServletRequestPartException, headers: HttpHeaders, status: HttpStatus, request: WebRequest): ResponseEntity<Any> {
        return super.handleMissingServletRequestPart(ex, headers, status, request)
    }

    override fun handleAsyncRequestTimeoutException(ex: AsyncRequestTimeoutException, headers: HttpHeaders, status: HttpStatus, webRequest: WebRequest): ResponseEntity<Any>? {
        return super.handleAsyncRequestTimeoutException(ex, headers, status, webRequest)
    }

    override fun handleConversionNotSupported(ex: ConversionNotSupportedException, headers: HttpHeaders, status: HttpStatus, request: WebRequest): ResponseEntity<Any> {
        return super.handleConversionNotSupported(ex, headers, status, request)
    }

    override fun handleTypeMismatch(ex: TypeMismatchException, headers: HttpHeaders, status: HttpStatus, request: WebRequest): ResponseEntity<Any> {
        return super.handleTypeMismatch(ex, headers, status, request)
    }

    override fun handleHttpMessageNotWritable(ex: HttpMessageNotWritableException, headers: HttpHeaders, status: HttpStatus, request: WebRequest): ResponseEntity<Any> {
        val error = "Error writing JSON output"
        return buildResponseEntity(
                ApiError(
                        status          = HttpStatus.INTERNAL_SERVER_ERROR,
                        message         = error,
                        debugMessage    = ex.localizedMessage)
        )
    }

    @ExceptionHandler(ConstraintViolationException::class)
    protected fun handleConstraintViolation(
            ex: ConstraintViolationException): ResponseEntity<Any> {
        return buildResponseEntity(
                ApiError(
                        status      = HttpStatus.BAD_REQUEST,
                        message     = "Validation error",
                        subErrors   = constraintValidationErrors(ex.constraintViolations))
        )
    }

    @ExceptionHandler(EntityNotFoundException::class)
    protected fun handleEntityNotFound(
            ex: EntityNotFoundException): ResponseEntity<Any> {
        return buildResponseEntity(
                ApiError(
                        status  = HttpStatus.NOT_FOUND,
                        message = ex.message
                )
        )
    }

    @ExceptionHandler(DataIntegrityViolationException::class)
    protected fun handleDataIntegrityViolation(ex: DataIntegrityViolationException,
                                               request: WebRequest?): ResponseEntity<Any> {
        return if (ex.cause is ConstraintViolationException) {
            buildResponseEntity(
                    ApiError(
                            status          = HttpStatus.CONFLICT,
                            message         = "Database error",
                            debugMessage    = ex.localizedMessage
                    )
            )
        } else buildResponseEntity(
                ApiError(
                        status          = HttpStatus.INTERNAL_SERVER_ERROR,
                        debugMessage    = ex.localizedMessage
                )
        )
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException::class)
    protected fun handleMethodArgumentTypeMismatch(ex: MethodArgumentTypeMismatchException,
                                                   request: WebRequest?): ResponseEntity<Any> {
        return buildResponseEntity(
                ApiError(
                        status          = HttpStatus.BAD_REQUEST,
                        message         = String.format("The parameter '%s' of value '%s' could not be converted to type '%s'", ex.name, ex.value, ex.requiredType?.simpleName),
                        debugMessage    = ex.localizedMessage
                )
        )
    }

    private fun buildResponseEntity(apiError: ApiError): ResponseEntity<Any> {
        return ResponseEntity(apiError, apiError.status)
    }
}