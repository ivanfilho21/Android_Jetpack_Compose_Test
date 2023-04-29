package com.example.myapplicationcompose.utils

import java.text.NumberFormat
import java.util.*

object MonetaryFormatHelper {
    private val locale = Locale("pt", "BR")

    fun format(value: Double?, shouldPrefix: Boolean = false): String {
        val nf = NumberFormat.getNumberInstance(locale).apply {
            minimumFractionDigits = 2
            maximumFractionDigits = 2
        }
        val prefix = if (shouldPrefix) "R$ " else ""
        return prefix + nf.format(value ?: 0.0)
    }
}