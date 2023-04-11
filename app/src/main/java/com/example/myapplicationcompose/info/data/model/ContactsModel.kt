package com.example.myapplicationcompose.info.data.model

import com.google.gson.annotations.SerializedName

object ContactsModel {
    data class Main(
        @SerializedName("saldo_atual") val balance: Double,
        @SerializedName("nome") val fullName: String,
        @SerializedName("meus_contatos") val myContacts: MyContacts
    )
    data class MyContacts(
        @SerializedName("qtd") val size: Int,
        @SerializedName("lista") val list: List<Contact>
    )
    data class Contact(
        @SerializedName("id") val id: Int,
        @SerializedName("nome_completo") val fullName: String,
        @SerializedName("dados_bancarios") val accountData: AccountData
    )
    data class AccountData(
        @SerializedName("agencia") val agency: Int,
        @SerializedName("conta_corrente") val account: Int
    )
}