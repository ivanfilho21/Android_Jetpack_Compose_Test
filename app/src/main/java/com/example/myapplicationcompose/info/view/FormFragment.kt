package com.example.myapplicationcompose.info.view

import android.app.DatePickerDialog
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.myapplicationcompose.R
import com.example.myapplicationcompose.info.view_model.ContactsViewModel
import com.example.myapplicationcompose.info.view_model.DataViewModel
import com.example.myapplicationcompose.info.view_model.TransferViewModel
import com.example.myapplicationcompose.ui.components.InputComponents
import com.example.myapplicationcompose.ui.components.InputValidator
import com.example.myapplicationcompose.ui.components.MonetaryVisualTransformation
import com.example.myapplicationcompose.ui.components.padZero
import com.example.myapplicationcompose.ui.theme.BoldSt
import com.example.myapplicationcompose.ui.theme.BookSm
import com.example.myapplicationcompose.ui.theme.MediumLg
import com.example.myapplicationcompose.ui.theme.MyApplicationComposeTheme
import com.example.myapplicationcompose.utils.MonetaryFormatHelper
import java.util.*

class FormFragment : Fragment() {
    private lateinit var actionButtonState: MutableState<Boolean>
    private val dataViewModel by activityViewModels<DataViewModel>()
    private val transferViewModel by activityViewModels<TransferViewModel>()
    private val contactsViewModel by activityViewModels<ContactsViewModel>()
    private var validAmount = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)

            setContent {
                MyApplicationComposeTheme {
                    actionButtonState = remember { mutableStateOf(false) }

                    FormLayout()
                }

                Handler(Looper.getMainLooper()).postDelayed({
                    dataViewModel.getContactsData(context)
                }, /*300*/0)
            }
        }
    }

    @Composable
    @Preview(device = Devices.NEXUS_5, showBackground = true)
    private fun DefaultPreview() {
        FormLayout()
    }

    @Composable
    private fun FormLayout() {
        if (dataViewModel.contactsLiveData.value == null) {
            InputComponents.Loading()
        } else {
            Column {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp)
                ) {
                    BalanceCard()
                    Form()
                }

                Button(
                    enabled = actionButtonState.value,
                    onClick = {
                        findNavController()
                            .navigate(R.id.action_formFragment_to_successFragment)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(24.dp),
                    contentPadding = PaddingValues(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = colorResource(id = R.color.light_green_2),
                        contentColor = colorResource(id = R.color.dark_green),
                        disabledBackgroundColor = colorResource(id = R.color.gray_light_primary),
                        disabledContentColor = colorResource(id = R.color.gray_dark_primary)
                    )
                ) {
                    Text(text = "Confirmar", style = BoldSt)
                }
            }
        }
    }

    @Composable
    private fun BalanceCard() {
        val data = dataViewModel.contactsLiveData.value

        val balance = MonetaryFormatHelper.format(data?.balance, true)
        val today = Calendar.getInstance().run {
            val day = get(Calendar.DAY_OF_MONTH).padZero(2)
            val month = get(Calendar.MONTH).padZero(2)
            val year = get(Calendar.YEAR).padZero(4)

            "$day/$month/$year"
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(3.dp, RoundedCornerShape(16.dp))
                .background(colorResource(id = R.color.white), RoundedCornerShape(16.dp))
                .padding(16.dp)
        ) {
            Text(
                text = "Meu saldo",
                modifier = Modifier.fillMaxWidth(),
                style = BookSm.copy(
                    textAlign = TextAlign.Center,
                    color = colorResource(id = R.color.primary_text)
                )
            )
            Text(
                text = balance,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                style = MediumLg.copy(
                    textAlign = TextAlign.Center,
                    color = colorResource(id = R.color.primary_text)
                )
            )
            Text(
                text = "Valor consultado em $today",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                style = BookSm.copy(
                    textAlign = TextAlign.Center,
                    color = colorResource(id = R.color.primary_text)
                )
            )
        }
    }

    @Composable
    private fun Form() {
        val context = LocalContext.current

        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            transferViewModel.amountValidator = InputValidator.Builder()
                .setRequired(true, "Campo obrigatório.")
                .addCustomValidation("Digite um valor maior que zero.") { text ->
                    val amount = text.toDouble() / 100
                    amount > 0.0
                }
                .addCustomValidation("O valor digitado é maior que o seu saldo.") { text ->
                    val amount = text.toDouble() / 100
                    val balance = dataViewModel.contactsLiveData.value?.balance ?: 0.0
                    amount <= balance
                }
                .setOnValidationCallback {
                    validAmount = it
                    validateButton()
                }
                .build()

            InputComponents.AppInput(
                title = stringResource(R.string.label_transfer_amount),
                text = transferViewModel.amount,
                placeholder = "Digite a quantidade",
                trailingIcon = R.drawable.ic_baseline_attach_money_24,
                inputOptions = InputComponents.InputOptions(
                    keyboardType = KeyboardType.NumberPassword,
                    visualTransformation = MonetaryVisualTransformation(),
                    validatorDelay = 0,
                    validator = transferViewModel.amountValidator,
                    pattern = "[^\\d]".toRegex()
                ),
            )

            InputComponents.AppInputSelectable(
                title = "Transferir para",
                text = contactsViewModel.selectedContact.value,
                placeholder = "Toque para selecionar o contato",
                trailingIcon = R.drawable.ic_baseline_account_circle_24
            ) {
                findNavController().navigate(R.id.action_formFragment_to_contactsFragment)
            }

            val checkedState: MutableState<Boolean> = remember { transferViewModel.scheduleState }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .toggleable(
                        value = checkedState.value,
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                        onValueChange = {
                            checkedState.value = it
                            validateButton()
                        }
                    )
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Agendar transferência",
                    style = BookSm.copy(color = colorResource(id = R.color.primary_text)),
                    modifier = Modifier.weight(1f)
                )

                Switch(
                    checked = checkedState.value,
                    onCheckedChange = null
                )
            }

            var calendar: Calendar by remember { mutableStateOf(Calendar.getInstance()) }
            var transferDate: String by remember { transferViewModel.transferDate }

            InputComponents.AppInputSelectable(
                visible = checkedState.value,
                title = "Transferir em",
                text = transferDate,
                placeholder = "Toque para escolher a data",
                trailingIcon = R.drawable.ic_baseline_today_24
            ) {
                val composableDatePicker = DatePickerDialog(
                    context,
                    { _: DatePicker, y: Int, m: Int, d: Int ->
                        calendar = Calendar.getInstance().apply {
                            set(Calendar.YEAR, y)
                            set(Calendar.MONTH, m)
                            set(Calendar.DAY_OF_MONTH, d)
                        }
                        transferDate =
                            "${d.padZero(2)}/${(m + 1).padZero(2)}/${y.padZero(4)}"

                        validateButton()
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                )
                composableDatePicker.datePicker.apply {
                    minDate = Calendar.getInstance().apply {
                        add(Calendar.DAY_OF_MONTH, 1)
                    }.timeInMillis
                }
                composableDatePicker.show()
            }
        }

        validateButton()
    }

    private fun validateButton() {
        transferViewModel.run {
            actionButtonState.value = validAmount &&
                    contactsViewModel.selectedContact.value.isNotEmpty() &&
                    (scheduleState.value && transferDate.value.isNotEmpty() || !scheduleState.value)
        }
    }

}
