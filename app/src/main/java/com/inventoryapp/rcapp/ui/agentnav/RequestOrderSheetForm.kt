package com.inventoryapp.rcapp.ui.agentnav

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun RequestOrderSheetForm(){
    Column {
        Text(modifier = Modifier.align(Alignment.CenterHorizontally),
            text = "Permintaan Pesanan",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Medium)

        )
        Card (
            modifier = Modifier.padding(horizontal = 15.dp, vertical = 20.dp),
            elevation = CardDefaults.cardElevation(5.dp),
            colors = CardColors(
                MaterialTheme.colorScheme.surfaceContainerLowest,
                MaterialTheme.colorScheme.onSurface,
                MaterialTheme.colorScheme.surfaceContainer,
                MaterialTheme.colorScheme.onSurfaceVariant)
        ){
            var qtyOrder by remember { mutableStateOf("") }
            var totalPriceProduct by remember { mutableStateOf("") }
            var isQtyEmpty = true
            Column (
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Text(
                    modifier = Modifier.padding(top=10.dp, start = 20.dp).align(Alignment.Start),
                    text = "Krupuk Sate",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Medium)
                )
                OutlinedTextField(
                    modifier = Modifier.padding(top = 5.dp),
                    value = qtyOrder,
                    onValueChange = {
                        qtyOrder = it
                        isQtyEmpty = it.isEmpty()
                    },
                    isError = isQtyEmpty,
                    maxLines = 1,
                    label = {
                        Text(text = "Jumlah barang")
                    },
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.None,
                        autoCorrect = false,
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Done
                    ),
                    placeholder = {
                        Text(text = "100")
                    },
                    trailingIcon = {
                        if (isQtyEmpty) {
                            Icon(Icons.Outlined.Info, contentDescription = "isi dahulu")
                        } else{
                            Icon(imageVector = Icons.Outlined.Done, contentDescription ="done" )
                        }
                    }
                )
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 15.dp, start = 20.dp, end = 20.dp, bottom = 15.dp)
                        .height(30.dp)
                        .background(
                            MaterialTheme.colorScheme.primary,
                            RoundedCornerShape(5.dp)
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    Text(
                        modifier = Modifier.padding(horizontal = 18.dp),
                        text = "Total",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.surfaceContainerLowest
                        )
                    )
                    Text(
                        modifier = Modifier.padding(horizontal = 18.dp),
                        text = "Rp110,000",
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.surfaceContainerLowest
                        )
                    )
                }
            }
        }

    }
}

@Preview(apiLevel = 33)
@Composable
fun PrevReqOrderSheet(){
    RequestOrderSheetForm()
}