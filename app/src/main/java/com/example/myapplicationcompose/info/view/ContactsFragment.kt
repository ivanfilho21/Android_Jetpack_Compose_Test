package com.example.myapplicationcompose.info.view

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.progressSemantics
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.myapplicationcompose.R
import com.example.myapplicationcompose.info.data.model.ContactsModel
import com.example.myapplicationcompose.info.view_model.ContactsViewModel
import com.example.myapplicationcompose.ui.components.padZero
import com.example.myapplicationcompose.ui.theme.BoldSt
import com.example.myapplicationcompose.ui.theme.BookSt
import java.util.*

class ContactsFragment : Fragment() {
    private val viewModel by activityViewModels<ContactsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                val contactsData = viewModel.contactsLiveData.value
                ContactList(contactsData)

                Handler(Looper.getMainLooper()).postDelayed({
                    viewModel.getContactsData(context)
                }, 1000)
            }
        }
    }

    @Composable
    private fun ContactList(data: ContactsModel.Main?) {
        if (data == null) {
            return Loading()
        }

        data.myContacts.list.let { contacts: List<ContactsModel.Contact> ->
            LazyColumn {
                items(count = contacts.size, itemContent = { index ->
                    ContactRow(contacts[index]) { contact ->
                        val name = contact.fullName.split(' ').run {
                            (firstOrNull() ?: "") + " " + (lastOrNull() ?: "")
                        }
                        val agency = contact.accountData.agency.padZero(4)
                        val account = contact.accountData.account.padZero(5)
                        val contactInfo = "$name, Ag. $agency, Cc. $account"
                        viewModel.selectedContact.value = contactInfo

                        activity?.onBackPressed()
                    }
                    Divider()
                })
            }
        }
    }

    @Composable
    private fun ContactRow(
        contact: ContactsModel.Contact,
        onClick: (ContactsModel.Contact) -> Unit
    ) {
        val accountData = contact.accountData
        val nameWords = contact.fullName.split(' ').map { it.substring(0, 1) }
        val randomDarkColor = Random(System.currentTimeMillis()).run {
            val bound = 180
            android.graphics.Color.argb(255, nextInt(bound), nextInt(bound), nextInt(bound))
        }
        Row(
            verticalAlignment = CenterVertically,
            modifier = Modifier.clickable {
                onClick(contact)
            }
        ) {
            Box(
                contentAlignment = Center,
                modifier = Modifier
                    .padding(top = 8.dp, start = 8.dp, bottom = 8.dp)
                    .background(
                        Color(randomDarkColor),
                        RoundedCornerShape(50)
                    )
                    .size(48.dp)
            ) {
                Text(
                    text = nameWords.run { (firstOrNull() ?: "") + (lastOrNull() ?: "") },
                    style = BookSt.copy(color = colorResource(id = R.color.white))
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 8.dp)
            ) {
                Text(
                    text = contact.fullName,
                    style = BoldSt.copy(color = colorResource(id = R.color.primary_text))
                )

                Row {
                    Text(
                        text = "Ag. " + accountData.agency.padZero(4),
                        style = BookSt.copy(color = colorResource(id = R.color.primary_text))
                    )

                    Text(
                        text = "Cc. " + accountData.account.padZero(5),
                        style = BookSt.copy(color = colorResource(id = R.color.primary_text)),
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
        }
    }

    @Composable
    private fun Loading() {
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

}