package com.inventoryapp.rcapp.ui.agentnav

import android.annotation.SuppressLint
import android.widget.Toast
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
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.core.text.isDigitsOnly
import androidx.navigation.NavController
import com.inventoryapp.rcapp.R
import com.inventoryapp.rcapp.data.model.AgentProduct
import com.inventoryapp.rcapp.data.model.AgentStockTransaction
import com.inventoryapp.rcapp.data.model.InternalProduct
import com.inventoryapp.rcapp.data.model.OfferingBySales
import com.inventoryapp.rcapp.ui.agentnav.viewmodel.AgentProductViewModel
import com.inventoryapp.rcapp.ui.agentnav.viewmodel.AgentTransactionViewModel
import com.inventoryapp.rcapp.ui.agentnav.viewmodel.internalProducts
import com.inventoryapp.rcapp.ui.nav.ROUTE_HOME_AGENT_SCREEN
import com.inventoryapp.rcapp.ui.theme.spacing
import com.inventoryapp.rcapp.util.Resource
import java.text.SimpleDateFormat
import java.util.Date

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "SuspiciousIndentation")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StockInScreen(
    agentTransactionViewModel: AgentTransactionViewModel,
    agentProductViewModel: AgentProductViewModel,
    navController: NavController
){
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val spacing = MaterialTheme.spacing

    val searchText by agentTransactionViewModel.searchText.collectAsState()
    val isSearching by agentTransactionViewModel.isSearching.collectAsState()
    val agentTransactionList by agentTransactionViewModel.agentTransactionList.collectAsState()

    val searchAgentProduct by agentProductViewModel.searchText.collectAsState()
    val agentProductIsSearching by agentProductViewModel.isSearching.collectAsState()
    val agentProductList by agentProductViewModel.agentProductList.collectAsState()

    val agentProducts by agentProductViewModel.agentProducts.observeAsState()
    val addStockIn = agentTransactionViewModel.addProductInFlow.collectAsState()
    val agentStocksIn by agentTransactionViewModel.agentTransactionsIn.observeAsState()

    val sheetState = rememberModalBottomSheetState()
    var showAddStockInSheet by remember { mutableStateOf(false) }
    var showDetailStockInSheet by remember {
        mutableStateOf(false)
    }
    var isQtyEmpty = true
    val context = LocalContext.current
    var qtyStockIn by remember { mutableStateOf("") }
    var descStockIn by remember { mutableStateOf("") }
    val selectedCard = remember { mutableStateOf("") }
    var selectedProduct by remember {
        mutableStateOf("")
    }
    var selectedItemId by remember {
        mutableStateOf("")
    }

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
            onQueryChange = agentTransactionViewModel::onSearchTextChange, //update the value of searchText
            onSearch = agentTransactionViewModel::onSearchTextChange, //the callback to be invoked when the input service triggers the ImeAction.Search action
            active = isSearching, //whether the user is searching or not
            onActiveChange = { agentTransactionViewModel.onToogleSearch() }, //the callback to be invoked when this search bar's active state is changed
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
                items(agentTransactionList) { item ->
                    ListItemForInOutAgent(item)
                }
            }
        }

            LaunchedEffect(Unit) {
                agentTransactionViewModel.fetchStockIn()
            }
            when (agentStocksIn) {
                is Resource.Success -> {
                    val agentStockIn = (agentStocksIn as Resource.Success<List<AgentStockTransaction>>).result.filter { it.transactionType == "IN" }
                    if (agentStockIn.isEmpty()){
                        Text(
                            modifier = Modifier.padding(top=20.dp),
                            text = "Data masih kosong")
                    }
                    else {
                        LazyColumn (modifier = Modifier.padding(start = 8.dp, end = 8.dp, top =25.dp, bottom = 80.dp)){
                            items(agentStockIn) { item ->
                                ListItemForInOutAgent(item)
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
                        if (agentProductIsSearching){
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
                            query = searchAgentProduct,
                            onQueryChange = agentProductViewModel::onSearchTextChange,
                            onSearch = agentProductViewModel::onSearchTextChange,
                            active = agentProductIsSearching,
                            onActiveChange = { agentProductViewModel.onToogleSearch() },
                            trailingIcon = {
                                Icon(imageVector = Icons.Rounded.Search, contentDescription = "cari" )
                            },
                            placeholder = {
                                Text(text = "Pilih barang dulu...")
                            }
                        ) {
                            LazyColumn (modifier = Modifier.padding(horizontal = 10.dp)){
                                items(agentProductList) { product ->
                                    CardItemForInOut(cardData = product, selectedCard = selectedCard,
                                        onCardClicked = { productName ->
                                            selectedProduct = productName
                                            println("Nama produk: $productName")
                                        },
                                        idInternalProduct = {
                                            selectedItemId = it
                                        }
                                    )
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
                    if (!agentProductIsSearching) {
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
                            LaunchedEffect(Unit) {
                                agentProductViewModel.fetchAgentProducts()
                            }
                            when (agentProducts) {
                                is Resource.Success -> {
                                    val agentProduct =
                                        (agentProducts as Resource.Success<List<AgentProduct>>).result
                                    if (agentProduct.isEmpty()) {
                                        Text(
                                            modifier = Modifier.padding(top = 20.dp),
                                            text = "Data masih kosong"
                                        )
                                    } else {
                                        LazyColumn(modifier = Modifier
                                            .padding(horizontal = 10.dp, vertical = 10.dp)
                                        )
                                        {
                                            items(agentProduct) { product ->
                                                CardItemForInOut(
                                                    product,
                                                    selectedCard,
                                                    onCardClicked = { productName ->
                                                        selectedProduct = productName
                                                        println("Nama produk: $productName")
                                                    },
                                                    idInternalProduct = {
                                                        selectedItemId = it
                                                    }
                                                ) // Replace with your composable for each item
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
                    }

                    Button(
                        onClick = {
                            if (selectedCard.value.isEmpty()){
                                Toast.makeText(context,"Pilih barang terlebih dahulu!", Toast.LENGTH_SHORT).show()
                            } else{
                                showDetailStockInSheet = true
                            }
                        },
                        modifier = if (agentProductIsSearching){
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
                        text = selectedProduct ,
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
                            if (it.isDigitsOnly())
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
                    fun getAgentStockTransaction(): AgentStockTransaction {
                        val qtyIn = qtyStockIn.toIntOrNull() ?: 0
                        return AgentStockTransaction(
                            idAgentStockTransaction = "",
                            idProduct = selectedItemId,
                            productName = selectedProduct,
                            qtyProduct = qtyIn,
                            transactionType = "IN",
                            createAt = Date(),
                            desc = descStockIn
                        )
                    }
                    val agentStockTransactionObj: AgentStockTransaction = getAgentStockTransaction()
                    fun getOffering(): OfferingBySales {
                        val qtyIn = qtyStockIn.toIntOrNull() ?: 0
                        return OfferingBySales(

                            desc = descStockIn
                        )
                    }
                    val offeringObj: OfferingBySales = getOffering()
                    Button(
                        onClick = {
                            if (isQtyEmpty) {
                                // Tampilkan pesan error
                                Toast.makeText(context, "jumlah stok masuk tidak boleh kosong", Toast.LENGTH_SHORT).show()
                            }else {
                                isQtyEmpty= false
                                agentTransactionViewModel.addProductIn(agentStockTransactionObj, selectedItemId, offeringObj)
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
                    addStockIn.value?.let {
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
fun CardItemForInOut(
    cardData: AgentProduct,
    selectedCard: MutableState<String>,
    onCardClicked: (String) -> Unit,
    idInternalProduct: (String) -> Unit
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
                selectedCard.value = cardData.idProduct!!
                onCardClicked(cardData.productName!!)
                idInternalProduct(cardData.idProduct!!)
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
            val fixDate = sdf.format(date!!)
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
                text = item.productName!!, style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Medium))
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

@SuppressLint("SimpleDateFormat")
@Composable
fun ListItemForInOutAgent(item: AgentStockTransaction) {
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
            val date = item.createAt
            val fixDate = sdf.format(date!!)
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
                text = item.productName!!, style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Medium))
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
ListItemForInOut(item = internalProducts[1])
}