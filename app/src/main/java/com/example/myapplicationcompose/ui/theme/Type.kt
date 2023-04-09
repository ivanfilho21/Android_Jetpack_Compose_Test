package com.example.myapplicationcompose.ui.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.myapplicationcompose.R

private val DefaultStyle = TextStyle(
    fontStyle = FontStyle.Normal,
    fontWeight = FontWeight.Normal,
    textAlign = TextAlign.Start
)

private val Book = DefaultStyle.copy(
    fontFamily = FontFamily(Font(R.font.poppins_regular)),
)

private val Medium = DefaultStyle.copy(
    fontStyle = FontStyle.Normal,
    fontWeight = FontWeight.Normal,
    fontFamily = FontFamily(Font(R.font.poppins_medium))
)

private val Bold = DefaultStyle.copy(
    fontStyle = FontStyle.Normal,
    fontWeight = FontWeight.Normal,
    fontFamily = FontFamily(Font(R.font.poppins_bold))
)

val BookSm = Book.copy(
    fontSize = 14.sp,
    lineHeight = 16.sp
)

val BookSt = Book.copy(
    fontSize = 16.sp,
    lineHeight = 18.sp
)

val MediumSt = Medium.copy(
    fontSize = 16.sp,
    lineHeight = 18.sp
)

val MediumLg = Medium.copy(
    fontSize = 18.sp,
    lineHeight = 20.sp
)

val BoldSm = Bold.copy(
    fontSize = 14.sp,
    lineHeight = 16.sp
)