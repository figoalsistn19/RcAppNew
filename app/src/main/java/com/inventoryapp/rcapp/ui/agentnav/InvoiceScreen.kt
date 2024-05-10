package com.inventoryapp.rcapp.ui.agentnav

//noinspection UsingMaterialAndMaterial3Libraries
import android.annotation.SuppressLint
import android.os.Build
import android.widget.Toast
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.inventoryapp.rcapp.R
import com.inventoryapp.rcapp.data.model.SalesOrder
import com.inventoryapp.rcapp.data.model.StatusOrder
import com.inventoryapp.rcapp.ui.viewmodel.SalesOrderViewModel
import com.inventoryapp.rcapp.util.Resource
import java.text.SimpleDateFormat
import java.util.TimeZone


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "SimpleDateFormat", "NewApi")
@Composable
fun InvoiceScreen (
    invoice: SalesOrder
){
    val color = when (invoice.statusOrder.toString()) {
        "Pending" -> MaterialTheme.colorScheme.error
        "Lunas" -> MaterialTheme.colorScheme.primary
        "Selesai" -> MaterialTheme.colorScheme.outline
        "DalamProses" -> MaterialTheme.colorScheme.tertiary
        "DalamPerjalanan" -> MaterialTheme.colorScheme.secondary
        else -> Color.Gray // Warna default untuk status yang tidak diketahui
    }
    val sdf = SimpleDateFormat(" dd MMM yyyy ・ HH:mm z")
    val date = invoice.orderDate
    val timeZone = TimeZone.getTimeZone("Asia/Jakarta") // Atur zona waktu ke WIB
    sdf.timeZone = timeZone
    val fixDate = sdf.format(date!!)
    val formattedPrice = String.format("Rp%,d", invoice.totalPrice)

    val productList = invoice.productsItem
    var totalPriceCalculation = 0L
    for (item in productList!!){
        totalPriceCalculation += item.totalPrice ?: 0
    }
    val totalTax = totalPriceCalculation * invoice.tax!!/100

    val formattedSubtotalPrice = String.format("Rp%,d", totalPriceCalculation)
    val formattedTotalTax = String.format("Rp%,d", totalTax)
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
                    Text(
                        modifier = Modifier.padding(horizontal = 10.dp),
                        text = invoice.nameAgent!!,
                        style = MaterialTheme.typography.headlineSmall
                            .copy(fontWeight = FontWeight.SemiBold)
                    )
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
                    Text(
                        text = invoice.idOrder!!,
                        style = MaterialTheme.typography.titleMedium
                    )
                    Text(
                        modifier = Modifier
                            .background(Color.Green, CircleShape)
                            .padding(horizontal = 7.dp),
                        text = invoice.statusOrder!!.toString(),
                        style = MaterialTheme.typography.titleSmall.copy(color = color, fontWeight = FontWeight.Normal))
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
                    text = formattedPrice,
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
                                text = fixDate,
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
                                    text = invoice.nameAgent!!,
                                    style = MaterialTheme.typography.headlineSmall.copy(
                                        fontStyle = FontStyle.Normal,
                                        fontWeight = FontWeight.Normal,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                )
                                Text(modifier = Modifier.padding(horizontal = 10.dp),
                                    text = invoice.email!!,
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
                    val itemList = invoice.productsItem
                    LazyColumn(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp)
                    ){
                        items(itemList!!){
                            val formattedTotalPriceItems = String.format("Rp%,d", it.totalPrice)
                            val formattedItemPrice = String.format("Rp%,d", it.finalPrice)
                            Column {
                                Row (
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ){
                                    Text(
                                        text = it.productName!!,
                                        modifier = Modifier.padding(horizontal = 5.dp)
                                    )
                                    Text(
                                        text = formattedTotalPriceItems,
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
                                        text = it.quantity.toString() + " x " + formattedItemPrice,
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            fontStyle = FontStyle.Italic,
                                            fontWeight = FontWeight.Normal,
                                            color = MaterialTheme.colorScheme.outline
                                        )
                                    )
                                }
                            }
                        }
//                        items(itemList!!.size){item ->

//                        }
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
                            text = formattedSubtotalPrice,
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
                            text = "Pajak (${invoice.tax}%)",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontStyle = FontStyle.Italic,
                                fontWeight = FontWeight.Normal,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                        Text(
                            modifier = Modifier.padding(horizontal = 10.dp),
                            text = formattedTotalTax,
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
                            text = formattedPrice,
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.surfaceContainerLowest
                            )
                        )
                    }
                }
            }
            ScreenshotButton()
            WriteToMediaStoreButton()
//            IconButton(
//                modifier = Modifier.padding(vertical = 15.dp).background(MaterialTheme.colorScheme.primaryContainer, CircleShape).width(160.dp).height(40.dp),
//                onClick = { /*TODO*/ }
//            )
//            {
//                Row (
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Icon(imageVector = Icons.Default.Add, contentDescription = "tombol simpan")
//                    Text(text = "Simpan Invoice")
//                }
//
//            }
        }
    }
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "SimpleDateFormat")
@Composable
fun InvoiceScreenForInternal (
    invoice: SalesOrder,
    salesOrderViewModel: SalesOrderViewModel,
    onProcessClick: (String) -> Unit
){
    val modelResource = salesOrderViewModel.updateStatusOrderFlow.collectAsState()
    val context = LocalContext.current
    val color = when (invoice.statusOrder.toString()) {
        "Pending" -> MaterialTheme.colorScheme.error
        "Lunas" -> MaterialTheme.colorScheme.primary
        "Selesai" -> MaterialTheme.colorScheme.outline
        "DalamProses" -> MaterialTheme.colorScheme.tertiary
        "DalamPerjalanan" -> MaterialTheme.colorScheme.secondary
        else -> Color.Gray // Warna default untuk status yang tidak diketahui
    }
    val sdf = SimpleDateFormat(" dd MMM yyyy ・ HH:mm")
    val date = invoice.orderDate
    val timeZone = TimeZone.getTimeZone("Asia/Jakarta") // Atur zona waktu ke WIB
    sdf.timeZone = timeZone
    val fixDate = sdf.format(date!!)
    val formattedPrice = String.format("Rp%,d", invoice.totalPrice)

    val productList = invoice.productsItem
    var totalPriceCalculation = 0L
    for (item in productList!!){
        totalPriceCalculation += item.totalPrice ?: 0
    }
    val totalTax = totalPriceCalculation * invoice.tax!!/100

    val formattedSubtotalPrice = String.format("Rp%,d", totalPriceCalculation)
    val formattedTotalTax = String.format("Rp%,d", totalTax)
    val formattedTotalPriceItem = String.format("Rp%,d", productList[0].totalPrice)
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
                    Text(
                        modifier = Modifier.padding(horizontal = 10.dp),
                        text = invoice.nameAgent!!,
                        style = MaterialTheme.typography.headlineSmall
                            .copy(fontWeight = FontWeight.SemiBold)
                    )
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
                    Text(
                        text = invoice.idOrder!!,
                        style = MaterialTheme.typography.titleMedium
                    )
//                    Text(
//                        modifier = Modifier
//                            .background(Color.Green, CircleShape)
//                            .padding(horizontal = 7.dp),
//                        text = invoice.statusOrder!!.toString(),
//                        style = MaterialTheme.typography.titleSmall.copy(color = color, fontWeight = FontWeight.Normal))
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
                        text = formattedPrice,
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
                                text = fixDate,
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
                                    text = invoice.nameAgent!!,
                                    style = MaterialTheme.typography.headlineSmall.copy(
                                        fontStyle = FontStyle.Normal,
                                        fontWeight = FontWeight.Normal,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                )
                                Text(modifier = Modifier.padding(horizontal = 10.dp),
                                    text = invoice.email!!,
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
                    val itemList = invoice.productsItem
                    LazyColumn(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp)
                    ){
                        items(itemList!!){
                            val formattedTotalPriceItems = String.format("Rp%,d", it.totalPrice)
                            val formattedItemPrice = String.format("Rp%,d", it.finalPrice)
                            Column {
                                Row (
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ){
                                    Text(
                                        text = it.productName!!,
                                        modifier = Modifier.padding(horizontal = 5.dp)
                                    )
                                    Text(
                                        text = formattedTotalPriceItems,
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
                                        text = it.quantity.toString() + " x " + formattedItemPrice,
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
                            text = formattedSubtotalPrice,
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
                            text = "Pajak (${invoice.tax}%)",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontStyle = FontStyle.Italic,
                                fontWeight = FontWeight.Normal,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                        Text(
                            modifier = Modifier.padding(horizontal = 10.dp),
                            text = formattedTotalTax,
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
                            text = formattedPrice,
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.surfaceContainerLowest
                            )
                        )
                    }
                }
            }
//            if (invoice.statusOrder == StatusOrder.Pending){
//                 Button(onClick = {
//                     onProcessClick(invoice.idOrder!!)
//                     salesOrderViewModel.updateStatusOrder(invoice.idOrder!!,StatusOrder.DalamProses)
//                 }) {
//                     Text(text = "Proses pesanan")
//                 }
//            }
//            if (invoice.statusOrder == StatusOrder.DalamProses){
//                Button(onClick = {
//                    onProcessClick(invoice.idOrder!!)
//                    salesOrderViewModel.updateStatusOrder(invoice.idOrder!!,StatusOrder.DalamPerjalanan)
//                })
//                {
//                    Text(text = "Tandai pesanan telah dikirim")
//                }
//            }
//            if (invoice.statusOrder == StatusOrder.DalamPerjalanan){
//                Button(onClick = {
//                    onProcessClick(invoice.idOrder!!)
//                    salesOrderViewModel.updateStatusOrder(invoice.idOrder!!,StatusOrder.Lunas)
//                }) {
//                    Text(text = "Tandai pesanan telah lunas")
//                }
//            }
//            if (invoice.statusOrder == StatusOrder.Lunas){
//                Button(onClick = {
//                    onProcessClick(invoice.idOrder!!)
//                    salesOrderViewModel.updateStatusOrder(invoice.idOrder!!,StatusOrder.Selesai)
//                }) {
//                    Text(text = "Tandai pesanan telah selesai")
//                }
//                ScreenshotButton()
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//                    WriteToMediaStoreButton()
//                }
//            }
//            if (invoice.statusOrder == StatusOrder.Selesai){
//                ScreenshotButton()
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//                    WriteToMediaStoreButton()
//                }
//            }
            modelResource.value.let {
                when (it) {
                    is Resource.Failure -> {
                        Toast.makeText(context, it.throwable.message, Toast.LENGTH_SHORT).show()
                    }
                    is Resource.Loading -> {
                        CircularProgressIndicator()
                    }
                    is Resource.Success -> {}
                    else -> {}
                }
            }


//            IconButton(
//                modifier = Modifier.padding(vertical = 15.dp).background(MaterialTheme.colorScheme.primaryContainer, CircleShape).width(160.dp).height(40.dp),
//                onClick = { /*TODO*/ }
//            )
//            {
//                Row (
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Icon(imageVector = Icons.Default.Add, contentDescription = "tombol simpan")
//                    Text(text = "Simpan Invoice")
//                }
//
//            }
        }
    }
}

//val salesOrder = SalesOrder("wdmwdkwmd","dqwwqqwdq","wdqdqwd","dwqdqdd",
//    listOf(
//        ProductsItem(
//            "daqwdq","wdqdwqdq",100,91,19,1000,19000
//        )
//    ),
//    orderDate = Date(),
//    "Selesai",
//    1000,11
//)
@Preview(apiLevel = 33)
@Composable
fun PrevInvoiceScreen(){
//    InvoiceScreenForInternal(invoice = salesOrder)
}