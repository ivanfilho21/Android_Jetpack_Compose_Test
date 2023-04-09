package com.example.myapplicationcompose.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
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
import androidx.compose.ui.unit.dp
import com.example.myapplicationcompose.R
import com.example.myapplicationcompose.ui.theme.BoldSm
import com.example.myapplicationcompose.ui.theme.BookSt

object AppComponents {

    @Composable
    fun AppInputSelectable(
        title: String,
        placeholder: String = "",
        @DrawableRes trailingIcon: Int? = null,
        indicatorColor: Color? = null
    ) {
        TitleAndComponent(title) {
            // TODO: deve permitir alterar este valor após o retorno de um callback
            var text: String by remember { mutableStateOf("") }
            val strokeColor = indicatorColor ?: colorResource(id = R.color.indicator_color)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
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
                    .padding(horizontal = 0.dp, vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    val textColorResId = if (text.isEmpty()) R.color.primary_text else R.color.placeholder
                    val displayText = text.ifEmpty { placeholder }
                    Text(
                        text = displayText,
                        style = BookSt.copy(color = colorResource(id = textColorResId)),
                        modifier = Modifier.fillMaxWidth()
                    )
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
    }

    @Composable
    fun AppInput(
        title: String,
        placeholder: String = "",
        @DrawableRes trailingIcon: Int? = null,
        indicatorColor: Color? = null,
        singleLine: Boolean = true
    ) {
        TitleAndComponent(title) {
            // Text Field
            var text: String by remember { mutableStateOf("") }
            val strokeColor = indicatorColor ?: colorResource(id = R.color.indicator_color)
            BasicTextField(
                value = text,
                onValueChange = { text = it },
                modifier = Modifier.padding(top = 8.dp),
                textStyle = BookSt.copy(color = colorResource(id = R.color.primary_text)),
                singleLine = singleLine,
                cursorBrush = SolidColor(strokeColor),
                decorationBox = { innerTextField ->
                    Row(
                        modifier = Modifier
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
                            .padding(horizontal = 0.dp, vertical = 2.dp),
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
            )
        }
    }

    @Composable
    private fun TitleAndComponent(
        title: String,
        component: (@Composable () -> Unit)
    ) {
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