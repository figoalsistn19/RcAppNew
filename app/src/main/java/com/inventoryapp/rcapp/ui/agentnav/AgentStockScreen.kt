package com.inventoryapp.rcapp.ui.agentnav

import android.annotation.SuppressLint
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.inventoryapp.rcapp.R
import com.inventoryapp.rcapp.data.model.InternalProduct
import com.inventoryapp.rcapp.ui.agentnav.viewmodel.AgentProductViewModel
import com.inventoryapp.rcapp.ui.agentnav.viewmodel.InternalProductViewModel
import com.inventoryapp.rcapp.ui.theme.spacing
import kotlinx.coroutines.flow.MutableStateFlow


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgentStockScreen(){
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
    var showAddProductSheet by remember { mutableStateOf(false) }
    var showDetailProductSheet by remember {
        mutableStateOf(false)
    }
    var qtyProduct by remember { mutableStateOf("") }
    var descProduct by remember { mutableStateOf("") }
    val selectedCard = remember { mutableStateOf("") }
    Scaffold(
        topBar = {
            Column {
                CenterAlignedTopAppBar(
                    title = {
                        Text(text = "Produk",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Medium))
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        titleContentColor = MaterialTheme.colorScheme.onSurface,
                    ),
                    navigationIcon = {},
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
                        .padding(start = 23.dp, top = 10.dp, end = 23.dp)
                ) {
                    LazyColumn (modifier = Modifier.padding(horizontal = 8.dp, vertical =10.dp)){
                        items(agentProductList) { item ->
                            ListItem(item)
                        }
                    }
                }
                LazyColumn (modifier = Modifier.padding(start = 8.dp, end = 8.dp, top =25.dp)){
                    items(internalProductList) { item ->
                        ListItem(item)
                    }
                }
//                Image(
//                    imageVector = ImageVector.vectorResource(id = R.drawable.firstpage),
//                    contentDescription = "ini logo",
//                    modifier = Modifier
//                        .fillMaxHeight()
//                        .padding(vertical = 20.dp))
            }
        },
        bottomBar = {
            Row(modifier = Modifier.fillMaxWidth()) {
                // Your other UI content here
                Spacer(modifier = Modifier.weight(1f)) // Add flexibility with weight
                ExtendedFloatingActionButton(
                    onClick = { showAddProductSheet = true },
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
        }
    ) {
        if (showAddProductSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showAddProductSheet = false
                },
                sheetState = sheetState
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
                    SearchBar(
                        query = searchInternalProduct,
                        onQueryChange = internalProductViewModel::onSearchTextChange,
                        onSearch = internalProductViewModel::onSearchTextChange,
                        active = internalProductIsSearching,
                        onActiveChange = { internalProductViewModel.onToogleSearch()},
                        modifier = Modifier.constrainAs(refSearchBar){
                            top.linkTo(refTitleSheet.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                    ) {
                        LazyColumn {
                            items(internalProductList) { product ->
                                CardItem(cardData = product, selectedCard = selectedCard )
                            }
                        }
                    }
                    LazyColumn (modifier = Modifier
                        .constrainAs(refListProduct) {
                            top.linkTo(refSearchBar.bottom)
                            start.linkTo(parent.start, spacing.medium)
                            end.linkTo(parent.end, spacing.medium)
                        }
                        .padding(horizontal = 10.dp, vertical = 10.dp)
                        .height(500.dp)
                    )
                    {
                        items(internalProductList) { internalitem ->
                            CardItem(internalitem, selectedCard) // Replace with your composable for each item
                        }
                    }
                    Button(
                        onClick = {
                            showDetailProductSheet=true
                                  },
                        modifier = Modifier
                            .constrainAs(refBtnNext) {
                                top.linkTo(refListProduct.bottom, spacing.medium)
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
                    val (refTitleSheet, refQtyMin, refDescProduct, refBtnSave) = createRefs()
                    Text(
                        text = "Tambahkan Detail Produk",
                        modifier = Modifier.constrainAs(refTitleSheet) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }.padding(vertical = 10.dp),
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Medium)
                    )
                    OutlinedTextField(
                        value = qtyProduct,
                        onValueChange = {
                            qtyProduct = it
                        },
                        label = {
                            Text(text = "Tulis jumlah stok minimum produk")
                        },
                        modifier = Modifier.constrainAs(refQtyMin) {
                            top.linkTo(refTitleSheet.bottom, spacing.medium)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            width = Dimension.wrapContent
                        },
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.None,
                            autoCorrect = false,
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        )
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

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun CardItem(cardData: InternalProduct, selectedCard: MutableState<String>) {
//    val selectedCard = remember { mutableStateOf("") }
    ElevatedCard(
        modifier = Modifier
            .padding(8.dp)
            .clickable {
                selectedCard.value = cardData.idProduct
            }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Bagian kiri
            Row(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 20.dp)
            ) {
                Icon(imageVector = ImageVector.vectorResource(id = R.drawable.bag_icon),
                    contentDescription = "Favorite",
                    modifier = Modifier.padding(horizontal = 10.dp),
                    tint = MaterialTheme.colorScheme.primary)
                Text(text = cardData.productName,
                    style = MaterialTheme.typography.titleLarge)
            }
            // Bagian kanan
            RadioButton(
                selected = cardData.idProduct == selectedCard.value,
                onClick = {  },
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
    AgentStockScreen()
}