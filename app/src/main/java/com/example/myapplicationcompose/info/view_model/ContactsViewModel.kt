package com.example.myapplicationcompose.info.view_model

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

class ContactsViewModel : ViewModel() {
    val selectedContact = mutableStateOf("")
}