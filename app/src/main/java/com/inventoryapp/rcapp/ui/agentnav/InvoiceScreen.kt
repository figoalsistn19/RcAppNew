package com.inventoryapp.rcapp.ui.agentnav

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.inventoryapp.rcapp.R
import com.inventoryapp.rcapp.data.model.SalesOrder

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun InvoiceScreen (
    invoice: SalesOrder
){
    val color = when (invoice.statusOrder) {
        "Pending" -> MaterialTheme.colorScheme.error
        "Lunas" -> MaterialTheme.colorScheme.primary
        "Selesai" -> Color.Green
        "DalamProses" -> MaterialTheme.colorScheme.tertiary
        "DalamPerjalanan" -> MaterialTheme.colorScheme.secondary
        else -> Color.Gray // Warna default untuk status yang tidak diketahui
    }
    val formattedPrice = String.format("Rp%,d", invoice.totalPrice)
    Scaffold {
        contentColorFor(backgroundColor = Color.White)
        Column (verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally)
        {
            Text(
                text = "Invoice / Nota",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Medium))

            Column (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 6.dp, vertical = 10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    Text(modifier = Modifier.padding(horizontal = 10.dp), text = "Figo Alsistani", style = MaterialTheme.typography.headlineSmall)
                    Spacer(modifier = Modifier.height(8.dp))
                }
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                )
                {
                    Text(text = "IDX-093-323", style = MaterialTheme.typography.titleMedium)
                    Text(
                        modifier = Modifier
                            .background(Color.Green, CircleShape)
                            .padding(horizontal = 7.dp),
                        text = "Dalam Perjalanan",
                        style = MaterialTheme.typography.titleSmall.copy(color = Color.White, fontWeight = FontWeight.Normal))
                }
            }
            Card (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ){
                Column {
                    Text(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 12.dp),
                        text = "Jumlah tagihan",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontStyle = FontStyle.Italic,
                            fontWeight = FontWeight.Normal,
                            color = MaterialTheme.colorScheme.outline
                        )
                    )
                    Text(modifier = Modifier.padding(horizontal = 10.dp),
                    text = "Rp100,000",
                    style = MaterialTheme.typography.titleLarge.copy
                        (
                        fontStyle = FontStyle.Italic,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                                )
                    )
                    Divider(
                        modifier = Modifier.padding(vertical = 15.dp)
                    )
                    Column (
                        modifier = Modifier.padding(bottom = 6.dp)
                    ) {
                        Row (
                            modifier = Modifier
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ){
                            Text(
                                modifier = Modifier.padding(horizontal = 10.dp),
                                text = "Dipesan pada",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontStyle = FontStyle.Italic,
                                    fontWeight = FontWeight.Normal,
                                    color = MaterialTheme.colorScheme.outline
                                )
                            )
                            Text(
                                modifier = Modifier.padding(horizontal = 10.dp),
                                text = "23:14・23 Desember 2023",
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            )
                        }
                        Row (
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 10.dp, vertical = 10.dp)
                        ){
                            Image(
                                modifier = Modifier
                                    .size(60.dp)
                                    .background(
                                        Color.White,
                                        CircleShape
                                    ),
                                imageVector = ImageVector.vectorResource(id = R.drawable.rc_logo),
                                contentDescription = "profile" )
                            Column (
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(60.dp),
                                verticalArrangement = Arrangement.SpaceEvenly
                            ){
                                Text(
                                    modifier = Modifier.padding(horizontal = 10.dp),
                                    text = "Figo Alsistani",
                                    style = MaterialTheme.typography.headlineSmall.copy(
                                        fontStyle = FontStyle.Normal,
                                        fontWeight = FontWeight.Normal,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                )
                                Text(modifier = Modifier.padding(horizontal = 10.dp),
                                    text = "figoals@gmail.com",
                                    style = MaterialTheme.typography.labelLarge.copy
                                        (
                                        fontStyle = FontStyle.Italic,
                                        fontWeight = FontWeight.Normal,
                                        color = MaterialTheme.colorScheme.secondary
                                    )
                                )
                            }
                        }
                    }
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 14.dp, start = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Text(
                    modifier = Modifier.padding(horizontal = 1.dp, vertical = 5.dp), text = "Detail Item",
                    style = MaterialTheme.typography.labelMedium.copy(color = MaterialTheme.colorScheme.outline))
                Spacer(modifier = Modifier.height(8.dp))
            }
            Card (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
            ){
                Column (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding()
                ){
                    LazyColumn(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp)
                    ){
                        items(3){
                            Column {
                                Row (
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ){
                                    Text(
                                        text = "Roti Tiro ",
                                        modifier = Modifier.padding(horizontal = 5.dp)
                                    )
                                    Text(
                                        text = "Rp100,000",
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            fontWeight = FontWeight.Medium,
                                            color = MaterialTheme.colorScheme.onSurface
                                        ),
                                        modifier = Modifier.padding(horizontal = 5.dp)
                                    )
                                }
                                Row {
                                    Text(
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 1.dp),
                                        text = "5 x Rp10,000",
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            fontStyle = FontStyle.Italic,
                                            fontWeight = FontWeight.Normal,
                                            color = MaterialTheme.colorScheme.outline
                                        )
                                    )
                                }
                            }
                        }
                    }
                    Divider(
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                    Row (
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 3.dp, horizontal = 5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ){
                        Text(
                            modifier = Modifier.padding(horizontal = 10.dp),
                            text = "Subtotal",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontStyle = FontStyle.Italic,
                                fontWeight = FontWeight.Normal,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                        Text(
                            modifier = Modifier.padding(horizontal = 10.dp),
                            text = "Rp100,000",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        )
                    }
                    Row (
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 1.dp, horizontal = 5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ){
                        Text(
                            modifier = Modifier.padding(horizontal = 10.dp),
                            text = "Pajak (10%)",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontStyle = FontStyle.Italic,
                                fontWeight = FontWeight.Normal,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                        Text(
                            modifier = Modifier.padding(horizontal = 10.dp),
                            text = "Rp10,000",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        )
                    }
                    Row (
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                            .height(30.dp)
                            .background(
                                MaterialTheme.colorScheme.primary,
                                RoundedCornerShape(0.dp, 0.dp, 12.dp, 12.dp)
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
            IconButton(
                modifier = Modifier.padding(vertical = 15.dp).background(MaterialTheme.colorScheme.primaryContainer, CircleShape).width(160.dp).height(40.dp),
                onClick = { /*TODO*/ }
            )
            {
                Row (
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = "tombol simpan")
                    Text(text = "Simpan Invoice")
                }

            }
        }
    }
}

@Preview(apiLevel = 33)
@Composable
fun PrevInvoiceScreen(){
//    InvoiceScreen(invoice = reqOrders[1])
}