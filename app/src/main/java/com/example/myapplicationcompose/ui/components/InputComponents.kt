package com.example.myapplicationcompose.ui.components

import android.os.Handler
import android.os.Looper
import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.progressSemantics
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.CircularProgressIndicator
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.myapplicationcompose.R
import com.example.myapplicationcompose.ui.theme.BoldSm
import com.example.myapplicationcompose.ui.theme.BookSm
import com.example.myapplicationcompose.ui.theme.BookSt

object InputComponents {
    data class InputOptions(
        val keyboardType: KeyboardType = KeyboardType.Text,
        val validator: InputValidator? = null,
        val validatorDelay: Long = 1000,
        val pattern: Regex? = null,
        val visualTransformation: VisualTransformation = VisualTransformation.None
    )

    data class InputColors(
        val icon: Color,
        val indicator: Color,
        val stroke: Color,
        val placeholder: Color,
        val text: Color,
        val title: Color,
        val error: Color
    )

    @Composable
    fun inputDefaultColors() = InputColors(
        icon = colorResource(id = R.color.light_green),
        indicator = colorResource(id = R.color.indicator_color),
        stroke = colorResource(id = R.color.indicator_color),
        placeholder = colorResource(id = R.color.placeholder),
        text = colorResource(id = R.color.secondary_text),
        title = colorResource(id = R.color.primary_text),
        error = colorResource(id = R.color.dark_red)
    )

    @Composable
    fun Loading() {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator(
                modifier = Modifier
                    .progressSemantics()
                    .size(32.dp),
                strokeWidth = 3.dp
            )
        }
    }

    @Composable
    fun AppInputSelectable(
        title: String,
        text: String,
        placeholder: String = "",
        @DrawableRes trailingIcon: Int? = null,
        colors: InputColors = inputDefaultColors(),
        visible: Boolean = true,
        onClickCallback: () -> Unit
    ) {
        TitleAndComponent(title, colors.title, visible) {
            InputDecorationBox(text, placeholder, trailingIcon, {
                Text(text = text, style = BookSt.copy(color = colors.text))
            }, colors, onClickCallback)
        }
    }

    @Composable
    fun AppInput(
        title: String,
        text: MutableState<String>,
        placeholder: String = "",
        @DrawableRes trailingIcon: Int? = null,
        singleLine: Boolean = true,
        visible: Boolean = true,
        inputOptions: InputOptions = InputOptions(),
        colors: InputColors = inputDefaultColors()
    ) {
        TitleAndComponent(title, colors.title, visible) {
            var mutableText by remember { text }
            val mutableValidator by remember { mutableStateOf(inputOptions.validator) }
            var mutableHelperVisibility by remember { mutableStateOf(false) }

            // Basic Text Field
            BasicTextField(
                value = mutableText,
                onValueChange = { value ->
                    val filtered =
                        inputOptions.pattern?.let { value.replace(it, "") } ?: value

                    mutableValidator?.let { validator ->
                        mutableHelperVisibility = false

                        if (!validator.validate(filtered)) {
                            Handler(Looper.getMainLooper()).postDelayed({
                                mutableHelperVisibility = true
                            }, inputOptions.validatorDelay)
                        }
                    }

                    mutableText = filtered
                },
                visualTransformation = inputOptions.visualTransformation,
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = inputOptions.keyboardType),
                modifier = Modifier.padding(top = 8.dp),
                textStyle = BookSt.copy(color = colors.text),
                singleLine = singleLine,
                cursorBrush = SolidColor(colors.indicator),
                decorationBox = { innerTextField ->
                    InputDecorationBox(
                        mutableText,
                        placeholder,
                        trailingIcon,
                        innerTextField,
                        colors
                    )
                }
            )

            // Helper Text
            val helperText = mutableValidator?.errorMessage ?: ""

            if (!mutableHelperVisibility || helperText.isEmpty()) {
                return@TitleAndComponent
            }

            Text(
                text = helperText,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                style = BookSm.copy(color = colors.error)
            )
        }
    }

    @Composable
    private fun InputDecorationBox(
        text: String = "",
        placeholder: String = "",
        @DrawableRes trailingIcon: Int? = null,
        innerTextField: (@Composable () -> Unit),
        colors: InputColors,
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
                    color = colors.stroke,
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
                        style = BookSt.copy(color = colors.placeholder),
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
                    tint = colors.icon
                )
            }
        }
    }

    @Composable
    private fun TitleAndComponent(
        title: String,
        color: Color,
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
                style = BoldSm.copy(color = color),
            )

            // Component
            component()
        }
    }

}