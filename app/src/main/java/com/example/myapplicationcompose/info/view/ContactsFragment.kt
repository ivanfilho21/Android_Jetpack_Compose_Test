package com.example.myapplicationcompose.info.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.myapplicationcompose.info.data.model.ContactsModel
import com.example.myapplicationcompose.info.view_model.ContactsViewModel
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue

class ContactsFragment : Fragment() {
    private val viewModel by activityViewModels<ContactsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel.getContactsData(context)

        return ComposeView(requireContext()).apply {
            setContent {
                val contactsData = viewModel.contactsLiveData.value
                ContactList(contactsData)
            }
        }
    }

    @Composable
    private fun ContactList(data: ContactsModel.Main?) {
        data?.myContacts?.list?.let { contacts ->
            LazyColumn {
                items(count = contacts.size, itemContent = { index ->
                    Text("${contacts[index]}")
                })
            }
        } ?: run {
            // Loading...
        }
    }

}