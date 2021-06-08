package com.softel.mpesa.util

import java.time.format.DateTimeFormatter

object DateFormatter {
    fun mpesaDateTimeFormatter(): DateTimeFormatter =
            DateTimeFormatter.ofPattern("yyyyMMddHHmmss")
    fun simpleDateTimeFormatter(): DateTimeFormatter =
            DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm:ss")
    fun statementDateTimeFormatter(): DateTimeFormatter =
            DateTimeFormatter.ofPattern("dd-MM-yyyy")
}