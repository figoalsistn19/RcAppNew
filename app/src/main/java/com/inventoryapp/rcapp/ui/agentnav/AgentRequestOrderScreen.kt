package com.inventoryapp.rcapp.ui.agentnav

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SelectableChipColors
import androidx.compose.material3.SelectableChipElevation
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.inventoryapp.rcapp.R
import com.inventoryapp.rcapp.data.model.OfferingBySales
import com.inventoryapp.rcapp.data.model.SalesOrder
import com.inventoryapp.rcapp.ui.agentnav.viewmodel.AgentProductViewModel
import com.inventoryapp.rcapp.ui.agentnav.viewmodel.reqOrders
import com.inventoryapp.rcapp.ui.internalnav.viewmodel.OfferingPoViewModel
import com.inventoryapp.rcapp.ui.nav.ROUTE_HOME_AGENT_SCREEN
import com.inventoryapp.rcapp.ui.theme.spacing
import com.inventoryapp.rcapp.util.Resource
import java.text.SimpleDateFormat

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AgentRequestOrderScreen(
    offeringPoViewModel: OfferingPoViewModel,
    agentProductViewModel: AgentProductViewModel,
    navController: NavController
){
    val offeringAgentList by offeringPoViewModel.offeringAgents.observeAsState()

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    var filterBySales by remember { mutableStateOf(true) }
    var filterBySystem by remember { mutableStateOf(false) }
    var qtyOrder by remember { mutableStateOf("") }
    var totalPriceProduct by remember { mutableStateOf("") }

    val context = LocalContext.current
    var isQtyEmpty = true
    val agentProductList by agentProductViewModel.agentProductListForReqOrder.collectAsState()
    var showDetailOrder by remember { mutableStateOf(false) }
    var sheetState = rememberModalBottomSheetState(true)
    Scaffold (
        containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
        topBar = {
            Column (
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(top = 65.dp)
                ){
                Row (
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ){
                    val colorchip = MaterialTheme.colorScheme
                    FilterChip(
                        modifier = Modifier
                            .size(154.dp, 43.dp)
                            .padding(start = 50.dp),
                        onClick = {
                            filterBySales = !filterBySales
                            filterBySystem = !filterBySystem
                                  },
                        label = {
                            Text(
                                "by Sales",
                                textAlign = TextAlign.Center
                            )
                        },
                        colors = SelectableChipColors(
                            containerColor = colorchip.onPrimary,
                            labelColor = colorchip.onSurface,
                            leadingIconColor = colorchip.primary,
                            trailingIconColor = colorchip.primaryContainer,
                            disabledContainerColor = colorchip.onPrimary,
                            disabledLabelColor = colorchip.onTertiaryContainer,
                            disabledLeadingIconColor = colorchip.onSurface,
                            disabledTrailingIconColor = colorchip.onTertiary,
                            selectedContainerColor = colorchip.primary,
                            disabledSelectedContainerColor = colorchip.onPrimary,
                            selectedLabelColor = colorchip.onPrimary,
                            selectedLeadingIconColor = colorchip.onPrimary,
                            selectedTrailingIconColor = colorchip.onSurface
                        ),
                        selected = filterBySales,
                        leadingIcon = if (filterBySales) {
                            {
                                Icon(
                                    imageVector = ImageVector.vectorResource(id = R.drawable.by_sales),
                                    contentDescription = "Done icon",
                                    modifier = Modifier.size(FilterChipDefaults.IconSize),
                                )
                            }
                        } else {
                            {
                                Icon(
                                    imageVector = ImageVector.vectorResource(id = R.drawable.by_sales),
                                    contentDescription = "Done icon",
                                    modifier = Modifier.size(FilterChipDefaults.IconSize),
                                )
                            }
                        },
                        shape = RoundedCornerShape(12.dp),
                        elevation = SelectableChipElevation(10.dp,10.dp,10.dp,10.dp,10.dp,0.dp)
                    )
                    FilterChip(
                        modifier = Modifier
                            .size(154.dp, 43.dp)
                            .padding(end = 38.dp),
                        onClick = {
                            filterBySystem = !filterBySystem
                            filterBySales = !filterBySales
                                  },
                        label = {
                            Text(
                                "by System",
                                textAlign = TextAlign.Center
                            )
                        },
                        selected = filterBySystem,
                        colors = SelectableChipColors(
                            containerColor = colorchip.onPrimary,
                            labelColor = colorchip.onSurface,
                            leadingIconColor = colorchip.primary,
                            trailingIconColor = colorchip.onPrimary,
                            disabledContainerColor = colorchip.onPrimary,
                            disabledLabelColor = colorchip.onPrimary,
                            disabledLeadingIconColor = colorchip.onPrimary,
                            disabledTrailingIconColor = colorchip.onPrimary,
                            selectedContainerColor = colorchip.primary,
                            disabledSelectedContainerColor = colorchip.onPrimary,
                            selectedLabelColor = colorchip.onPrimary,
                            selectedLeadingIconColor = colorchip.onPrimary,
                            selectedTrailingIconColor = colorchip.primary
                        ),
                        leadingIcon = if (filterBySystem) {
                            {
                                Icon(
                                    imageVector = ImageVector.vectorResource(id = R.drawable.by_system),
                                    contentDescription = "Done icon",
                                    modifier = Modifier.size(FilterChipDefaults.IconSize),
                                )
                            }
                        } else {
                            {
                                Icon(
                                    imageVector = ImageVector.vectorResource(id = R.drawable.by_system),
                                    contentDescription = "Done icon",
                                    modifier = Modifier.size(FilterChipDefaults.IconSize),
                                )
                            }
                        },
                        shape = RoundedCornerShape(12.dp)
                    )
                }
                if (filterBySystem){
                    LazyColumn (modifier = Modifier.padding(start = 8.dp, end = 8.dp, top =25.dp)){
                        items(agentProductList) { item ->
//                            CardBySales(reqOrder = item, onCardClick = {
//                                showDetailOrder = true
//                            })
                        }
                    }
                } else {
                    LaunchedEffect(Unit) {
                        offeringPoViewModel.fetchOfferingBySales()
                    }
                    when (offeringAgentList) {
                        is Resource.Success -> {
                            val offeringList = (offeringAgentList as Resource.Success<List<OfferingBySales>>).result
                            LazyColumn (modifier = Modifier.padding(start = 8.dp, end = 8.dp, top =25.dp)){
                                items(offeringList) { item ->
                                    CardBySales(reqOrder = item, onCardClick = {
                                        showDetailOrder = true
                                    })
                                }
                            }
                        }

                        is Resource.Loading -> {
                            // Tampilkan indikator loading jika diperlukan
                            CircularProgressIndicator()
                        }

                        is Resource.Failure -> {
                            // Tampilkan pesan error jika diperlukan
                            val error = (offeringAgentList as Resource.Failure).throwable
                            Text(text = "Error: ${error.message}")
                        }

                        else -> {
                            // Tampilkan pesan default jika diperlukan
                            Text(text = "No data available")
                        }
                    }
                }
            }
        }
    ){
        if (showDetailOrder){
            ModalBottomSheet(
                onDismissRequest = {
                    showDetailOrder=false
                },
                sheetState = sheetState
            ){
                Column (
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
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
                        Column (
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ){
                            Text(
                                modifier = Modifier
                                    .padding(top = 10.dp, start = 20.dp)
                                    .align(Alignment.Start),
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
                                    .padding(
                                        top = 15.dp,
                                        start = 20.dp,
                                        end = 20.dp,
                                        bottom = 15.dp
                                    )
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
                    Button(
                        onClick = {
                            if (isQtyEmpty) {
                                // Tampilkan pesan error
                                Toast.makeText(context, "Jumlah barang tidak boleh kosong", Toast.LENGTH_SHORT).show()
                            }else {
                                isQtyEmpty= false
                                navController.navigate(ROUTE_HOME_AGENT_SCREEN)
                            }
                                  },
                        modifier = Modifier.padding(bottom = 20.dp)
                    ) {
                        Text(text = "Pesan")
                    }

                }
            }
        }
    }
}

@Composable
fun CardBySystem(
    reqOrder: OfferingBySales, // Pass required data from LazyColumn items
    onCardClick: (String) -> Unit // Pass lambda to handle card click
) {
    val formattedPrice = String.format("Rp%,d", reqOrder.totalPrice)
//    val sdf = SimpleDateFormat("dd MMM yyyy ・ HH:mm")
//    val date = reqOrder.orderDate
//    val fixDate = sdf.format(date)
    val color = when (reqOrder.statusOffering) {
        "PENDING" -> MaterialTheme.colorScheme.error
        "Lunas" -> MaterialTheme.colorScheme.primary
        "Selesai" -> Color.Green
        "DalamProses" -> MaterialTheme.colorScheme.tertiary
        "DalamPerjalanan" -> MaterialTheme.colorScheme.secondary
        else -> Color.Gray // Warna default untuk status yang tidak diketahui
    }
    ElevatedCard (modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 8.dp, vertical = 6.dp)
        .clickable { onCardClick(reqOrder.idOffering!!) },
        elevation = CardDefaults.cardElevation(2.dp,6.dp,4.dp,3.dp,3.dp,0.dp),
        colors = CardColors(MaterialTheme.colorScheme.surfaceContainerLow,MaterialTheme.colorScheme.onBackground, MaterialTheme.colorScheme.tertiaryContainer,MaterialTheme.colorScheme.tertiary)
    )
    {
        ConstraintLayout (modifier = Modifier.height(70.dp)){
            val (refIcon, refIdReqOrder, refDate, refPrice, refStatusOrder)= createRefs()
            val spacing = MaterialTheme.spacing
            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.tag_2),
                contentDescription = "icon harga",
                modifier = Modifier.constrainAs(refIcon){
                    top.linkTo(parent.top, spacing.medium)
                    start.linkTo(parent.start, spacing.small)
                    bottom.linkTo(parent.bottom, spacing.medium)
                },
//                tint = MaterialTheme.colorScheme.primary
            )
            Text(
                text = reqOrder.productsItem!![0].productName!!,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.constrainAs(refIdReqOrder){
                    top.linkTo(parent.top, spacing.medium)
                    bottom.linkTo(parent.bottom, spacing.medium)
                    start.linkTo(refIcon.end, spacing.small)
                }
            )
            Text(
                text = reqOrder.desc!!,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Light),
                modifier = Modifier.constrainAs(refDate){
                    top.linkTo(refIdReqOrder.bottom)
                    start.linkTo(refIcon.end, spacing.extraSmall)
                }
            )
            Spacer(modifier = Modifier.fillMaxWidth())
            Column (
                modifier = Modifier
                    .constrainAs(refPrice){
                        end.linkTo(parent.end, spacing.small)
                        top.linkTo(parent.top, spacing.medium)
                    },
                horizontalAlignment = Alignment.End
            ){
                Text(
                    text = formattedPrice,
                    style = MaterialTheme.typography.titleMedium)
                Text(
                    text = reqOrder.statusOffering.toString(),
                    style = MaterialTheme.typography.titleSmall,
                    color = color
                )
            }
        }
    }
}

@Composable
fun CardBySales(
    reqOrder: OfferingBySales, // Pass required data from LazyColumn items
    onCardClick: (String) -> Unit // Pass lambda to handle card click
) {
    val formattedPrice = String.format("Rp%,d", reqOrder.totalPrice)
//    val sdf = SimpleDateFormat("dd MMM yyyy ・ HH:mm")
//    val date = reqOrder.orderDate
//    val fixDate = sdf.format(date)
    val color = when (reqOrder.statusOffering) {
        "PENDING" -> MaterialTheme.colorScheme.error
        "Lunas" -> MaterialTheme.colorScheme.primary
        "Selesai" -> Color.Green
        "DalamProses" -> MaterialTheme.colorScheme.tertiary
        "DalamPerjalanan" -> MaterialTheme.colorScheme.secondary
        else -> Color.Gray // Warna default untuk status yang tidak diketahui
    }
    ElevatedCard (modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 8.dp, vertical = 6.dp)
        .clickable { onCardClick(reqOrder.idOffering!!) },
        elevation = CardDefaults.cardElevation(2.dp,6.dp,4.dp,3.dp,3.dp,0.dp),
        colors = CardColors(MaterialTheme.colorScheme.surfaceContainerLow,MaterialTheme.colorScheme.onBackground, MaterialTheme.colorScheme.tertiaryContainer,MaterialTheme.colorScheme.tertiary)
    )
    {
        ConstraintLayout (modifier = Modifier.height(70.dp)){
            val (refIcon, refIdReqOrder, refDate, refPrice, refStatusOrder)= createRefs()
            val spacing = MaterialTheme.spacing
            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.tag_2),
                contentDescription = "icon harga",
                modifier = Modifier.constrainAs(refIcon){
                    top.linkTo(parent.top, spacing.medium)
                    start.linkTo(parent.start, spacing.small)
                    bottom.linkTo(parent.bottom, spacing.medium)
                },
//                tint = MaterialTheme.colorScheme.primary
            )
            Text(
                text = reqOrder.productsItem!![0].productName!!,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.constrainAs(refIdReqOrder){
                    top.linkTo(parent.top, spacing.medium)
                    bottom.linkTo(parent.bottom, spacing.medium)
                    start.linkTo(refIcon.end, spacing.small)
                }
            )
            Text(
                text = reqOrder.desc!!,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Light),
                modifier = Modifier.constrainAs(refDate){
                    top.linkTo(refIdReqOrder.bottom)
                    start.linkTo(refIcon.end, spacing.extraSmall)
                }
            )
            Spacer(modifier = Modifier.fillMaxWidth())
            Column (
                modifier = Modifier
                    .constrainAs(refPrice){
                        end.linkTo(parent.end, spacing.small)
                        top.linkTo(parent.top, spacing.medium)
                    },
                horizontalAlignment = Alignment.End
            ){
                Text(
                    text = formattedPrice,
                    style = MaterialTheme.typography.titleMedium)
                Text(
                    text = reqOrder.statusOffering.toString(),
                    style = MaterialTheme.typography.titleSmall,
                    color = color
                )
            }
        }
    }
}

@SuppressLint("SimpleDateFormat")
@Composable
fun CardReqOrder(
    reqOrder: SalesOrder, // Pass required data from LazyColumn items
    onCardClick: (String) -> Unit // Pass lambda to handle card click
) {
    val formattedPrice = String.format("Rp%,d", reqOrder.totalPrice)
    val sdf = SimpleDateFormat("dd MMM yyyy ・ HH:mm")
    val date = reqOrder.orderDate
    val fixDate = sdf.format(date)
    val color = when (reqOrder.statusOrder) {
        "Pending" -> MaterialTheme.colorScheme.error
        "Lunas" -> MaterialTheme.colorScheme.primary
        "Selesai" -> Color.Green
        "DalamProses" -> MaterialTheme.colorScheme.tertiary
        "DalamPerjalanan" -> MaterialTheme.colorScheme.secondary
        else -> Color.Gray // Warna default untuk status yang tidak diketahui
    }
    ElevatedCard (modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 8.dp, vertical = 6.dp)
        .clickable { onCardClick(reqOrder.idOrder!!) },
        elevation = CardDefaults.cardElevation(2.dp,6.dp,4.dp,3.dp,3.dp,0.dp),
        colors = CardColors(MaterialTheme.colorScheme.surfaceContainerLow,MaterialTheme.colorScheme.onBackground, MaterialTheme.colorScheme.tertiaryContainer,MaterialTheme.colorScheme.tertiary)
    )
    {
        ConstraintLayout (modifier = Modifier.height(70.dp)){
            val (refIcon, refIdReqOrder, refDate, refPrice, refStatusOrder)= createRefs()
            val spacing = MaterialTheme.spacing
            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.tag_2),
                contentDescription = "icon harga",
                modifier = Modifier.constrainAs(refIcon){
                    top.linkTo(parent.top, spacing.medium)
                    start.linkTo(parent.start, spacing.small)
                    bottom.linkTo(parent.bottom, spacing.medium)
                },
//                tint = MaterialTheme.colorScheme.primary
            )
            Text(
                text = reqOrder.productsItem[0].productName!!,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.constrainAs(refIdReqOrder){
                    top.linkTo(parent.top, spacing.medium)
                    start.linkTo(refIcon.end, spacing.extraSmall)
                }
                )
            Text(
                text = fixDate,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Light),
                modifier = Modifier.constrainAs(refDate){
                    top.linkTo(refIdReqOrder.bottom)
                    start.linkTo(refIcon.end, spacing.extraSmall)
                }
            )
            Spacer(modifier = Modifier.fillMaxWidth())
            Column (
                modifier = Modifier
                    .constrainAs(refPrice){
                        end.linkTo(parent.end, spacing.small)
                        top.linkTo(parent.top, spacing.medium)
            },
                horizontalAlignment = Alignment.End
            ){
                Text(
                    text = formattedPrice,
                    style = MaterialTheme.typography.titleMedium)
                Text(
                    text = reqOrder.statusOrder.toString(),
                    style = MaterialTheme.typography.titleSmall,
                    color = color
                )
            }


//            Text(
//                text = "Rp.500.000",
//                modifier = Modifier.constrainAs(refPrice){
//                    top.linkTo(parent.top)
//                    end.linkTo(parent.end)
//                })
        }
    }

}

@Preview(apiLevel = 33)
@Composable
fun PreviewRequestOrder(){

}