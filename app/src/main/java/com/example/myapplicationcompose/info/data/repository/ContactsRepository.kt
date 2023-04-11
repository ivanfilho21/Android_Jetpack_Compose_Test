package com.example.myapplicationcompose.info.data.repository

import android.content.Context
import com.example.myapplicationcompose.info.data.model.ContactsModel
import com.google.gson.Gson

class ContactsRepository {

    fun getContactsData(context: Context?): ContactsModel.Main {
        val jsonFile = context?.assets?.open("data.json")
        val json = jsonFile?.bufferedReader()?.use { it.readText() } ?: "{}"
        return Gson().fromJson(json, ContactsModel.Main::class.java)
    }
}