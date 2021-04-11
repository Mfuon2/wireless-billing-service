package com.softel.mpesa.util

import java.time.format.DateTimeFormatter

object DateFormatter {
    fun mpesaDateTimeFormatter(): DateTimeFormatter =
            DateTimeFormatter.ofPattern("yyyyMMddHHmmss")
    fun statementDateTimeFormatter(): DateTimeFormatter =
            DateTimeFormatter.ofPattern("dd-MM-yyyy")
}