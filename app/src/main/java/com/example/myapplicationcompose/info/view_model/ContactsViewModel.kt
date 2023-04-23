package com.example.myapplicationcompose.info.view_model

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.myapplicationcompose.info.data.model.ContactsModel
import com.example.myapplicationcompose.info.data.repository.ContactsRepository

class ContactsViewModel : ViewModel() {
    private val repository = ContactsRepository()
    val contactsLiveData = mutableStateOf<ContactsModel.Main?>(null)
    val selectedContact = mutableStateOf<String>("")

    fun getContactsData(context: Context?) {
        contactsLiveData.value = repository.getContactsData(context)
    }
}