package com.inventoryapp.rcapp.ui.agentnav

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.text2.input.TextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.core.text.isDigitsOnly
import androidx.navigation.NavController
import com.inventoryapp.rcapp.R
import com.inventoryapp.rcapp.data.model.AgentProduct
import com.inventoryapp.rcapp.data.model.InternalProduct
import com.inventoryapp.rcapp.ui.viewmodel.AgentProductViewModel
import com.inventoryapp.rcapp.ui.viewmodel.InternalProductViewModel
import com.inventoryapp.rcapp.ui.nav.ROUTE_AGENT_STOCK_SCREEN
import com.inventoryapp.rcapp.ui.nav.ROUTE_HOME_INTERNAL_SCREEN
import com.inventoryapp.rcapp.ui.nav.ROUTE_INTERNAL_STOCK_SCREEN
import com.inventoryapp.rcapp.ui.theme.spacing
import com.inventoryapp.rcapp.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import java.util.Date


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "SuspiciousIndentation",
    "StateFlowValueCalledInComposition"
)
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun AgentStockScreen(
    agentProductViewModel: AgentProductViewModel?,
    internalProductViewModel: InternalProductViewModel?,
    navController: NavController
){
    val spacing = MaterialTheme.spacing
    val searchText by agentProductViewModel!!.searchText.collectAsState()
    val isSearching by agentProductViewModel!!.isSearching.collectAsState()
    val agentProductList by agentProductViewModel!!.agentProductList.collectAsState()
    val searchInternalProduct by internalProductViewModel!!.searchText.collectAsState()
    val internalProductIsSearching by internalProductViewModel!!.isSearching.collectAsState()
    val internalProductsForSearch by internalProductViewModel!!.internalProductList.collectAsState()
    val sheetState = rememberModalBottomSheetState(true)
    var showAddProductSheet by remember { mutableStateOf(false) }
    var showDetailProductSheet by remember {
        mutableStateOf(false)
    }

    val addAgentProductResource = agentProductViewModel?.agentProductFlow?.collectAsState()
    val modelEditResource = agentProductViewModel?.agentProductEditFlow?.collectAsState()
    val agentProducts by agentProductViewModel!!.agentProducts.observeAsState()
    val internalProducts by internalProductViewModel!!.internalProducts.observeAsState()

    val context = LocalContext.current
    var qtyProduct by remember { mutableStateOf("") }
    var descProduct by remember { mutableStateOf("") }
    val selectedCard = remember { mutableStateOf("") }
//    val selectedProductNameHolder = StateHolder(initialValue = "")
    var qtyMinProduct by remember { mutableStateOf("") }
    var showEditProductSheet by remember {
        mutableStateOf(false)
    }
    var selectedProduct by remember {
        mutableStateOf("")
    }
    var selectedItemId by remember {
        mutableStateOf("")
    }
    var selectedItemDiscount by remember {
        mutableIntStateOf(0)
    }
    var selectedItemPrice by remember {
        mutableLongStateOf(0)
    }
    var selectedItemFinalPrice by remember {
        mutableLongStateOf(0)
    }
    val selectedData = MutableStateFlow<AgentProduct?>(null)
    var isQtyEmpty = true
    var isEditQtyMinProductEmpty = true

    Scaffold(
        containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
        topBar = {
            Column (
                modifier = Modifier.padding(top = 55.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                SearchBar(
                    query = searchText,//text showed on SearchBar
                    onQueryChange = agentProductViewModel!!::onSearchTextChange, //update the value of searchText
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
                            ListProductAgent(item,
                                onCardClicked = {
                                    selectedData.value = it
                                    showEditProductSheet = true
                                })
                        }
                    }
                }

                LaunchedEffect(Unit) {
                    agentProductViewModel.fetchAgentProducts()
                }
                when (agentProducts) {
                    is Resource.Success -> {
                        val agentProduct = (agentProducts as Resource.Success<List<AgentProduct>>).result
                        if (agentProduct.isEmpty()){
                            Text(
                                modifier = Modifier.padding(top=20.dp),
                                text = "Data masih kosong")
                        }
                        else {
                            LazyColumn (modifier = Modifier.padding(start = 8.dp, end = 8.dp, top =25.dp, bottom = 80.dp)){
                                items(agentProduct) { item ->
                                    ListProductAgent(item,
                                        onCardClicked = {
                                            selectedData.value = it
                                            showEditProductSheet = true
                                        })
                                }
                            }
                        }
                    }
                    is Resource.Loading -> {
                        // Tampilkan indikator loading jika diperlukan
                        CircularProgressIndicator(
                            modifier = Modifier
                                .padding(horizontal = 10.dp, vertical = 10.dp)
                        )
                    }
                    is Resource.Failure -> {
                        // Tampilkan pesan error jika diperlukan
                        val error = (agentProducts as Resource.Failure).throwable
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
                    Text(
                        modifier = Modifier.padding(vertical = 6.dp),
                        text = selectedData.value?.productName!!,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Normal
                        )
                    )
                    OutlinedTextField(
                        value = qtyMinProduct,
                        onValueChange = {
                            qtyMinProduct = it
                            isEditQtyMinProductEmpty = it.isEmpty()
                        },
                        isError = isEditQtyMinProductEmpty,
                        label = {
                            Text(text = "Stok Minimum")
                        },
                        modifier = Modifier.padding(vertical = 5.dp),
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.None,
                            autoCorrect = false,
                            keyboardType = KeyboardType.Number,
                            imeAction = ImeAction.Done
                        ),
                        maxLines = 1,
                        suffix = { Text(text = "Pcs")},
                        placeholder = {
                            Text(text = selectedData.value?.qtyMin.toString())
                        },
                        trailingIcon = {
                            if (isEditQtyMinProductEmpty) {
                                Icon(Icons.Outlined.Info, contentDescription = "isi dahulu")
                            } else{
                                Icon(imageVector = Icons.Outlined.Done, contentDescription ="done" )
                            }
                        }
                    )
                    fun getAgentProductForUpdate(): AgentProduct {
                        val qtyMinProductValue = qtyMinProduct.toIntOrNull() ?: 0
                        return AgentProduct(
                            idProduct = selectedData.value?.idProduct,
                            qtyMin = qtyMinProductValue
                        )
                    }
                    val updatedObj: AgentProduct = getAgentProductForUpdate()
                    Button(
                        modifier = Modifier.padding(top = 10.dp, bottom = 20.dp),
                        onClick = {
                            if (isEditQtyMinProductEmpty) {
                                // Tampilkan pesan error
                                Toast.makeText(
                                    context,
                                    "Jumlah minimum tidak boleh kosong",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                agentProductViewModel?.editInternalProduct(updatedObj)
                                showEditProductSheet = false
//                                Toast.makeText(
//                                    context,
//                                    "Sukses edit barang",
//                                    Toast.LENGTH_SHORT
//                                ).show()
                            }
                        }
                    ) {
                        Text(text = "Simpan Perubahan")
                    }
                    modelEditResource?.value?.let {
                        when (it) {
                            is Resource.Failure -> {
                                Toast.makeText(context, it.throwable.message, Toast.LENGTH_SHORT).show()
                            }
                            is Resource.Loading -> {
                                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                            }
                            is Resource.Success -> {
                                Toast.makeText(
                                    context,
                                    "Sukses edit barang",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
            }
        }
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
                            onQueryChange = internalProductViewModel!!::onSearchTextChange,
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
                                items(internalProductsForSearch) { product ->
                                    CardItem(
                                        cardData = product,
                                        selectedCard = selectedCard,
                                        onCardClicked = {productName ->
//                                            selectedProductNameHolder.updateValue(productName)
                                            selectedProduct = productName
//                                            println("Nama produk: $productName")
                                        },
                                        idInternalProduct = {id ->
                                            selectedItemId = id
                                        },
                                        price = {selectedItemPrice = it},
                                        finalPrice = {selectedItemFinalPrice = it},
                                        discount = {selectedItemDiscount = it}
                                    )
                                }
                            }
                        }
                    }
                    LaunchedEffect(Unit) {
                        internalProductViewModel!!.fetchInternalProducts()
                    }

                    when (internalProducts) {
                        is Resource.Success -> {
                            val internalProductList = (internalProducts as Resource.Success<List<InternalProduct>>).result
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
                                            selectedProduct = productName
                                        },
                                        idInternalProduct = {
                                            selectedItemId = it
                                        },
                                        price = {selectedItemPrice = it},
                                        finalPrice = {selectedItemFinalPrice = it},
                                        discount = {selectedItemDiscount = it}
                                    ) // Replace with your composable for each item
                                }
                            }
                        }
                        is Resource.Loading -> {
                            // Tampilkan indikator loading jika diperlukan
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .constrainAs(refListProduct) {
                                        top.linkTo(refSearchBar.bottom)
                                        start.linkTo(parent.start, spacing.medium)
                                        end.linkTo(parent.end, spacing.medium)
                                        bottom.linkTo(refBtnNext.top, spacing.extraSmall)
                                        height = Dimension.fillToConstraints
                                    }
                                    .padding(horizontal = 10.dp, vertical = 10.dp)
                            )
                        }
                        is Resource.Failure -> {
                            // Tampilkan pesan error jika diperlukan
                            val error = (internalProducts as Resource.Failure).throwable
                            Text(text = "Error: ${error.message}")
                        }
                        else -> {
                            // Tampilkan pesan default jika diperlukan
                            Text(text = "No data available")
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
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                    ) {
//                    val (refTitleSheet, refProductName, refQtyMin, refDescProduct, refBtnSave) = createRefs()
                    Text(
                        text = "Tambahkan Detail Produk",
                        modifier = Modifier
                            .padding(vertical = 10.dp),
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Medium)
                    )
                    Text(
                        text = selectedProduct ,
                        modifier = Modifier,
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Medium)
                    )
                    TextFieldState()
                    OutlinedTextField(
                        value = qtyProduct,
                        onValueChange = {
                            if (it.isDigitsOnly())
                            qtyProduct = it
                            isQtyEmpty = it.isEmpty()
                                        },
                        isError = isQtyEmpty,
                        label = {
                            Text(text = "Stok Minimum")
                        },
                        modifier = Modifier
                        ,
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
                        modifier = Modifier,
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.None,
                            autoCorrect = false,
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Done
                        )
                    )
                    fun getAgentProduct(): AgentProduct {
                        val qtyMinProductValue = qtyProduct.toIntOrNull() ?: 0
                        return AgentProduct(
                            idProduct = selectedItemId,
                            productName = selectedProduct,
                            qtyProduct = 0,
                            qtyMin = qtyMinProductValue,
                            updateAt = Date(),
                            desc = descProduct
                        )
                    }
                    val agentProductObj: AgentProduct = getAgentProduct()
                    Button(
                        onClick = {
                            if (isQtyEmpty) {
                                // Tampilkan pesan error
                                Toast.makeText(context, "Jumlah stok minimum tidak boleh kosong", Toast.LENGTH_SHORT).show()
                            }else {
                                agentProductViewModel?.addAgentProduct(agentProductObj)
                                Toast.makeText(
                                    context,
                                    "Sukses menambahkan barang",
                                    Toast.LENGTH_SHORT
                                ).show()
                                navController.navigate(ROUTE_AGENT_STOCK_SCREEN)
                            }
                        },
                        modifier = Modifier
                            .padding(top = 10.dp, bottom = 25.dp)
                            .width(200.dp)
                    ) {
                        Text(text = "Simpan")
                    }
                    addAgentProductResource?.value?.let {
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
    }
}

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun CardItem(
    cardData: InternalProduct,
    selectedCard: MutableState<String>,
    onCardClicked: (String) -> Unit,
    idInternalProduct: (String) -> Unit,
    price: (Long) -> Unit,
    discount: (Int) -> Unit,
    finalPrice: (Long) -> Unit
) {
//    val selectedCard = remember { mutableStateOf("") }
//    var cardClicked = remember { mutableStateOf(false) }
//    val color = when (cardClicked.value) {
//        true -> CardColors(MaterialTheme.colorScheme.surfaceContainerLowest, MaterialTheme.colorScheme.onSurface, MaterialTheme.colorScheme.tertiaryContainer, MaterialTheme.colorScheme.tertiary)
//        else -> CardColors(MaterialTheme.colorScheme.surfaceContainerLowest, MaterialTheme.colorScheme.onSurface, MaterialTheme.colorScheme.tertiaryContainer, MaterialTheme.colorScheme.tertiary)
//    }
    Card(
        modifier = Modifier
            .padding(8.dp)
            .clickable {
                selectedCard.value = cardData.idProduct!!
                onCardClicked(cardData.productName!!)
                idInternalProduct(cardData.idProduct!!)
                price(cardData.price!!)
                discount(cardData.discProduct!!)
                finalPrice(cardData.finalPrice!!)
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
                Text(text = cardData.productName!!,
                    style = MaterialTheme.typography.titleLarge)
            }
            // Bagian kanan
            RadioButton(
                selected = cardData.idProduct == selectedCard.value,
                onClick = {
                    selectedCard.value = cardData.idProduct!!
                    onCardClicked(cardData.productName!!)
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

@Preview(apiLevel = 34)
@Composable
fun PrevAgentStockScreen(){
//    AgentStockScreen(navController = rememberNavController())
}