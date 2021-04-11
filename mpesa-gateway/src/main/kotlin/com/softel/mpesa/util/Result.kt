package com.softel.mpesa.util

data class Result<T>(
        val success: Boolean,
        val msg: String? = null,
        val data: T? = null
)