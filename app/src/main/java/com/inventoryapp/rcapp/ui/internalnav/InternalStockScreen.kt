package com.inventoryapp.rcapp.ui.internalnav

import android.annotation.SuppressLint
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
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Button
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.inventoryapp.rcapp.ui.agentnav.ListProduct
import com.inventoryapp.rcapp.ui.agentnav.viewmodel.AgentProductViewModel
import com.inventoryapp.rcapp.ui.agentnav.viewmodel.InternalProductViewModel
import com.inventoryapp.rcapp.ui.agentnav.viewmodel.StateHolder
import com.inventoryapp.rcapp.ui.agentnav.viewmodel.internalProducts
import com.inventoryapp.rcapp.ui.auth.internalauth.AuthInternalViewModel
import com.inventoryapp.rcapp.ui.nav.ROUTE_HOME
import com.inventoryapp.rcapp.ui.theme.spacing
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun InternalStockScreen(viewModel: AuthInternalViewModel, navController: NavController){
    val agentProductViewModel = AgentProductViewModel()
    val internalProductViewModel = InternalProductViewModel()
    val spacing = MaterialTheme.spacing
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val searchText by agentProductViewModel.searchText.collectAsState()
    val isSearching by agentProductViewModel.isSearching.collectAsState()
    val agentProductList by agentProductViewModel.agentProductList.collectAsState()
    val searchInternalProduct by internalProductViewModel.searchText.collectAsState()
    val internalProductIsSearching by internalProductViewModel.isSearching.collectAsState()
    val internalProductList by internalProductViewModel.productsList.collectAsState()
    val sheetState = rememberModalBottomSheetState()
    var showAddDataProductSheet by remember { mutableStateOf(false) }
    var showEditProductSheet by remember {
        mutableStateOf(false)
    }
    var productName by remember { mutableStateOf("") }
    var qtyProduct by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var discount by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    val context = LocalContext.current
    var descProduct by remember { mutableStateOf("") }
    val selectedCard = remember { mutableStateOf("") }
    val selectedProductNameHolder = StateHolder(initialValue = "")
    var isProductNameEmpty = true
    var isQtyProductEmpty = true
    var isPriceEmpty = true
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = MaterialTheme.colorScheme.surfaceContainerLowest
            ) {
                Column {
                    Text("Drawer title", modifier = Modifier.padding(16.dp))
                    Divider()
                    NavigationDrawerItem(
                        label = { Text(text = "Drawer Item") },
                        selected = false,
                        onClick = { /*TODO*/ }
                    )
                    IconButton(onClick = {
                        viewModel.logout()
                        navController.navigate(ROUTE_HOME)
                    }) {
                        Icon(imageVector = Icons.Default.Home, contentDescription = "logout")
                    }
                }

            }
        },
    )
    {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
            topBar = {
                Column {
                    TopAppBar(
                        title = {
                            Text(text = "Produk",
                                maxLines = 1,
                                overflow = TextOverflow.Clip,
                                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Medium))
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                            titleContentColor = MaterialTheme.colorScheme.onSurface,
                        ),
                        navigationIcon =
                        {
                            IconButton(onClick = {
                                scope.launch {
                                    drawerState.apply {
                                        if (isClosed) open() else close()
                                    }
                                }
                            }){
                                Icon(imageVector = Icons.Rounded.Menu, contentDescription = "menu")
                            }
                        },
                        actions = {},
                        scrollBehavior = scrollBehavior)
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
                                ListProduct(
                                    item,
                                    onCardClicked = {itemClick ->
                                    showEditProductSheet = true
                                })
                            }
                        }
                    }
                    LazyColumn (modifier = Modifier.padding(start = 8.dp, end = 8.dp, top =25.dp, bottom = 80.dp)){
                        items(internalProducts) { item ->
                            ListProduct(
                                item,
                                onCardClicked = {itemClick ->
                                    showEditProductSheet = true
                                }
                            )
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
                                Text(text = "Roti Tiro")
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
                            placeholder = {
                                Text(text = "Roti Tiro")
                            },
                            trailingIcon = {
                                if (isQtyProductEmpty) {
                                    Icon(Icons.Outlined.Info, contentDescription = "isi dahulu")
                                } else{
                                    Icon(imageVector = Icons.Outlined.Done, contentDescription ="done" )
                                }
                            }
                        )
                        OutlinedTextField(
                            value = price,
                            onValueChange = {
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
                                Text(text = "Rp." + "100000")
                            },
                            trailingIcon = {
                                if (isPriceEmpty) {
                                    Icon(Icons.Outlined.Info, contentDescription = "isi dahulu")
                                } else{
                                    Icon(imageVector = Icons.Outlined.Done, contentDescription ="done" )
                                }
                            }
                        )
                        OutlinedTextField(
                            value = discount,
                            onValueChange = {
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
                        Button(
                            modifier = Modifier.padding(top = 10.dp, bottom = 20.dp),
                            onClick = { /*TODO*/ }) {
                            Text(text = "Simpan")
                        }
                    }
                }
            }
            if (showEditProductSheet) {
                ModalBottomSheet(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                    onDismissRequest = {
                        showEditProductSheet = false
                    },
                    sheetState = sheetState
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Edit Data Produk",
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
                                Text(text = "Roti Tiro")
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
                            placeholder = {
                                Text(text = "Roti Tiro")
                            },
                            trailingIcon = {
                                if (isQtyProductEmpty) {
                                    Icon(Icons.Outlined.Info, contentDescription = "isi dahulu")
                                } else{
                                    Icon(imageVector = Icons.Outlined.Done, contentDescription ="done" )
                                }
                            }
                        )
                        OutlinedTextField(
                            value = price,
                            onValueChange = {
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
                                Text(text = "Rp." + "100000")
                            },
                            trailingIcon = {
                                if (isPriceEmpty) {
                                    Icon(Icons.Outlined.Info, contentDescription = "isi dahulu")
                                } else{
                                    Icon(imageVector = Icons.Outlined.Done, contentDescription ="done" )
                                }
                            }
                        )
                        OutlinedTextField(
                            value = discount,
                            onValueChange = {
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
                        Button(
                            modifier = Modifier.padding(top = 10.dp, bottom = 20.dp),
                            onClick = { /*TODO*/ }) {
                            Text(text = "Simpan")
                        }
                    }
                }
            }
        }
    }
}