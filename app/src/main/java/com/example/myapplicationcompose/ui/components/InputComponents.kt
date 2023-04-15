package com.example.myapplicationcompose.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.myapplicationcompose.R
import com.example.myapplicationcompose.ui.theme.BoldSm
import com.example.myapplicationcompose.ui.theme.BookSt
import com.example.myapplicationcompose.utils.MonetaryFormatHelper

object InputComponents {
    data class InputType(
        val keyboardType: KeyboardType = KeyboardType.Text
    )

    class MonetaryTransformation : VisualTransformation {
        private val suffix = "R$ "

        override fun filter(text: AnnotatedString): TransformedText {
            val amount = MonetaryFormatHelper.format(text.text.toDouble() / 100)

            // TODO: continuar tratando

            return TransformedText(
                text = AnnotatedString(suffix + amount),
                offsetMapping = object : OffsetMapping {
                    override fun originalToTransformed(offset: Int): Int {
                        return offset + suffix.length
                    }

                    override fun transformedToOriginal(offset: Int): Int {
                        return offset - suffix.length
                    }
                }
            )
        }

    }

    @Composable
    fun AppInputSelectable(
        title: String,
        text: String,
        placeholder: String = "",
        @DrawableRes trailingIcon: Int? = null,
        indicatorColor: Color? = null,
        visible: Boolean = true,
        onClickCallback: () -> Unit
    ) {
        TitleAndComponent(title, visible) {
            val strokeColor = indicatorColor ?: colorResource(id = R.color.indicator_color)
            InputDecorationBox(text, placeholder, trailingIcon, strokeColor, {
                Text(
                    text = text,
                    style = BookSt.copy(color = colorResource(id = R.color.primary_text))
                )
            }, onClickCallback)
        }
    }

    @Composable
    fun AppInput(
        title: String,
        text: String = "",
        placeholder: String = "",
        @DrawableRes trailingIcon: Int? = null,
        indicatorColor: Color? = null,
        singleLine: Boolean = true,
        visible: Boolean = true,
        inputType: InputType = InputType()
    ) {
        TitleAndComponent(title, visible) {
            var inputText by remember { mutableStateOf(text) }
            val strokeColor = indicatorColor ?: colorResource(id = R.color.indicator_color)

            val visualTransformation =
                if (inputText.isEmpty()) VisualTransformation.None else MonetaryTransformation()

            // Basic Text Field
            BasicTextField(
                value = inputText,
                onValueChange = { value -> inputText = value },
                visualTransformation = visualTransformation,
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = inputType.keyboardType),
                modifier = Modifier.padding(top = 8.dp),
                textStyle = BookSt.copy(color = colorResource(id = R.color.primary_text)),
                singleLine = singleLine,
                cursorBrush = SolidColor(strokeColor),
                decorationBox = { innerTextField ->
                    InputDecorationBox(
                        inputText,
                        placeholder,
                        trailingIcon,
                        strokeColor,
                        innerTextField
                    )
                }
            )
        }
    }

    @Composable
    private fun InputDecorationBox(
        text: String = "",
        placeholder: String = "",
        @DrawableRes trailingIcon: Int? = null,
        strokeColor: Color,
        innerTextField: (@Composable () -> Unit),
        onClickCallback: (() -> Unit)? = null
    ) {
        var rowModifier = Modifier
            .fillMaxWidth()
            /*.border(
                width = 2.dp,
                color = strokeColor,
                shape = RoundedCornerShape(size = 16.dp)
            )*/
            .drawBehind {
                val borderSize = 1.dp.toPx()
                val width = size.width
                val height = size.height - borderSize / 2
                drawLine(
                    color = strokeColor,
                    start = Offset(0f, height),
                    end = Offset(width, height),
                    strokeWidth = borderSize
                )
            }
            .padding(horizontal = 0.dp, vertical = 2.dp)

        onClickCallback?.let { callback: () -> Unit ->
            rowModifier = rowModifier.clickable {
                callback()
            }
        }

        Row(
            modifier = rowModifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                if (text.isEmpty()) {
                    Text(
                        text = placeholder,
                        style = BookSt.copy(color = colorResource(id = R.color.placeholder)),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // Necessário para exibir o texto digitado
                innerTextField()
            }

            Spacer(modifier = Modifier.width(width = 4.dp))

            trailingIcon?.let { iconResId ->
                Icon(
                    painter = painterResource(id = iconResId),
                    contentDescription = "",
                    modifier = Modifier.size(24.dp),
                    tint = colorResource(id = R.color.light_green)
                )
            }
        }
    }

    @Composable
    private fun TitleAndComponent(
        title: String,
        visible: Boolean = true,
        component: (@Composable () -> Unit)
    ) {
        if (!visible) {
            return
        }

        Column(Modifier.fillMaxWidth()) {
            // Title
            Text(
                text = title,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp),
                style = BoldSm.copy(
                    color = colorResource(id = R.color.primary_text)
                ),
            )

            // Component
            component()
        }
    }

}