package com.example.myapplicationcompose.info.view_model

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.myapplicationcompose.ui.components.InputValidator

class TransferViewModel: ViewModel() {
    var amountValidator: InputValidator? = null

    val amount = mutableStateOf("")
    val scheduleState = mutableStateOf(false)
    val transferDate = mutableStateOf("")
}