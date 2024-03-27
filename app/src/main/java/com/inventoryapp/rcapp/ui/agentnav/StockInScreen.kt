package com.inventoryapp.rcapp.ui.agentnav

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
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
import com.inventoryapp.rcapp.ui.agentnav.viewmodel.AgentProductViewModel
import com.inventoryapp.rcapp.ui.agentnav.viewmodel.InternalProductViewModel
import com.inventoryapp.rcapp.ui.agentnav.viewmodel.StateHolder
import com.inventoryapp.rcapp.ui.nav.ROUTE_HOME_AGENT_SCREEN
import com.inventoryapp.rcapp.ui.theme.spacing
import java.text.SimpleDateFormat

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StockInScreen(navController: NavController){
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val agentProductViewModel = AgentProductViewModel()
    val internalProductViewModel = InternalProductViewModel()
    val spacing = MaterialTheme.spacing
    val searchText by agentProductViewModel.searchText.collectAsState()
    val isSearching by agentProductViewModel.isSearching.collectAsState()
    val agentProductList by agentProductViewModel.agentProductList.collectAsState()
    val searchInternalProduct by internalProductViewModel.searchText.collectAsState()
    val internalProductIsSearching by internalProductViewModel.isSearching.collectAsState()
    val internalProductList by internalProductViewModel.productsList.collectAsState()
    val sheetState = rememberModalBottomSheetState()
    var showAddStockInSheet by remember { mutableStateOf(false) }
    var showDetailStockInSheet by remember {
        mutableStateOf(false)
    }
    var isQtyEmpty = true
    val context = LocalContext.current
    var qtyStockIn by remember { mutableStateOf("") }
    var productName by remember { mutableStateOf("") }
    var descStockIn by remember { mutableStateOf("") }
    val selectedCard = remember { mutableStateOf("") }
    val selectedProductNameHolder = StateHolder(initialValue = "")
    Scaffold (
        containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Barang Masuk",
                        maxLines = 1,
                        overflow = TextOverflow.Clip,
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Medium))
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                ),
                navigationIcon = {
                    IconButton(onClick = { navController.navigate(ROUTE_HOME_AGENT_SCREEN) }) {
                        Icon(imageVector = ImageVector.vectorResource(id = R.drawable.back_btn),
                            contentDescription = "tombol kembali",
                            tint = MaterialTheme.colorScheme.onSurface)
                    }
                },
                actions = {},
                scrollBehavior = scrollBehavior)
        },
        bottomBar ={
            Column {
                Row(modifier = Modifier.fillMaxWidth()) {
                    // Your other UI content here
                    Spacer(modifier = Modifier.weight(1f)) // Add flexibility with weight
                    ExtendedFloatingActionButton(
                        onClick = {
                            showAddStockInSheet = true
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
        Column (
            modifier = Modifier.padding(top = 90.dp)
        ) {
        SearchBar(
            query = searchText,
            onQueryChange = agentProductViewModel::onSearchTextChange, //update the value of searchText
            onSearch = agentProductViewModel::onSearchTextChange, //the callback to be invoked when the input service triggers the ImeAction.Search action
            active = isSearching, //whether the user is searching or not
            onActiveChange = { agentProductViewModel.onToogleSearch() }, //the callback to be invoked when this search bar's active state is changed
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            trailingIcon = {
                Icon(imageVector = Icons.Rounded.Search, contentDescription = "cari" )
            },
            placeholder = {
                Text(text = "Cek riwayat barang masuk...")
            }
        ) {
            LazyColumn (modifier = Modifier.padding(horizontal = 8.dp, vertical =10.dp)){
                items(agentProductList) { item ->
                    ListItemForInOut(item)
                }
            }
        }
        LazyColumn (modifier = Modifier.padding(start = 8.dp, end = 8.dp, top =25.dp, bottom = 80.dp)){
            items(internalProductList) { item ->
                ListItemForInOut(item)
            }
        }
    }
        if (showAddStockInSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showAddStockInSheet = false
                },
                sheetState = sheetState
            ) {
                ConstraintLayout(modifier = Modifier.fillMaxSize()) {
                    val (refTitleSheet, refSearchBar, refListProduct, refBtnNext) = createRefs()
                    Text(
                        text = "Tambahkan barang masuk",
                        modifier = Modifier.constrainAs(refTitleSheet) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            height = Dimension.wrapContent
                        },
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Medium)
                    )
                    Box (
                        modifier =
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
                            onActiveChange = { internalProductViewModel.onToogleSearch() },
                            trailingIcon = {
                                Icon(imageVector = Icons.Rounded.Search, contentDescription = "cari" )
                            },
                            placeholder = {
                                Text(text = "Pilih barang dulu...")
                            }
                        ) {
                            LazyColumn (modifier = Modifier.padding(horizontal = 10.dp)){
                                items(internalProductList) { product ->
                                    CardItem(cardData = product, selectedCard = selectedCard,
                                        onCardClicked = { productName ->
                                            selectedProductNameHolder.updateValue(productName)
                                            println("Nama produk: $productName")
                                        })
                                }
                            }
                        }
                    }

//                    SearchBar(
//                        query = searchInternalProduct,
//                        onQueryChange = internalProductViewModel::onSearchTextChange,
//                        onSearch = internalProductViewModel::onSearchTextChange,
//                        isActive = internalProductIsSearching,
//                        modifier = Modifier.constrainAs(refSearchBar) {
//                            top.linkTo(refTitleSheet.bottom)
//                            start.linkTo(parent.start, spacing.large)
//                            end.linkTo(parent.end, spacing.large)
//                        }
//                    )
//
//                    if (internalProductList.isNotEmpty()) {
//                        LazyColumn(modifier = Modifier.fillMaxWidth()) {
//                            items(internalProductList) { product ->
//                                CardItem(cardData = product, selectedCard = selectedCard,
//                                    onCardClicked = { productName ->
//                                        selectedProductNameHolder.updateValue(productName)
//                                        println("Nama produk: $productName")
//                                    })
//                            }
//                        }
//                    }
//                    SearchBarSample()
                    if (internalProductIsSearching){

                    } else {
                        Box(modifier = Modifier
                            .constrainAs(refListProduct) {
                                top.linkTo(refSearchBar.bottom)
                                start.linkTo(parent.start, spacing.medium)
                                end.linkTo(parent.end, spacing.medium)
                                bottom.linkTo(refBtnNext.top, spacing.small)
                                height = Dimension.fillToConstraints
                            }
                            .padding(horizontal = 10.dp, vertical = 10.dp))
                        {
                            LazyColumn(modifier = Modifier
                                .padding(horizontal = 10.dp, vertical = 10.dp)
                            )
                            {
                                items(internalProductList) { internalitem ->
                                    CardItem(
                                        internalitem,
                                        selectedCard,
                                        onCardClicked = { productName ->
                                            selectedProductNameHolder.updateValue(productName)
                                            println("Nama produk: $productName")
                                        }
                                    ) // Replace with your composable for each item
                                }
                            }
                        }

                    }

                    Button(
                        onClick = {
                            if (selectedCard.value.isEmpty()){
                                Toast.makeText(context,"Pilih barang terlebih dahulu!", Toast.LENGTH_SHORT).show()
                            } else{
                                showDetailStockInSheet = true
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

//                        Modifier
//                            .constrainAs(refBtnNext) {
//                                top.linkTo(refListProduct.bottom, spacing.medium)
//                                end.linkTo(refListProduct.end)
//                            }
//                            .padding(end = 18.dp)
                    ) {
                        Text(text = "Selanjutnya")
                    }
                }
            }
        }
        if (showDetailStockInSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showDetailStockInSheet = false
                    showAddStockInSheet = false
                },
                sheetState = sheetState
            ) {
                ConstraintLayout(modifier = Modifier.fillMaxSize()) {
                    val (refTitleSheet, refQtyMin, refProductName, refDescProduct, refBtnSave) = createRefs()
                    Text(
                        text = "Tambahkan Detail Barang Masuk",
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
                        value = qtyStockIn,
                        onValueChange = {
                            qtyStockIn = it
                            isQtyEmpty = it.isEmpty()
                        },
                        isError = isQtyEmpty,
                        maxLines = 1,
                        label = {
                            Text(text = "Stok masuk")
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
                        value = descStockIn,
                        onValueChange = {
                            descStockIn = it
                        },
                        maxLines = 1,
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
                                Toast.makeText(context, "jumlah stok masuk tidak boleh kosong", Toast.LENGTH_SHORT).show()
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

@SuppressLint("SimpleDateFormat")
@Composable
fun ListItemForInOut(item: InternalProduct) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp, horizontal = 10.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        ),
        colors = CardColors(contentColor = MaterialTheme.colorScheme.onSurface, containerColor = MaterialTheme.colorScheme.surfaceContainerLowest, disabledContentColor = MaterialTheme.colorScheme.onSurface, disabledContainerColor = MaterialTheme.colorScheme.onTertiaryContainer)
    ) {
        ConstraintLayout (modifier = Modifier.fillMaxWidth()) {
            val sdf = SimpleDateFormat("dd MMM yyyy ・ HH:mm")
            val date = item.updateAt
            val fixDate = sdf.format(date)
            val spacing = MaterialTheme.spacing
            val (refIcon, refTitle, refDate, refStock) = createRefs()
            Image(modifier = Modifier
                .constrainAs(refIcon){
                    top.linkTo(parent.top, spacing.medium)
                    start.linkTo(parent.start, spacing.medium)
                    bottom.linkTo(parent.bottom, spacing.medium)
                },
                imageVector = ImageVector.vectorResource(id = R.drawable.bag_icon), contentDescription = "ini icon")
            Text(modifier = Modifier
                .constrainAs(refTitle){
                    top.linkTo(parent.top)
                    start.linkTo(refIcon.end, spacing.small)
                    bottom.linkTo(parent.bottom, spacing.medium)
                },
                text = item.productName, style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Medium))
            Text(modifier = Modifier
                .constrainAs(refDate){
                    top.linkTo(refTitle.bottom)
                    start.linkTo(refIcon.end, spacing.small)
                    bottom.linkTo(parent.bottom, spacing.medium)
                },
                text = fixDate,
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Light))
            Text(modifier = Modifier
                .constrainAs(refStock){
                    top.linkTo(parent.top)
                    end.linkTo(parent.end, spacing.small)
                    bottom.linkTo(parent.bottom)
                },
                text = item.qtyProduct.toString()+ " pcs",
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium))
        }

    }
}

@Preview(apiLevel = 33)
@Composable
fun PrevStockInScreen(){
    StockInScreen(navController = rememberNavController())
}