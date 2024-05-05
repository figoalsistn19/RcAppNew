package com.inventoryapp.rcapp.ui.agentnav

import android.annotation.SuppressLint
import android.widget.Toast
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
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableLongStateOf
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
import com.google.firebase.firestore.FirebaseFirestore
import com.inventoryapp.rcapp.R
import com.inventoryapp.rcapp.data.model.AgentProduct
import com.inventoryapp.rcapp.data.model.AgentStockTransaction
import com.inventoryapp.rcapp.data.model.OfferingForAgent
import com.inventoryapp.rcapp.data.model.ProductsItem
import com.inventoryapp.rcapp.ui.agentnav.viewmodel.AgentProductViewModel
import com.inventoryapp.rcapp.ui.agentnav.viewmodel.AgentTransactionViewModel
import com.inventoryapp.rcapp.ui.nav.ROUTE_HOME_AGENT_SCREEN
import com.inventoryapp.rcapp.ui.theme.spacing
import com.inventoryapp.rcapp.util.FireStoreCollection
import com.inventoryapp.rcapp.util.Resource
import java.util.Date

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StockOutScreen(
    agentTransactionViewModel: AgentTransactionViewModel,
    agentProductViewModel: AgentProductViewModel,
    navController: NavController
){
    val db = FirebaseFirestore.getInstance()

    val context = LocalContext.current

    var finalPrice by remember {
        mutableLongStateOf(0L)
    }

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val spacing = MaterialTheme.spacing

    val searchText by agentTransactionViewModel.searchText.collectAsState()
    val isSearching by agentTransactionViewModel.isSearching.collectAsState()

    val searchAgentProduct by agentProductViewModel.searchText.collectAsState()
    val agentProductIsSearching by agentProductViewModel.isSearching.collectAsState()
    val agentProductList by agentProductViewModel.agentProductList.collectAsState()
    val agentTransactionOutList by agentTransactionViewModel.agentTransactionOutList.collectAsState()

    val agentProducts by agentProductViewModel.agentProducts.observeAsState()
    val addStockOut = agentTransactionViewModel.addProductInFlow.collectAsState()
    val agentStocksOut by agentTransactionViewModel.agentTransactionsIn.observeAsState()

    val sheetState = rememberModalBottomSheetState()
    var showAddStockOutSheet by remember { mutableStateOf(false) }
    var showDetailStockOutSheet by remember {
        mutableStateOf(false)
    }
    var isQtyEmpty = true
    var qtyStockOut by remember { mutableStateOf("") }
    var descStockOut by remember { mutableStateOf("") }
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
                    Text(text = "Barang Keluar",
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
                            showAddStockOutSheet = true
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
                    .padding(start = 23.dp, top = 8.dp, end = 23.dp),
                trailingIcon = {
                    Icon(imageVector = Icons.Rounded.Search, contentDescription = "cari" )
                },
                placeholder = {
                    Text(text = "Cek riwayat barang keluar...")
                }
            ) {
                LazyColumn (modifier = Modifier.padding(horizontal = 8.dp, vertical =10.dp)){
                    items(agentTransactionOutList) { item ->
                        ListItemForInOutAgent(item
                        )
                    }
                }
            }
            LaunchedEffect(Unit) {
                agentTransactionViewModel.fetchStockIn()
            }
            when (agentStocksOut) {
                is Resource.Success -> {
                    val agentStockIn = (agentStocksOut as Resource.Success<List<AgentStockTransaction>>).result.filter { it.transactionType == "OUT" }
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
        if (showAddStockOutSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showAddStockOutSheet = false
                },
                sheetState = sheetState
            ) {
                ConstraintLayout(modifier = Modifier.fillMaxSize()) {
                    val (refTitleSheet, refSearchBar, refListProduct, refBtnNext) = createRefs()
                    Text(
                        text = "Tambahkan barang keluar",
                        modifier = Modifier.constrainAs(refTitleSheet) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        },
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Medium)
                    )
                    Box (modifier =
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
                                showDetailStockOutSheet = true
                                db.collection(FireStoreCollection.INTERNALPRODUCT)
                                    .document(selectedItemId)
                                    .get()
                                    .addOnSuccessListener { document ->
                                        val finalPriceProduct = document.getLong("finalPrice")
                                        if (document != null) {
                                            // Dokumen ditemukan, dapatkan data
                                            finalPrice = finalPriceProduct!!
                                            // Lakukan sesuatu dengan data
                                        } else {
                                            Toast.makeText(context,"Data tidak ditemukan", Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                    .addOnFailureListener { exception ->
                                        Toast.makeText(context,exception.message, Toast.LENGTH_SHORT).show()
                                    }
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
                    ) {
                        Text(text = "Selanjutnya")
                    }
                }
            }
        }
        if (showDetailStockOutSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showDetailStockOutSheet = false
                    showAddStockOutSheet = false
                },
                sheetState = sheetState,

            ) {
                ConstraintLayout(modifier = Modifier.fillMaxSize()) {
                    val (refTitleSheet, refQtyMin, refProductName, refDescProduct, refBtnSave) = createRefs()
                    Text(
                        text = "Tambahkan Detail Barang Keluar",
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
                        style = MaterialTheme.typography.titleLarge
                    )
                    OutlinedTextField(
                        value = qtyStockOut,
                        onValueChange = {
                            qtyStockOut = it
                            isQtyEmpty = it.isEmpty()
                        },
                        isError = isQtyEmpty,
                        maxLines = 1,
                        label = {
                            Text(text = "Stok Keluar")
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
                            Text(text = "Ada berapa barang yang keluar ?")
                        },
                        trailingIcon = {
                            if (isQtyEmpty) {
                                Icon(Icons.Outlined.Info, contentDescription = "isi dahulu")
                            } else{
                                Icon(imageVector = Icons.Rounded.Done, contentDescription ="done" )
                            }
                        }
                    )
                    OutlinedTextField(
                        value = descStockOut,
                        onValueChange = {
                            descStockOut = it
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
                        val qtyOut = qtyStockOut.toIntOrNull() ?: 0
                        return AgentStockTransaction(
                            idAgentStockTransaction = "",
                            idProduct = selectedItemId,
                            productName = selectedProduct,
                            qtyProduct = -qtyOut,
                            transactionType = "OUT",
                            createAt = Date(),
                            desc = descStockOut
                        )
                    }
                    val agentStockTransactionObj: AgentStockTransaction = getAgentStockTransaction()
                    fun getOffering(): OfferingForAgent {
                        return OfferingForAgent(
                            idOffering = selectedItemId + "bysystem",
                            desc = "STOK MENIPIS",
                            idAgent = agentProductViewModel.currentUser?.uid?:"",
                            nameAgent = agentProductViewModel.currentUser?.displayName?:"",
                            statusOffering = "BY SYSTEM",
                            productsItem = listOf(
                                ProductsItem(
                                    idProduct = selectedItemId,
                                    productName = selectedProduct,
                                    price = finalPrice,
                                    finalPrice = finalPrice,
                                    quantity = 10,
                                    totalPrice = finalPrice * 10,
                                    discProduct = null
                                )
                            ),
                            totalPrice = finalPrice * 10
                        )
                    }
                    val offeringObj: OfferingForAgent = getOffering()
                    Button(
                        onClick = {
                            if (isQtyEmpty) {
                                // Tampilkan pesan error
                                Toast.makeText(context, "Jumlah stok masuk tidak boleh kosong!", Toast.LENGTH_SHORT).show()
                            } else {
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
                    addStockOut.value?.let {
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

@Preview(apiLevel = 33)
@Composable
fun PrevStockOutScreen(){
//    StockOutScreen(navController = rememberNavController())
}