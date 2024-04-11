package com.inventoryapp.rcapp.ui.internalnav

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import androidx.navigation.NavController
import com.inventoryapp.rcapp.data.model.InternalProduct
import com.inventoryapp.rcapp.ui.agentnav.ListProduct
import com.inventoryapp.rcapp.ui.agentnav.viewmodel.AgentProductViewModel
import com.inventoryapp.rcapp.ui.auth.internalauth.AuthInternalViewModel
import com.inventoryapp.rcapp.ui.internalnav.viewmodel.InternalProductViewModel
import com.inventoryapp.rcapp.ui.nav.ROUTE_HOME_INTERNAL_SCREEN
import com.inventoryapp.rcapp.ui.nav.ROUTE_INTERNAL_STOCK_SCREEN
import com.inventoryapp.rcapp.util.Resource
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "StateFlowValueCalledInComposition")
@Composable
fun InternalStockScreen(
    agentProductViewModel: AgentProductViewModel,
    internalProductViewModel: InternalProductViewModel?,
    authViewModel: AuthInternalViewModel?,
    navController: NavController
){
    val searchText by agentProductViewModel.searchText.collectAsState()
    val isSearching by agentProductViewModel.isSearching.collectAsState()
    val agentProductList by agentProductViewModel.agentProductList.collectAsState()
    val sheetState = rememberModalBottomSheetState(true)
    var showAddDataProductSheet by remember { mutableStateOf(false) }
    var showEditProductSheet by remember {
        mutableStateOf(false)
    }
    var productName by remember { mutableStateOf("") }
    var qtyProduct by remember { mutableStateOf("") }
    var qtyMinProduct by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var discount by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    val modelResource = internalProductViewModel?.internalProductFlow?.collectAsState()
    val internalProduct by internalProductViewModel!!.internalProducts.observeAsState()

    val context = LocalContext.current
    var isProductNameEmpty = true
    var isQtyProductEmpty = true
    var isQtyMinProductEmpty = true
    var isPriceEmpty = true
    var isEditProductNameEmpty = true
    var isEditQtyMinProductEmpty = true
    var isEditPriceEmpty = true

    Scaffold(
        containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
        topBar = {
            Column (
                modifier = Modifier.padding(top = 55.dp)
            ){
                SearchBar(
                    query = searchText,//text showed on SearchBar
                    onQueryChange = agentProductViewModel::onSearchTextChange, //update the value of searchText
                    onSearch = agentProductViewModel::onSearchTextChange, //the callback to be invoked when the input service triggers the ImeAction.Search action
                    active = isSearching, //whether the user is searching or not
                    onActiveChange = { agentProductViewModel.onToogleSearch() }, //the callback to be invoked when this search bar's active state is changed
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 23.dp, top = 8.dp, end = 23.dp),
                    trailingIcon = {
                        Icon(imageVector = Icons.Rounded.Search, contentDescription = "cari" )
                    },
                    placeholder = {
                        Text(text = "Cek stok disini...")
                    }
                ) {
                    LazyColumn (modifier = Modifier.padding(horizontal = 8.dp, vertical =10.dp)){
                        items(agentProductList) { item ->
//                            ListProduct(
//                                item,
//                                onCardClicked = {itemClick ->
//                                    showEditProductSheet = true
//                                })
                        }
                    }
                }
                LaunchedEffect(Unit) {
                    internalProductViewModel!!.fetchInternalProducts()
                }

                when (internalProduct) {
                    is Resource.Success -> {
                        val internalProductList = (internalProduct as Resource.Success<List<InternalProduct>>).result
                        LazyColumn (modifier = Modifier.padding(start = 8.dp, end = 8.dp, top =25.dp, bottom = 80.dp)){
                            items(internalProductList) { item ->
                                ListProduct(
                                    item,
                                    onCardClicked = {itemClick ->
                                        showEditProductSheet = true
                                    }
                                )
                            }
                        }
                    }
                    is Resource.Loading -> {
                        // Tampilkan indikator loading jika diperlukan
                        CircularProgressIndicator(
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                                .padding(top = 100.dp)
                        )
                    }
                    is Resource.Failure -> {
                        // Tampilkan pesan error jika diperlukan
                        val error = (internalProduct as Resource.Failure).throwable
                        Text(text = "Error: ${error.message}")
                    }
                    else -> {
                        // Tampilkan pesan default jika diperlukan
                        Text(text = "No data available")
                    }
                }
            }
        },
        bottomBar = {
            Column {
                Row(modifier = Modifier.fillMaxWidth()) {
                    // Your other UI content here
                    Spacer(modifier = Modifier.weight(1f)) // Add flexibility with weight
                    ExtendedFloatingActionButton(
                        onClick = {
                            showAddDataProductSheet = true
                        },
                        shape = FloatingActionButtonDefaults.extendedFabShape,
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        elevation = FloatingActionButtonDefaults.elevation(1.dp),
                        modifier = Modifier.padding(bottom = 20.dp, end = 18.dp)

                    ) {
                        Icon(imageVector = Icons.Default.Edit, contentDescription = "Tombol tambah" )
                        Text(text = "Tambah", modifier = Modifier.padding(start = 5.dp))
                    }
                }
                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    ) {
        if (showAddDataProductSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showAddDataProductSheet = false
                },
                sheetState = sheetState,
                containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Tambah Data Produk",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Medium
                        )
                    )
                    OutlinedTextField(
                        value = productName,
                        onValueChange = {
                            productName = it
                            isProductNameEmpty = it.isEmpty()
                        },
                        isError = isProductNameEmpty,
                        label = {
                            Text(text = "Nama Produk")
                        },
                        modifier = Modifier.padding(top = 15.dp),
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.None,
                            autoCorrect = false,
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Done
                        ),
                        maxLines = 1,
                        placeholder = {
                            Text(text = "cth : Roti ayam")
                        },
                        trailingIcon = {
                            if (isProductNameEmpty) {
                                Icon(Icons.Outlined.Info, contentDescription = "isi dahulu")
                            } else{
                                Icon(imageVector = Icons.Outlined.Done, contentDescription ="done" )
                            }
                        }
                    )
                    OutlinedTextField(
                        value = qtyProduct,
                        onValueChange = {
                            if (it.isDigitsOnly())
                            qtyProduct = it
                            isQtyProductEmpty = it.isEmpty()
                        },
                        isError = isQtyProductEmpty,
                        label = {
                            Text(text = "Jumlah Produk")
                        },
                        modifier = Modifier.padding(vertical = 5.dp),
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.None,
                            autoCorrect = false,
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        ),
                        maxLines = 1,
//                        placeholder = {
//                            Text(text = "Jumlah produk sekarang")
//                        },
                        trailingIcon = {
                            if (isQtyProductEmpty) {
                                Icon(Icons.Outlined.Info, contentDescription = "isi dahulu")
                            } else{
                                Icon(imageVector = Icons.Outlined.Done, contentDescription ="done" )
                            }
                        },
                        suffix = {
                            Text(text = "Pcs")
                        }
                    )
                    OutlinedTextField(
                        value = qtyMinProduct,
                        onValueChange = {
                            if (it.isDigitsOnly())
                            qtyMinProduct = it
                            isQtyMinProductEmpty = it.isEmpty()
                        },
                        isError = isQtyMinProductEmpty,
                        label = {
                            Text(text = "Jumlah Minimal Produk")
                        },
                        modifier = Modifier.padding(vertical = 5.dp),
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.None,
                            autoCorrect = false,
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        ),
                        maxLines = 1,
//                        placeholder = {
//                            Text(text = "Jumlah minimal produk di gudang")
//                        },
                        trailingIcon = {
                            if (isQtyMinProductEmpty) {
                                Icon(Icons.Outlined.Info, contentDescription = "isi dahulu")
                            } else{
                                Icon(imageVector = Icons.Outlined.Done, contentDescription ="done" )
                            }
                        },
                        suffix = {
                            Text(text = "Pcs")
                        }
                    )
                    OutlinedTextField(
                        value = price,
                        onValueChange = {
                            if (it.isDigitsOnly())
                            price = it
                            isPriceEmpty = it.isEmpty()
                        },
                        isError = isPriceEmpty,
                        label = {
                            Text(text = "Harga")
                        },
                        modifier = Modifier.padding(vertical = 5.dp),
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.None,
                            autoCorrect = false,
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        ),
                        maxLines = 1,
                        placeholder = {
                            Text(text = "Harga barang")
                        },
                        trailingIcon = {
                            if (isPriceEmpty) {
                                Icon(Icons.Outlined.Info, contentDescription = "isi dahulu")
                            } else{
                                Icon(imageVector = Icons.Outlined.Done, contentDescription ="done" )
                            }
                        },
                        prefix = {
                            Text(text = "Rp.")
                        }
                    )
                    OutlinedTextField(
                        value = discount,
                        onValueChange = {
                            if (it.isDigitsOnly() && it.length <=2 )
                            discount = it
                        },
                        label = {
                            Text(text = "Diskon")
                        },
                        modifier = Modifier.padding(vertical = 5.dp),
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.None,
                            autoCorrect = false,
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        ),
                        maxLines = 1,
                        placeholder = {
                            Text(text = "10" + " %")
                        },
                        suffix = {
                            Text(text = "%")
                        }
                    )
                    OutlinedTextField(
                        value = notes,
                        onValueChange = {
                            notes = it
                        },
                        label = {
                            Text(text = "Catatan")
                        },
                        modifier = Modifier.padding(vertical = 5.dp),
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.None,
                            autoCorrect = false,
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Done
                        ),
                        maxLines = 1,
                        placeholder = {
                            Text(text = "Catatan")
                        }
                    )
                    fun getInternalProduct(): InternalProduct {
                        val qtyProductValue = qtyProduct.toIntOrNull() ?: 0
                        val qtyMinProductValue = qtyMinProduct.toIntOrNull() ?: 0
                        val priceValue = price.toLongOrNull() ?: 0
                        val discountValue = discount.toIntOrNull() ?:0
                        return InternalProduct(
                            idProduct = "",
                            productName = productName,
                            qtyProduct = qtyProductValue,
                            qtyMin = qtyMinProductValue,
                            discProduct = discountValue,
                            price = priceValue,
                            finalPrice = priceValue - (priceValue * discountValue / 100),
                            updateAt = Date(),
                            desc = notes
                        )
                    }
                    val internalObj: InternalProduct = getInternalProduct()
                    Button(
                        modifier = Modifier.padding(top = 10.dp, bottom = 40.dp),
                        onClick = {
                            if (isProductNameEmpty) {
                                // Tampilkan pesan error
                                Toast.makeText(
                                    context,
                                    "Nama produk tidak boleh kosong",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            if (isQtyProductEmpty) {
                                // Tampilkan pesan error
                                Toast.makeText(
                                    context,
                                    "Jumlah tidak boleh kosong",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            if (isPriceEmpty) {
                                // Tampilkan pesan error
                                Toast.makeText(
                                    context,
                                    "Harga tidak boleh kosong",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                internalProductViewModel?.addInternalProduct(internalObj)
                                navController.navigate(ROUTE_INTERNAL_STOCK_SCREEN) {
                                    popUpTo(ROUTE_HOME_INTERNAL_SCREEN) { inclusive = true }
                                }
                                Toast.makeText(
                                    context,
                                    "Sukses menambahkan barang",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    ) {
                        Text(text = "Simpan")
                    }
                    modelResource?.value?.let {
                        when (it) {
                            is Resource.Failure -> {
                                Toast.makeText(context, it.throwable.message, Toast.LENGTH_SHORT).show()
                            }
                            is Resource.Loading -> {
                                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                            }
                            is Resource.Success -> {
                            }
                        }
                    }
                }
            }
        }
//        if (showEditProductSheet) {
//            ModalBottomSheet(
//                containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
//                onDismissRequest = {
//                    showEditProductSheet = false
//                },
//                sheetState = sheetState
//            ) {
//                Column(
//                    modifier = Modifier.fillMaxWidth(),
//                    verticalArrangement = Arrangement.Center,
//                    horizontalAlignment = Alignment.CenterHorizontally
//                ) {
//                    Text(
//                        text = "Edit Data Produk",
//                        style = MaterialTheme.typography.titleLarge.copy(
//                            fontWeight = FontWeight.Medium
//                        )
//                    )
//                    OutlinedTextField(
//                        value = productName,
//                        onValueChange = {
//                            productName = it
//                            isEditProductNameEmpty = it.isEmpty()
//                        },
//                        isError = isEditProductNameEmpty,
//                        label = {
//                            Text(text = "Nama Produk")
//                        },
//                        modifier = Modifier.padding(top = 15.dp),
//                        keyboardOptions = KeyboardOptions(
//                            capitalization = KeyboardCapitalization.None,
//                            autoCorrect = false,
//                            keyboardType = KeyboardType.Text,
//                            imeAction = ImeAction.Done
//                        ),
//                        maxLines = 1,
//                        placeholder = {
//                            Text(text = "Roti Tiro")
//                        },
//                        trailingIcon = {
//                            if (isEditProductNameEmpty) {
//                                Icon(Icons.Outlined.Info, contentDescription = "isi dahulu")
//                            } else{
//                                Icon(imageVector = Icons.Outlined.Done, contentDescription ="done" )
//                            }
//                        }
//                    )
//                    OutlinedTextField(
//                        value = qtyMinProduct,
//                        onValueChange = {
//                            qtyMinProduct = it
//                            isEditQtyMinProductEmpty = it.isEmpty()
//                        },
//                        isError = isEditQtyMinProductEmpty,
//                        enabled = false,
//                        label = {
//                            Text(text = "Jumlah Produk")
//                        },
//                        modifier = Modifier.padding(vertical = 5.dp),
//                        keyboardOptions = KeyboardOptions(
//                            capitalization = KeyboardCapitalization.None,
//                            autoCorrect = false,
//                            keyboardType = KeyboardType.Number,
//                            imeAction = ImeAction.Done
//                        ),
//                        maxLines = 1,
//                        placeholder = {
//                            Text(text = "Roti Tiro")
//                        },
//                        trailingIcon = {
//                            if (isEditQtyMinProductEmpty) {
//                                Icon(Icons.Outlined.Info, contentDescription = "isi dahulu")
//                            } else{
//                                Icon(imageVector = Icons.Outlined.Done, contentDescription ="done" )
//                            }
//                        }
//                    )
//                    OutlinedTextField(
//                        value = price,
//                        onValueChange = {
//                            price = it
//                            isEditPriceEmpty = it.isEmpty()
//                        },
//                        isError = isEditPriceEmpty,
//                        label = {
//                            Text(text = "Harga")
//                        },
//                        modifier = Modifier.padding(vertical = 5.dp),
//                        keyboardOptions = KeyboardOptions(
//                            capitalization = KeyboardCapitalization.None,
//                            autoCorrect = false,
//                            keyboardType = KeyboardType.Number,
//                            imeAction = ImeAction.Done
//                        ),
//                        maxLines = 1,
//                        placeholder = {
//                            Text(text = "Rp." + "100000")
//                        },
//                        trailingIcon = {
//                            if (isEditPriceEmpty) {
//                                Icon(Icons.Outlined.Info, contentDescription = "isi dahulu")
//                            } else{
//                                Icon(imageVector = Icons.Outlined.Done, contentDescription ="done" )
//                            }
//                        }
//                    )
//                    OutlinedTextField(
//                        value = discount,
//                        onValueChange = {
//                            discount = it
//                        },
//                        label = {
//                            Text(text = "Diskon")
//                        },
//                        modifier = Modifier.padding(vertical = 5.dp),
//                        keyboardOptions = KeyboardOptions(
//                            capitalization = KeyboardCapitalization.None,
//                            autoCorrect = false,
//                            keyboardType = KeyboardType.Number,
//                            imeAction = ImeAction.Done
//                        ),
//                        maxLines = 1,
//                        placeholder = {
//                            Text(text = "10" + " %")
//                        }
//                    )
//                    OutlinedTextField(
//                        value = notes,
//                        onValueChange = {
//                            notes = it
//                        },
//                        label = {
//                            Text(text = "Catatan")
//                        },
//                        modifier = Modifier.padding(vertical = 5.dp),
//                        keyboardOptions = KeyboardOptions(
//                            capitalization = KeyboardCapitalization.None,
//                            autoCorrect = false,
//                            keyboardType = KeyboardType.Text,
//                            imeAction = ImeAction.Done
//                        ),
//                        maxLines = 1,
//                        placeholder = {
//                            Text(text = "Catatan")
//                        }
//                    )
//
//                    Button(
//                        modifier = Modifier.padding(top = 10.dp, bottom = 20.dp),
//                        onClick = {
//                            internalProductViewModel?.addInternalProduct(internalObj)
//                            navController.popBackStack()
//                        }
//                    ) {
//                        Text(text = "Simpan Perubahan")
//                    }
//                }
//            }
//        }
    }

}