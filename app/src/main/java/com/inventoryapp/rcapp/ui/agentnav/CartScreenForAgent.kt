package com.inventoryapp.rcapp.ui.agentnav

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Source
import com.inventoryapp.rcapp.data.model.ProductsItem
import com.inventoryapp.rcapp.data.model.SalesOrder
import com.inventoryapp.rcapp.data.model.StatusOrder
import com.inventoryapp.rcapp.ui.internalnav.AlertDialogExample
import com.inventoryapp.rcapp.ui.internalnav.CartItemRow
import com.inventoryapp.rcapp.ui.nav.ROUTE_MAIN_INTERNAL_SCREEN
import com.inventoryapp.rcapp.ui.viewmodel.InternalProductViewModel
import com.inventoryapp.rcapp.ui.viewmodel.SalesOrderViewModel
import com.inventoryapp.rcapp.util.FireStoreCollection
import com.inventoryapp.rcapp.util.Resource
import kotlinx.coroutines.launch
import java.util.Date

@SuppressLint("DefaultLocale", "StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreenForAgent(
    salesOrderViewModel: SalesOrderViewModel?,
    internalProductViewModel: InternalProductViewModel?,
    navController: NavController
) {
    val searchText by internalProductViewModel!!.searchText.collectAsState()
    val isSearching by internalProductViewModel!!.isSearching.collectAsState()
    val internalProductList by internalProductViewModel!!.internalProductList.collectAsState()

    val modelResource = salesOrderViewModel?.addSalesOrderFlow?.collectAsState()

    val cartItems by internalProductViewModel!!.cartDataAgent.observeAsState()
    val openAlertDialog = remember { mutableStateOf(false) }

    val agentUser = internalProductViewModel?.currentUser
    val db = FirebaseFirestore.getInstance()

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    var cartItemsListForObj by remember {
        mutableStateOf(listOf<ProductsItem>())
    }

    var totalPrice by remember {
        mutableLongStateOf(0L)
    }
    var tax by remember {
        mutableLongStateOf(0L)
    }
    var totalPriceAllItem by remember {
        mutableLongStateOf(0L)
    }

    val subtotalFormattedPrice = String.format("Rp%,d", totalPrice)
    val taxFormattedPrice = String.format("Rp%,d", tax)
    val totalPriceAllItemFormattedPrice = String.format("Rp%,d", totalPriceAllItem)

    Column(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.surfaceContainerLowest)
    ) {
        TopAppBar(
            title = {
                Text(
                    text = "Keranjang",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Medium
                    )
                )
            },
            colors = TopAppBarColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                scrolledContainerColor = MaterialTheme.colorScheme.surfaceContainer,
                navigationIconContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                titleContentColor = MaterialTheme.colorScheme.onSurface,
                actionIconContentColor = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            modifier = Modifier.fillMaxWidth(),
            navigationIcon = {
                IconButton(onClick = {
                    scope.launch {
                        navController.popBackStack()
                    }
                }) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = "Localized description",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        )
        SearchBar(
            query = searchText,//text showed on SearchBar
            onQueryChange = internalProductViewModel!!::onSearchTextChange, //update the value of searchText
            onSearch = internalProductViewModel::onSearchTextChange, //the callback to be invoked when the input service triggers the ImeAction.Search action
            active = isSearching, //whether the user is searching or not
            onActiveChange = { internalProductViewModel.onToogleSearch() }, //the callback to be invoked when this search bar's active state is changed
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 23.dp, bottom = 8.dp, end = 23.dp),
            trailingIcon = {
                Icon(imageVector = Icons.Rounded.Search, contentDescription = "cari" )
            },
            placeholder = {
                Text(text = "Cari barang disini...")
            }
        ) {
            LazyColumn (modifier = Modifier
                .fillMaxWidth()
                .padding(start = 23.dp, top = 8.dp, end = 23.dp),){
                items(internalProductList) { item ->
                    ListProduct(
                        item,
                        onCardClicked = {},
                        onCardData = { it ->
//                            internalProduct.value = it
                            val finalPrice = it.price!! - (it.price!! * it.discProduct!!/100)
                            val productItem = ProductsItem(
                                idProduct = it.idProduct,
                                productName = it.productName,
                                price = it.price,
                                quantity = 1,
                                discProduct = it.discProduct,
                                finalPrice = finalPrice,
                                totalPrice = it.price
                            )
                            db.collection(FireStoreCollection.AGENTUSER)
                                .document(agentUser!!.uid)
                                .collection(FireStoreCollection.CARTDATAAGENT)
                                .document(it.idProduct!!)
                                .set(productItem)
                                .addOnSuccessListener {
                                    Toast.makeText(context,"Barang berhasil ditambahkan", Toast.LENGTH_SHORT).show()
                                    internalProductViewModel.setIsSearching()
                                    internalProductViewModel.fetchCartDataAgent()
                                }
                                .addOnFailureListener {
                                    Toast.makeText(context,it.message, Toast.LENGTH_SHORT).show()
                                }
                        }
                    )
                }
            }
        }
        Column(
            modifier = Modifier
                .padding(top = 10.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(MaterialTheme.colorScheme.surfaceContainerLow)
        ) {
            LaunchedEffect(Unit) {
                internalProductViewModel.fetchCartDataAgent()
            }
            when (cartItems) {
                is Resource.Success -> {
                    val cartItemsList = (cartItems as Resource.Success<List<ProductsItem>>).result
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .padding(top = 12.dp)
                    ) {
                        items(cartItemsList) { cartItem ->
                            val source = Source.DEFAULT
                            db.collection(FireStoreCollection.INTERNALPRODUCT)
                                .document(cartItem.idProduct!!)
                                .get(source)
                                .addOnSuccessListener { document ->
                                    val finalPriceProduct = document.getLong("finalPrice")
                                    if (document != null) {
                                        // Dokumen ditemukan, dapatkan data
                                        cartItem.finalPrice = finalPriceProduct!!
                                        // Lakukan sesuatu dengan data
                                    } else {
                                        Toast.makeText(context,"Data tidak ditemukan", Toast.LENGTH_SHORT).show()
                                    }
                                }
                                .addOnFailureListener { exception ->
                                    Toast.makeText(context,exception.message, Toast.LENGTH_SHORT).show()
                                }
                            var quantity by remember {
                                mutableIntStateOf(1)
                            }
//                            val finalPrice by remember {
//                                mutableLongStateOf(cartItem.finalPrice!! * quantity)
//                            }
                            cartItem.quantity = quantity
                            val priceBefore = cartItem.price!! * quantity
                            cartItem.totalPrice = cartItem.finalPrice!! * quantity
                            CartItemRow(
                                cartItem = cartItem,
                                price = cartItem.totalPrice!!,
                                onQuantityIncreased = { quantity++ },
                                quantity = quantity,
                                onQuantityDecreased = { if (quantity>1) quantity-- },
                                priceBeforeDisc = priceBefore,
                                onItemRemoved = {
                                    db.collection(FireStoreCollection.CARTDATA)
                                        .document(cartItem.idProduct!!).delete()
                                        .addOnSuccessListener {
                                            Toast.makeText(context,"Barang berhasil dihapus", Toast.LENGTH_SHORT).show()
                                            internalProductViewModel.fetchCartDataAgent()
                                        }
                                        .addOnFailureListener {
                                            Toast.makeText(context,it.message, Toast.LENGTH_SHORT).show()
                                        }
                                }
                            )

                            var totalPriceCalculation = 0L
                            for (item in cartItemsList){
                                totalPriceCalculation += item.totalPrice ?: 0
                            }
                            totalPrice = totalPriceCalculation
                            tax = totalPriceCalculation * 11/100
                            totalPriceAllItem = totalPrice + tax
                            if (cartItemsList.isNotEmpty()){
                                for (item in cartItemsList){
                                    cartItemsListForObj = cartItemsList
                                }
                            } else cartItemsListForObj = emptyList()
                        }
                    }
                }
                is Resource.Loading -> {
                    // Tampilkan indikator loading jika diperlukan
                    CircularProgressIndicator(
                        modifier = Modifier
                            .padding(horizontal = 10.dp, vertical = 1.dp)
                    )
                }
                is Resource.Failure -> {
                    // Tampilkan pesan error jika diperlukan
                    val error = (cartItems as Resource.Failure).throwable
                    Text(text = "Error: ${error.message}")
                }
                else -> {
                    // Tampilkan pesan default jika diperlukan
                    Text(text = "No data available")
                }
            }
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 23.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
                Row {
                    Text(
                        text = "Subtotal: ",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.outline
                        )
                    )
                    Text(
                        text = subtotalFormattedPrice,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )
                }
                Row {
                    Text(
                        text = "Pajak: ",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.outline
                        )
                    )
                    Text(
                        text = taxFormattedPrice,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    )
                }

            }
            Column (
                modifier = Modifier
                    .padding(top = 10.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp))
                    .background(MaterialTheme.colorScheme.surfaceContainerLowest)
            ){
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Column (
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.Start,
                        modifier = Modifier
                            .padding(vertical = 16.dp, horizontal = 12.dp)
                            .wrapContentSize()
                    ){
                        Text(text = "Total",
                            style = MaterialTheme.typography.titleSmall)
                        Text(
                            text = totalPriceAllItemFormattedPrice,
                            style = MaterialTheme.typography.titleLarge)
                    }
                    Row {
                        Button(
                            onClick = {
                                if (cartItemsListForObj.isEmpty()){
                                    Toast.makeText(context,"Keranjang kosong", Toast.LENGTH_SHORT).show()
                                }
                                if (internalProductViewModel.currentUser == null){
                                    Toast.makeText(context,"Pilih agen dulu", Toast.LENGTH_SHORT).show()
                                }
                                else openAlertDialog.value = true
                            },
                            modifier = Modifier
                                .wrapContentSize()
                                .padding(16.dp)
                        ) {
                            Text(text = "Checkout")
                        }
                    }

                }
            }
            fun getSalesOrder(): SalesOrder {
//
                return SalesOrder(
                    idOrder = "",
                    idAgent = agentUser?.uid,
                    nameAgent = agentUser?.displayName,
                    email = agentUser?.email,
                    statusOrder = StatusOrder.Pending,
                    productsItem = cartItemsListForObj,
                    totalPrice = totalPriceAllItem,
                    tax = 11,
                    orderDate = Date()
                )
            }
            val salesOrderObj: SalesOrder = getSalesOrder()
            when {
                // ...
                openAlertDialog.value -> {
                    AlertDialogExample(
                        onDismissRequest = {
                            openAlertDialog.value = false
                            salesOrderViewModel?.fetchSalesOrderInternal()
                        },
                        onConfirmation = {
                            openAlertDialog.value = false
                            salesOrderViewModel!!.addSalesOrder(salesOrderObj)
                            Toast.makeText(context, "Pesanan berhasil dikirim", Toast.LENGTH_SHORT).show()
                            navController.navigate(ROUTE_MAIN_INTERNAL_SCREEN)
                            println("Confirmation registered") // Add logic here to handle confirmation.
                        },
                        dialogTitle = "Yakin dengan semua pesanan ?",
                        dialogText = "Jika memilih confirm maka pesanan akan diajukan",
                        icon = Icons.Default.Info
                    )
                }
            }
            modelResource?.value?.let {
                when (it) {
                    is Resource.Failure -> {
                        Toast.makeText(context, it.throwable.message, Toast.LENGTH_SHORT).show()
                    }
                    is Resource.Loading -> {
                        CircularProgressIndicator()
                    }
                    is Resource.Success -> {
                    }
                }
            }
        }
    }
}