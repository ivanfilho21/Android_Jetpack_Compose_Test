package com.example.myapplicationcompose.info.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.myapplicationcompose.R
import com.example.myapplicationcompose.info.data.model.ContactsModel
import com.example.myapplicationcompose.info.view_model.ContactsViewModel
import com.example.myapplicationcompose.info.view_model.DataViewModel
import com.example.myapplicationcompose.ui.components.InputComponents
import com.example.myapplicationcompose.ui.components.padZero
import com.example.myapplicationcompose.ui.theme.BoldSt
import com.example.myapplicationcompose.ui.theme.BookSt
import com.example.myapplicationcompose.ui.theme.MyApplicationComposeTheme
import java.util.*

class ContactsFragment : Fragment() {
    private val dataViewModel by activityViewModels<DataViewModel>()
    private val layoutViewModel by activityViewModels<ContactsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)

            setContent {
                val contactsData = dataViewModel.contactsLiveData.value

                MyApplicationComposeTheme {
                    ContactList(contactsData)
                }
            }
        }
    }

    @Composable
    private fun ContactList(data: ContactsModel.Main?) {
        if (data == null) {
            return InputComponents.Loading()
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
                        layoutViewModel.selectedContact.value = contactInfo

                        findNavController().navigateUp()
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
            modifier = Modifier.clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = rememberRipple(),
                onClick = { onClick(contact) }
            )
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

}