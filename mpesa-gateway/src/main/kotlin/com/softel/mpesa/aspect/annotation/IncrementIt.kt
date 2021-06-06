package com.softel.mpesa.aspect.annotation

import java.lang.annotation.ElementType
import java.lang.annotation.Inherited
import kotlin.annotation.Retention
import kotlin.annotation.Target
import java.lang.annotation.RetentionPolicy

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class IncrementIt(
    val field : String = "count"
    )

