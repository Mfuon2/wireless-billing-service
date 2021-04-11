package com.softel.mpesa.util

import com.softel.mpesa.config.gson
import com.softel.mpesa.remote.mpesa.MpesaErrorResponse

object ResultFactory {
    fun <T> getSuccessResult(data: T): Result<T> {
        return Result(success = true, data = data)
    }

    fun <T> getSuccessResult(data: T, msg: String?): Result<T> {
        return Result(success = true, data = data, msg = msg)
    }

    fun <T> getSuccessResult(msg: String?): Result<T> {
        return Result(success = true, msg = msg)
    }

    fun <T> getFailResult(msg: String?): Result<T> {
        return Result(success = false, msg = msg)
    }

    fun <T> getFailResult(data: T, msg: String?): Result<T> {
        return Result(success = false, data = data, msg = msg)
    }

    fun <T> getApiResponse(response: String?, classOfT: Class<T>): Result<T> {
        return when(response.isNullOrBlank()){
            true -> getFailResult(msg = "Request Failed")
            false -> {
                if (response.contains("errorMessage")) {
                    val errorResponse = gson.fromJson(response, MpesaErrorResponse::class.java)
                    getFailResult(msg = errorResponse.errorMessage)
                } else {
                    val result = gson.fromJson(response, classOfT)
                    getSuccessResult(data = result)
                }
            }
        }
    }
}