package com.softel.mpesa.util

import java.nio.charset.Charset

fun String.phoneToLong(): Long =
        this.replace(Regex("[^0-9]"), "").toLong()

fun String.toMpesaByteArray(): ByteArray =
        this.toByteArray(Charset.forName("ISO-8859-1"))