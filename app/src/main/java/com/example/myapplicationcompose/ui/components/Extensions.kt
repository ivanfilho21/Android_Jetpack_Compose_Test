package com.example.myapplicationcompose.ui.components

fun Int?.padZero(length: Int) =
    this?.toString()?.padStart(length, '0') ?: "0".repeat(length)