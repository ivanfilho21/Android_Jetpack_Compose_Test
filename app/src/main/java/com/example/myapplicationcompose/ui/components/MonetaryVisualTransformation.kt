package com.example.myapplicationcompose.ui.components

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import com.example.myapplicationcompose.utils.MonetaryFormatHelper

class MonetaryVisualTransformation : VisualTransformation {
    private fun offsetTranslator(formatted: String) = object : OffsetMapping {
        override fun originalToTransformed(offset: Int): Int {
            return if (offset <= 0) 0 else formatted.length
        }

        override fun transformedToOriginal(offset: Int): Int {
            return offset
        }
    }

    override fun filter(text: AnnotatedString): TransformedText {
        val formatted = text.text.toCurrency()
        return TransformedText(AnnotatedString(formatted), offsetTranslator(formatted))
    }

    private fun String.toCurrency(): String {
        if (isEmpty()) return ""
        return MonetaryFormatHelper.format(toDouble() / 100)
    }
}