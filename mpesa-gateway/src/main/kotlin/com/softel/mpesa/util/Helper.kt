package com.softel.mpesa.util

import com.aventrix.jnanoid.jnanoid.NanoIdUtils
import java.util.*

object Helper {
    fun encodeToBase64String(bytes: ByteArray): String =
            Base64.getEncoder().encodeToString(bytes)

    fun uniqueID(): String {
        return NanoIdUtils.randomNanoId()
    }

}