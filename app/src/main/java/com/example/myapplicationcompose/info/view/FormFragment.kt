package com.example.myapplicationcompose.info.view

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.myapplicationcompose.R
import com.example.myapplicationcompose.ui.components.InputComponents
import com.example.myapplicationcompose.ui.theme.*
import java.util.*

class FormFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                MyApplicationComposeTheme {
                    FormLayout()
                }
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
                enabled = false, // TODO: Rever
                onClick = {
                    // TODO: ir para a tela de Comprovante
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

    @Composable
    private fun BalanceCard() {
        val balance = "R$ 999.999,00"
        val today = "00/00/0000"

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
            InputComponents.AppInput(
                title = "Valor da transferência",
                placeholder = "Digite a quantidade",
                trailingIcon = R.drawable.ic_baseline_attach_money_24,
                inputType = InputComponents.InputType(KeyboardType.Number),
            )

            var contactName: String by remember { mutableStateOf("") }
            InputComponents.AppInputSelectable(
                title = "Transferir para",
                text = contactName,
                placeholder = "Toque para selecionar o contato",
                trailingIcon = R.drawable.ic_baseline_account_circle_24
            ) {
                findNavController().navigate(R.id.action_formFragment_to_contactsFragment)
                contactName = "Fulano de Tal"
            }

            val checkedState: MutableState<Boolean> = remember { mutableStateOf(false) }
            Row(
                modifier = Modifier.padding(top = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Agendar transferência",
                    style = BookSm.copy(color = colorResource(id = R.color.primary_text)),
                    modifier = Modifier.weight(1f)
                )
                Switch(
                    checked = checkedState.value,
                    onCheckedChange = { checkedState.value = it }
                )
            }

            var transferDate: String by remember { mutableStateOf("") }
            var calendar: Calendar by remember { mutableStateOf(Calendar.getInstance()) }

            InputComponents.AppInputSelectable(
                visible = checkedState.value,
                title = "Transferir em",
                text = transferDate,
                placeholder = "Toque para escolher a data",
                trailingIcon = R.drawable.ic_baseline_today_24
            ) {
                DatePickerDialog(
                    context,
                    { _: DatePicker, y: Int, m: Int, d: Int ->
                        calendar = Calendar.getInstance().apply {
                            set(Calendar.YEAR, y)
                            set(Calendar.MONTH, m)
                            set(Calendar.DAY_OF_MONTH, d)
                        }
                        transferDate =
                            "${d.padZero(2)}/${(m + 1).padZero(2)}/${y.padZero(4)}"
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                ).show()
            }
        }
    }

}

private fun Int?.padZero(length: Int) =
    this?.toString()?.padStart(length, '0') ?: "0".repeat(length)
