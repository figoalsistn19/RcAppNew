package com.inventoryapp.rcapp.ui.agentnav

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ElevatedCard
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
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarColors
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.inventoryapp.rcapp.R
import com.inventoryapp.rcapp.data.model.InternalProduct
import com.inventoryapp.rcapp.data.model.StatusOrder
import com.inventoryapp.rcapp.ui.agentnav.viewmodel.AgentProductViewModel
import com.inventoryapp.rcapp.ui.agentnav.viewmodel.InternalProductViewModel
import com.inventoryapp.rcapp.ui.agentnav.viewmodel.StateHolder
import com.inventoryapp.rcapp.ui.auth.agentauth.AuthAgentViewModel
import com.inventoryapp.rcapp.ui.nav.BottomNavAgentViewModel
import com.inventoryapp.rcapp.ui.nav.BottomNavBarAgent
import com.inventoryapp.rcapp.ui.nav.ROUTE_HOME
import com.inventoryapp.rcapp.ui.nav.ROUTE_HOME_AGENT_SCREEN
import com.inventoryapp.rcapp.ui.theme.spacing
import kotlinx.coroutines.launch


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgentStockScreen(viewModel: AuthAgentViewModel?,navController: NavController){
    val agentProductViewModel = AgentProductViewModel()
    val internalProductViewModel = InternalProductViewModel()
    val bottomNavBarViewModel = BottomNavAgentViewModel()
    val spacing = MaterialTheme.spacing
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val searchText by agentProductViewModel.searchText.collectAsState()
    val isSearching by agentProductViewModel.isSearching.collectAsState()
    val agentProductList by agentProductViewModel.agentProductList.collectAsState()
    val searchInternalProduct by internalProductViewModel.searchText.collectAsState()
    val internalProductIsSearching by internalProductViewModel.isSearching.collectAsState()
    val internalProductList by internalProductViewModel.productsList.collectAsState()
    val sheetState = rememberModalBottomSheetState()
    var showAddProductSheet by remember { mutableStateOf(false) }
    var showDetailProductSheet by remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current
    var qtyProduct by remember { mutableStateOf("") }
    var descProduct by remember { mutableStateOf("") }
    val selectedCard = remember { mutableStateOf("") }
    val selectedProductNameHolder = StateHolder(initialValue = "")
    var isQtyEmpty = true
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
                        viewModel!!.logout()
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
                                ListProduct(item,
                                    onCardClicked = {
                                    })
                            }
                        }
                    }
                    LazyColumn (modifier = Modifier.padding(start = 8.dp, end = 8.dp, top =25.dp, bottom = 80.dp)){
                        items(internalProductList) { item ->
                            ListProduct(item,
                                onCardClicked = {
                                })
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
                                showAddProductSheet = true
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
            if (showAddProductSheet) {
                ModalBottomSheet(
                    onDismissRequest = {
                        showAddProductSheet = false
                    },
                    sheetState = sheetState,
                    containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
                ) {
                    ConstraintLayout (modifier = Modifier.fillMaxSize()){
                        val (refTitleSheet, refSearchBar, refListProduct, refBtnNext) = createRefs()
                        Text(text = "Tambahkan Produk",
                            modifier = Modifier.constrainAs(refTitleSheet){
                                top.linkTo(parent.top)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            },
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Medium))
                        Box (modifier =
                        if (internalProductIsSearching){
                            Modifier
                                .constrainAs(refSearchBar) {
                                    top.linkTo(refTitleSheet.bottom, spacing.small)
                                    start.linkTo(parent.start, spacing.large)
                                    end.linkTo(parent.end, spacing.large)
                                    bottom.linkTo(refBtnNext.top, spacing.small)
                                    height = Dimension.fillToConstraints
                                }
                        } else
                            Modifier.constrainAs(refSearchBar) {
                                top.linkTo(refTitleSheet.bottom)
                                start.linkTo(parent.start, spacing.large)
                                end.linkTo(parent.end, spacing.large)
                            }
                        ){
                            SearchBar(
                                query = searchInternalProduct,
                                onQueryChange = internalProductViewModel::onSearchTextChange,
                                onSearch = internalProductViewModel::onSearchTextChange,
                                active = internalProductIsSearching,
                                onActiveChange = { internalProductViewModel.onToogleSearch()},
                                trailingIcon = {
                                    Icon(imageVector = Icons.Rounded.Search, contentDescription = "cari" )
                                },
                                placeholder = {
                                    Text(text = "Pilih barang dulu...")
                                },
                                shadowElevation = 2.dp,
                                colors = SearchBarDefaults.colors(MaterialTheme.colorScheme.surfaceContainerLowest)
                            ) {
                                LazyColumn {
                                    items(internalProductList) { product ->
                                        CardItem(
                                            cardData = product,
                                            selectedCard = selectedCard,
                                            onCardClicked = {productName ->
                                                selectedProductNameHolder.updateValue(productName)
                                                println("Nama produk: $productName")}
                                        )
                                    }
                                }
                            }
                        }
                        LazyColumn (modifier = Modifier
                            .constrainAs(refListProduct) {
                                top.linkTo(refSearchBar.bottom)
                                start.linkTo(parent.start, spacing.medium)
                                end.linkTo(parent.end, spacing.medium)
                                bottom.linkTo(refBtnNext.top, spacing.extraSmall)
                                height = Dimension.fillToConstraints
                            }
                            .padding(horizontal = 10.dp, vertical = 10.dp)
                        )
                        {
                            items(internalProductList) { internalitem ->
                                CardItem(internalitem, selectedCard,
                                    onCardClicked = { productName ->
                                        selectedProductNameHolder.updateValue(productName)
                                        println("Nama produk: $productName")
                                    }) // Replace with your composable for each item
                            }
                        }
                        Button(
                            onClick = {
                                if (selectedCard.value.isEmpty()){
                                    Toast.makeText(context,"Pilih barang terlebih dahulu!", Toast.LENGTH_SHORT).show()
                                } else{
                                    showDetailProductSheet = true
                                }
                            },
                            modifier = if (internalProductIsSearching){
                                Modifier
                                    .constrainAs(refBtnNext) {
                                        top.linkTo(refSearchBar.bottom)
                                        end.linkTo(refSearchBar.end)
                                        bottom.linkTo(parent.bottom, spacing.large)
                                    }
                                    .padding(end = 18.dp)
                            } else Modifier
                                .constrainAs(refBtnNext) {
                                    bottom.linkTo(parent.bottom, spacing.large)
                                    end.linkTo(refListProduct.end)
                                }
                                .padding(end = 18.dp)
                        ) {
                            Text(text = "Selanjutnya")
                        }
                    }
                }
            }
            if (showDetailProductSheet) {
                ModalBottomSheet(
                    onDismissRequest = {
                        showDetailProductSheet = false
                        showAddProductSheet = false
                    },
                    sheetState = sheetState
                ) {
                    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
                        val (refTitleSheet, refProductName, refQtyMin, refDescProduct, refBtnSave) = createRefs()
                        Text(
                            text = "Tambahkan Detail Produk",
                            modifier = Modifier
                                .constrainAs(refTitleSheet) {
                                    top.linkTo(parent.top)
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                }
                                .padding(vertical = 10.dp),
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Medium)
                        )
                        Text(
                            text = selectedProductNameHolder.value ,
                            modifier = Modifier.constrainAs(refProductName) {
                                top.linkTo(refTitleSheet.bottom, spacing.medium)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                                width = Dimension.wrapContent
                            },
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Medium)
                        )
                        OutlinedTextField(
                            value = qtyProduct,
                            onValueChange = {
                                qtyProduct = it
                                isQtyEmpty = it.isEmpty()
                            },
                            isError = isQtyEmpty,
                            label = {
                                Text(text = "Stok Minimum")
                            },
                            modifier = Modifier.constrainAs(refQtyMin) {
                                top.linkTo(refProductName.bottom, spacing.medium)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                                width = Dimension.wrapContent
                            },
                            keyboardOptions = KeyboardOptions(
                                capitalization = KeyboardCapitalization.None,
                                autoCorrect = false,
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Done
                            ),
                            placeholder = {
                                Text(text = "Ada berapa barang yang masuk ?")
                            },
                            trailingIcon = {
                                if (isQtyEmpty) {
                                    Icon(Icons.Outlined.Info, contentDescription = "isi dahulu")
                                } else{
                                    Icon(imageVector = Icons.Outlined.Done, contentDescription ="done" )
                                }
                            }
                        )
                        OutlinedTextField(
                            value = descProduct,
                            onValueChange = {
                                descProduct = it
                            },
                            label = {
                                Text(text = "Keterangan")
                            },
                            modifier = Modifier.constrainAs(refDescProduct) {
                                top.linkTo(refQtyMin.bottom, spacing.medium)
                                start.linkTo(parent.start, spacing.large)
                                end.linkTo(parent.end, spacing.large)
                            },
                            keyboardOptions = KeyboardOptions(
                                capitalization = KeyboardCapitalization.None,
                                autoCorrect = false,
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Done
                            )
                        )
                        Button(
                            onClick = {
                                if (isQtyEmpty) {
                                    // Tampilkan pesan error
                                    Toast.makeText(context, "Jumlah stok minimum tidak boleh kosong", Toast.LENGTH_SHORT).show()
                                }else {
                                    isQtyEmpty= false
                                    navController.navigate(ROUTE_HOME_AGENT_SCREEN)
                                }
                            },
                            modifier = Modifier
                                .constrainAs(refBtnSave) {
                                    top.linkTo(refDescProduct.bottom, spacing.medium)
                                    start.linkTo(refDescProduct.start)
                                    end.linkTo(refDescProduct.end)
                                }
                                .padding(top = 10.dp)
                                .width(200.dp)
                        ) {
                            Text(text = "Simpan")
                        }
                    }
                }
            }
        }
    }

}

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun CardItem(
    cardData: InternalProduct,
    selectedCard: MutableState<String>,
    onCardClicked: (String) -> Unit
) {
//    val selectedCard = remember { mutableStateOf("") }
//    var cardClicked = remember { mutableStateOf(false) }
//    val color = when (cardClicked.value) {
//        true -> CardColors(MaterialTheme.colorScheme.surfaceContainerLowest, MaterialTheme.colorScheme.onSurface, MaterialTheme.colorScheme.tertiaryContainer, MaterialTheme.colorScheme.tertiary)
//        else ->CardColors(MaterialTheme.colorScheme.surfaceContainerLowest, MaterialTheme.colorScheme.onSurface, MaterialTheme.colorScheme.tertiaryContainer, MaterialTheme.colorScheme.tertiary)
//    }
    Card(
        modifier = Modifier
            .padding(8.dp)
            .clickable {
                selectedCard.value = cardData.idProduct
                onCardClicked(cardData.productName)
            }
            .height(80.dp) ,
        colors = CardColors(MaterialTheme.colorScheme.surfaceContainerLowest, MaterialTheme.colorScheme.onSurface, MaterialTheme.colorScheme.tertiaryContainer, MaterialTheme.colorScheme.onSurface),
        elevation = CardDefaults.cardElevation(2.dp,10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Bagian kiri
            Row(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 20.dp)
            ) {
                Image(imageVector = ImageVector.vectorResource(id = R.drawable.bag_icon),
                    contentDescription = "Favorite",
                    modifier = Modifier.padding(horizontal = 10.dp))
                Text(text = cardData.productName,
                    style = MaterialTheme.typography.titleLarge)
            }
            // Bagian kanan
            RadioButton(
                selected = cardData.idProduct == selectedCard.value,
                onClick = {
                    selectedCard.value = cardData.idProduct
                    onCardClicked(cardData.productName)
                          }
            )
        }
    }
}

//@Preview(apiLevel = 33)
//@Composable
//fun prevProductListOnly(){
//    ProductListOnly()
//}

@Preview(apiLevel = 33)
@Composable
fun PrevAgentStockScreen(){
//    AgentStockScreen(navController = rememberNavController())
}