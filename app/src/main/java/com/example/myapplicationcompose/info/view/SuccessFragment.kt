package com.example.myapplicationcompose.info.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.myapplicationcompose.R
import com.example.myapplicationcompose.info.view_model.ContactsViewModel
import com.example.myapplicationcompose.info.view_model.DataViewModel
import com.example.myapplicationcompose.info.view_model.TransferViewModel
import com.example.myapplicationcompose.ui.components.padZero
import com.example.myapplicationcompose.ui.theme.BoldSm
import com.example.myapplicationcompose.ui.theme.BoldSt
import com.example.myapplicationcompose.ui.theme.BookSm
import com.example.myapplicationcompose.ui.theme.MyApplicationComposeTheme
import java.util.*

class SuccessFragment : Fragment() {
    private val dataViewModel by activityViewModels<DataViewModel>()
    private val transferViewModel by activityViewModels<TransferViewModel>()
    private val contactsViewModel by activityViewModels<ContactsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        activity?.onBackPressedDispatcher?.addCallback(object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                activity?.finish()
            }
        })

        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)

            setContent {
                MyApplicationComposeTheme {
                    LayoutContent()
                }
            }
        }
    }

    @Composable
    @Preview(device = Devices.NEXUS_5, showBackground = true)
    private fun DefaultPreview() {
        LayoutContent()
    }

    @Composable
    private fun LayoutContent() {
        val data = dataViewModel.contactsLiveData.value

        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
                .padding(bottom = 24.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.ic_success),
                contentDescription = null,
                modifier = Modifier
                    .align(CenterHorizontally)
                    .padding(top = 48.dp)
            )

            Text(
                text = stringResource(R.string.label_transfer_performed),
                style = BoldSt.copy(
                    color = colorResource(R.color.primary_text_2),
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp)
            )

            val calendar = Calendar.getInstance()
            val day = calendar[Calendar.DAY_OF_MONTH].padZero(2)
            val month = calendar[Calendar.MONTH].padZero(2)
            val year = calendar[Calendar.YEAR].padZero(4)
            val hour = calendar[Calendar.HOUR_OF_DAY].padZero(2)
            val min = calendar[Calendar.MINUTE].padZero(2)

            val rowModifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
            val textNormalModifier = Modifier
                .weight(0.5f)
                .padding(end = 16.dp)
            val textBoldModifier = Modifier.weight(0.5f)
            val textNormalStyle = BookSm.copy(
                color = colorResource(R.color.secondary_text)
            )
            val textBoldStyle = BoldSm.copy(
                color = colorResource(R.color.primary_text_2),
                textAlign = TextAlign.End
            )

            Text(
                text = stringResource(
                    R.string.label_date_and_time_value,
                    day, month, year, hour, min
                ),
                style = textNormalStyle.copy(textAlign = TextAlign.Center),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            )

            Row(modifier = rowModifier.padding(top = 8.dp)) {
                Text(
                    text = stringResource(R.string.label_transaction_identifier_code),
                    style = textNormalStyle,
                    modifier = textNormalModifier
                )

                Text(
                    text = UUID.randomUUID().toString(),
                    style = textBoldStyle,
                    modifier = textBoldModifier
                )
            }

            Row(modifier = rowModifier) {
                Text(
                    text = stringResource(R.string.label_transfer_amount),
                    style = textNormalStyle,
                    modifier = textNormalModifier
                )

                Text(
                    text = "R$ " + transferViewModel.amount.value,
                    style = textBoldStyle,
                    modifier = textBoldModifier
                )
            }

            Row(modifier = rowModifier) {
                Text(
                    text = stringResource(R.string.label_payer_name),
                    style = textNormalStyle,
                    modifier = textNormalModifier
                )

                Text(text = contactsViewModel.selectedContact.value.run {
                    substring(0, indexOf(','))
                }, style = textBoldStyle, modifier = textBoldModifier)
            }

            Divider(modifier = Modifier.padding(top = 24.dp))

            Row(modifier = rowModifier.padding(top = 8.dp)) {
                Text(
                    text = stringResource(R.string.label_receiver_name),
                    style = textNormalStyle,
                    modifier = textNormalModifier
                )

                Text(
                    text = data?.fullName ?: "",
                    style = textBoldStyle,
                    modifier = textBoldModifier
                )
            }

            Row(modifier = rowModifier) {
                Text(
                    text = stringResource(R.string.label_agency),
                    style = textNormalStyle,
                    modifier = textNormalModifier
                )

                Text(
                    text = data?.accountData?.agency?.padZero(4) ?: "",
                    style = textBoldStyle,
                    modifier = textBoldModifier
                )
            }

            Row(modifier = rowModifier) {
                Text(
                    text = stringResource(R.string.label_account),
                    style = textNormalStyle,
                    modifier = textNormalModifier
                )

                Text(
                    text = data?.accountData?.account?.padZero(5) ?: "",
                    style = textBoldStyle,
                    modifier = textBoldModifier
                )
            }

        }
    }
}