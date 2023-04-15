package com.example.myapplicationcompose.utils

import java.text.NumberFormat
import java.util.*

object MonetaryFormatHelper {
    private val locale = Locale("pt", "BR")

    fun format(value: Double?): String {
        val nf = NumberFormat.getNumberInstance(locale).apply {
            minimumFractionDigits = 2
            maximumFractionDigits = 2
        }
        return nf.format(value ?: 0.0)
    }
}