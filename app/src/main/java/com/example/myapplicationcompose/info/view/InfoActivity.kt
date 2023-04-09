package com.example.myapplicationcompose.info.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.myapplicationcompose.R
import com.example.myapplicationcompose.databinding.ActivityInfoBinding
import com.example.myapplicationcompose.ui.components.AppComponents
import com.example.myapplicationcompose.ui.theme.*

class InfoActivity : AppCompatActivity() {
    private val viewBinding by lazy { ActivityInfoBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)

        viewBinding.composeView.setContent {
            MyApplicationComposeTheme {
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
                        onClick = {},
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(text = "Confirmar")
                    }
                }


            }
        }
    }

    @Preview(showBackground = true, showSystemUi = true, device = Devices.NEXUS_5)
    @Composable
    fun MyPreview() {
        MyApplicationComposeTheme {
            Column(modifier = Modifier.padding(16.dp)) {
                BalanceCard()
                Form()
            }
        }
    }

    @Composable
    fun BalanceCard() {
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
    fun Form() {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            AppComponents.AppInput(
                title = "Valor da transferência",
                placeholder = "Digite a quantidade",
                trailingIcon = R.drawable.ic_launcher_foreground
            )
            AppComponents.AppInputSelectable(
                title = "Transferir para",
                placeholder = "Toque para selecionar o contato",
                trailingIcon = R.drawable.ic_launcher_foreground
            )
            AppComponents.AppInputSelectable(
                title = "Transferir em",
                placeholder = "Toque para escolher a data",
                trailingIcon = R.drawable.ic_launcher_foreground
            )
        }
    }

    @Composable
    fun ProductRow(productName: String, productPrice: Double) {

        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            Image(
                painter = painterResource(id = R.drawable.churrasqueira),
                contentDescription = "Imagem do Produto",
                modifier = Modifier
                    .size(Dp(48f))
                    .padding(Dp(4f))
            )

            Column(Modifier.padding(start = 8.dp)) {
                Text(
                    text = productName,
                    modifier = Modifier.fillMaxWidth(),
                    style = MediumSt,
                    color = colorResource(id = R.color.black)
                )
                Text(
                    text = "R$ $productPrice",
                    modifier = Modifier.fillMaxWidth(),
                    style = BookSt,
                    color = colorResource(id = R.color.primary_text)
                )
            }
        }

    }

}