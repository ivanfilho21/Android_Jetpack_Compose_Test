package com.example.myapplicationcompose.info.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.myapplicationcompose.R
import com.example.myapplicationcompose.ui.components.InputComponents
import com.example.myapplicationcompose.ui.theme.BookSm
import com.example.myapplicationcompose.ui.theme.MediumLg
import com.example.myapplicationcompose.ui.theme.MyApplicationComposeTheme

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
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
                onClick = {
                    // TODO: ir para a tela de Comprovante
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(text = "Confirmar")
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
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            InputComponents.AppInput(
                title = "Valor da transferência",
                placeholder = "Digite a quantidade",
                trailingIcon = R.drawable.ic_launcher_foreground
            )

            var contactName: String by remember { mutableStateOf("") }
            InputComponents.AppInputSelectable(
                title = "Transferir para",
                text = contactName,
                placeholder = "Toque para selecionar o contato",
                trailingIcon = R.drawable.ic_launcher_foreground
            ) {
                //findNavController().navigate(R.id.action_formFragment_to_contactsFragment)
                contactName = "Fulano de Tal"
            }

            var transferDate: String by remember { mutableStateOf("") }
            InputComponents.AppInputSelectable(
                title = "Transferir em",
                text = transferDate,
                placeholder = "Toque para escolher a data",
                trailingIcon = R.drawable.ic_launcher_foreground
            ) {
                // TODO: abrir DatePicker

                transferDate = "09/04/2023"
                Toast.makeText(
                    context,
                    "Abrir date picker: $transferDate",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

}