package com.inventoryapp.rcapp.ui.internalnav

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.twotone.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
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
import com.inventoryapp.rcapp.R
import com.inventoryapp.rcapp.data.model.AgentUser
import com.inventoryapp.rcapp.ui.agentnav.CardItem
import com.inventoryapp.rcapp.ui.agentnav.viewmodel.InternalProductTestViewModel
import com.inventoryapp.rcapp.ui.agentnav.viewmodel.StateHolder
import com.inventoryapp.rcapp.ui.agentnav.viewmodel.internalProducts
import com.inventoryapp.rcapp.ui.internalnav.viewmodel.AgentUserViewModel
import com.inventoryapp.rcapp.ui.internalnav.viewmodel.VerificationAgentViewModel
import com.inventoryapp.rcapp.ui.internalnav.viewmodel.agentUserListDummy
import com.inventoryapp.rcapp.ui.nav.ROUTE_HOME_AGENT_SCREEN
import com.inventoryapp.rcapp.ui.theme.spacing
import com.inventoryapp.rcapp.util.Resource

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun OfferingPoForAgentScreen(agentUserViewModel:AgentUserViewModel, navController: NavController){
    val verifAgentViewModel = VerificationAgentViewModel()
    val queryAgentList by verifAgentViewModel.searchText.collectAsState()
    val onQueryChangeAgentList by verifAgentViewModel.isSearching.collectAsState()
    val agentList by verifAgentViewModel.agentUsersList.collectAsState()
    val internalProductViewModel = InternalProductTestViewModel()
    val queryInternalProduct by internalProductViewModel.searchText.collectAsState()
    val onQueryChangeInternalProduct by internalProductViewModel.isSearching.collectAsState()
    val internalProductList1 by internalProductViewModel.productsList.collectAsState()
    val query by agentUserViewModel.searchText.collectAsState()
    val onQueryChange by agentUserViewModel.isSearching.collectAsState()
    val agentUserList by agentUserViewModel.agentUsers.observeAsState()
    val agentSearchList by agentUserViewModel.agentUsersList.collectAsState()

    val sheetState = rememberModalBottomSheetState(true)
    var showAddPoForAgentSheet by remember { mutableStateOf(false) }
    var showPickItemSheet by remember { mutableStateOf(false) }
    var showDetailPoSheet by remember { mutableStateOf(false) }
    val selectedAgentUser = remember { mutableStateOf("") }
    val selectedProduct = remember { mutableStateOf("") }
    val selectedAgentNameHolder = StateHolder(initialValue = "")
    val selectedProductNameHolder = StateHolder(initialValue = "")
    var qtyOffering by remember { mutableStateOf("") }
    var descOffering by remember { mutableStateOf("") }
    var isQtyEmpty = false
    val spacing = MaterialTheme.spacing
    val context = LocalContext.current
    Scaffold (
        bottomBar = {
            Column {
                Row(modifier = Modifier.fillMaxWidth()) {
                    // Your other UI content here
                    Spacer(modifier = Modifier.weight(1f)) // Add flexibility with weight
                    ExtendedFloatingActionButton(
                        onClick = {
                            showAddPoForAgentSheet = true
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
        },
        containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
    ){
        Column {
            TopAppBar(
                title = {
                    Text(text = "Buat PO Agen",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Medium))
                },
                navigationIcon = {
                    IconButton(onClick = {

                    }) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.back_btn),
                            contentDescription = "tombol kembali",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                )
            )
            SearchBar(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 10.dp),
                query = query,
                onQueryChange = agentUserViewModel::onSearchTextChange,
                onSearch = agentUserViewModel::onSearchTextChange,
                active = onQueryChangeAgentList,
                onActiveChange = { verifAgentViewModel.onToogleSearch()},
                trailingIcon = {
                    Icon(imageVector = Icons.Rounded.Search, contentDescription = "cari" )
                },
                placeholder = {
                    Text(text = "Cari nama agen disini...")
                },
                shadowElevation = 2.dp,
                colors = SearchBarDefaults.colors(MaterialTheme.colorScheme.surfaceContainerLowest)
            ) {
                LazyColumn {
                    items(agentList) { user ->
                        CardPoAgent()
                    }
                }
            }
            LazyColumn(
                modifier = Modifier.padding(top=8.dp, bottom = 80.dp)
            ){
                items(agentUserListDummy){ user ->
                    CardPoAgent()
                }
            }
        }
        if (showAddPoForAgentSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showAddPoForAgentSheet = false
                },
                sheetState = sheetState
            ) {
                ConstraintLayout(modifier = Modifier.fillMaxSize()) {
                    val (refTitleSheet, refSearchBar, refListProduct, refBtnNext) = createRefs()
                    Text(
                        text = "Pilih Agen",
                        modifier = Modifier.constrainAs(refTitleSheet) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            height = Dimension.wrapContent
                        },
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Medium)
                    )
                    Box(
                        modifier =
                        if (onQueryChangeAgentList) {
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
                    ) {
                        SearchBar(
                            query = query,
                            onQueryChange = agentUserViewModel::onSearchTextChange,
                            onSearch = agentUserViewModel::onSearchTextChange,
                            active = onQueryChange,
                            onActiveChange = { agentUserViewModel.onToogleSearch() },
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Rounded.Search,
                                    contentDescription = "cari"
                                )
                            },
                            placeholder = {
                                Text(text = "Cari agen disini...")
                            }
                        ) {
                            LazyColumn(modifier = Modifier.padding(horizontal = 10.dp)) {
                                items(agentSearchList) { agent ->
                                    CardAgent(
                                        cardData = agent,
                                        selectedCard =selectedAgentUser,
                                        onCardClicked = {selectedAgent ->
                                            selectedAgentNameHolder.updateValue(selectedAgent)
                                            println("Nama produk: $selectedAgent")
                                        }
                                    )
                                }
                            }
                        }
                    }

                    if (onQueryChangeAgentList) {

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
                            LaunchedEffect(Unit) {
                                agentUserViewModel.fetchUsers()
                            }

                            when (agentUserList) {
                                is Resource.Success -> {
                                    val userList = (agentUserList as Resource.Success<List<AgentUser>>).result
                                    LazyColumn(
                                        modifier = Modifier
                                            .padding(horizontal = 10.dp, vertical = 10.dp)
                                    )
                                    {
                                        items(userList) { agent ->
                                            CardAgent(
                                                cardData = agent,
                                                selectedCard = selectedAgentUser,
                                                onCardClicked = { selectedAgent ->
                                                    selectedAgentNameHolder.updateValue(selectedAgent)
                                                    println("Nama produk: $selectedAgent")
                                                }
                                            ) // Replace with your composable for each item
                                        }
                                    }
                                }
                                is Resource.Loading -> {
                                    // Tampilkan indikator loading jika diperlukan
                                    CircularProgressIndicator()
                                }
                                is Resource.Failure -> {
                                    // Tampilkan pesan error jika diperlukan
                                    val error = (agentUserList as Resource.Failure).throwable
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
                            if (selectedAgentUser.value.isEmpty()) {
                                Toast.makeText(
                                    context,
                                    "Pilih barang terlebih dahulu!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                showPickItemSheet = true
                            }
                        },
                        modifier = if (onQueryChangeAgentList) {
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
        if (showPickItemSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    showAddPoForAgentSheet = false
                    showPickItemSheet = false
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
                    Box(
                        modifier =
                        if (onQueryChangeInternalProduct) {
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
                    ) {
                        SearchBar(
                            query = queryInternalProduct,
                            onQueryChange = internalProductViewModel::onSearchTextChange,
                            onSearch = internalProductViewModel::onSearchTextChange,
                            active = onQueryChangeInternalProduct,
                            onActiveChange = { internalProductViewModel.onToogleSearch() },
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Rounded.Search,
                                    contentDescription = "cari"
                                )
                            },
                            placeholder = {
                                Text(text = "Pilih barang dulu...")
                            }
                        ) {
                            LazyColumn(modifier = Modifier.padding(horizontal = 10.dp)) {
                                items(internalProductList1) { product ->
                                    CardItem(
                                        cardData = product,
                                        selectedCard = selectedProduct,
                                        onCardClicked = {selectedProduct ->
                                            selectedProductNameHolder.updateValue(selectedProduct)
                                            println("Nama produk: $selectedProduct")
                                        }
                                    )
                                }
                            }
                        }
                    }

                    if (onQueryChangeInternalProduct) {

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
                            LazyColumn(
                                modifier = Modifier
                                    .padding(horizontal = 10.dp, vertical = 10.dp)
                            )
                            {
                                items(internalProducts) { product ->
                                    CardItem(
                                        cardData = product,
                                        selectedCard = selectedProduct,
                                        onCardClicked = { selectedProduct ->
                                            selectedProductNameHolder.updateValue(selectedProduct)
                                            println("Nama produk: $selectedProduct")
                                        }
                                    ) // Replace with your composable for each item
                                }
                            }
                        }

                    }

                    Button(
                        onClick = {
                            if (selectedProduct.value.isEmpty()) {
                                Toast.makeText(
                                    context,
                                    "Pilih barang terlebih dahulu!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                showDetailPoSheet = true
                            }
                        },
                        modifier = if (onQueryChangeInternalProduct) {
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

        if (showDetailPoSheet) {
            ModalBottomSheet(
                containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                onDismissRequest = {
                    showDetailPoSheet = false
                    showAddPoForAgentSheet = false
                    showPickItemSheet = false
                },
                sheetState = sheetState
            ) {
                ConstraintLayout(modifier = Modifier.fillMaxSize()) {
                    val (refTitleSheet, refProductName, refQtyMin, refDescProduct, refBtnSave) = createRefs()
                    Text(
                        text = "Tambahkan Detail PO",
                        modifier = Modifier
                            .constrainAs(refTitleSheet) {
                                top.linkTo(parent.top)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                            }
                            .padding(vertical = 10.dp),
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Medium)
                    )
                    Card (
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                            .height(80.dp)
                            .constrainAs(refProductName) {
                                top.linkTo(refTitleSheet.bottom, spacing.medium)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                                width = Dimension.wrapContent
                            },
                        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceContainerLowest),
                        border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
                    ){
                        Row (
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ){
                            Row (
                                modifier = Modifier.fillMaxHeight(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ){
                                Icon(
                                    imageVector = Icons.Default.AccountCircle,
                                    contentDescription = "profile",
                                    tint = MaterialTheme.colorScheme.primary)
                            }
                            Column (
                                modifier = Modifier.padding(start = 5.dp),
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = selectedAgentNameHolder.value,
                                    style = MaterialTheme.typography.titleMedium
                                    )
                                Text(
                                    text = selectedProductNameHolder.value,
                                    style = MaterialTheme.typography.titleMedium
                                )
                            }
                            Row (
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.End
                            ){
                                Image(imageVector = ImageVector.vectorResource(R.drawable.checked), contentDescription = "checked")
                            }
                        }
                    }

//                    modifier = Modifier.constrainAs(refProductName) {
//                        top.linkTo(refTitleSheet.bottom, spacing.medium)
//                        start.linkTo(parent.start)
//                        end.linkTo(parent.end)
//                        width = Dimension.wrapContent
//                    }
                    OutlinedTextField(
                        value = qtyOffering,
                        onValueChange = {
                            qtyOffering = it
                            isQtyEmpty = it.isEmpty()
                        },
                        isError = isQtyEmpty,
                        label = {
                            Text(text = "Jumlah Barang")
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
                            Text(text = "Jumlah ")
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
                        value = descOffering,
                        onValueChange = {
                            descOffering = it
                        },
                        label = {
                            Text(text = "Catatan")
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


//        if (showAddPoForAgent){
//            ModalBottomSheet(
//                onDismissRequest = {
//                    showAddPoForAgent=false
//                },
//                sheetState = sheetState,
//                containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
//            ){
//                Column (
//                    modifier = Modifier.fillMaxWidth(),
//                    verticalArrangement = Arrangement.Center,
//                    horizontalAlignment = Alignment.CenterHorizontally
//                ) {
//                    Text(
//                        text = "Pilih Agen",
//                        style = MaterialTheme.typography.titleLarge.copy(
//                            fontWeight = FontWeight.Medium
//                        )
//                    )
//                    SearchBar(
//                        modifier = Modifier.padding(bottom = 10.dp),
//                        query = query,
//                        onQueryChange = verifAgentViewModel::onSearchTextChange,
//                        onSearch = verifAgentViewModel::onSearchTextChange,
//                        active = onQueryChange,
//                        onActiveChange = { verifAgentViewModel.onToogleSearch()},
//                        trailingIcon = {
//                            Icon(imageVector = Icons.Rounded.Search, contentDescription = "cari" )
//                        },
//                        placeholder = {
//                            Text(text = "Pilih barang dulu...")
//                        },
//                        shadowElevation = 2.dp,
//                        colors = SearchBarDefaults.colors(MaterialTheme.colorScheme.surfaceContainerLowest)
//                    ) {
//                        LazyColumn (
//                            modifier = Modifier.padding(start = 10.dp, end = 10.dp, bottom = 80.dp)
//                        ) {
//                            items(agentList) { product ->
//                                CardAgent(
//                                    cardData = product, selectedCard = selectedCard,
//                                    onCardClicked = {agentName ->
//                                        selectedProductNameHolder.updateValue(agentName)
//                                        println("Nama produk: $agentName")}
//                                )
//                            }
//                        }
//                    }
//                    Column(
//                        modifier = Modifier
//                            .padding(start = 10.dp, end = 10.dp, bottom = 5.dp)
//                            .height(400.dp)
//                    ){
//                        LazyColumn{
//                            items(agentUserList) { product ->
//                                CardAgent(
//                                    cardData = product, selectedCard = selectedCard,
//                                    onCardClicked = {agentName ->
//                                        selectedProductNameHolder.updateValue(agentName)
//                                        println("Nama produk: $agentName")}
//                                )
//                            }
//                        }
//                    }
//
//                    Row (
//                        modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp, end = 10.dp),
//                        verticalAlignment = Alignment.CenterVertically,
//                        horizontalArrangement = Arrangement.End
//                    ) {
//                        Button(onClick = { /*TODO*/ }) {
//                            Text(text = "Selanjutnya")
//                        }
//                    }
//                }
//            }
//        }
    }
}

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun CardAgent(
    cardData: AgentUser,
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
                selectedCard.value = cardData.idAgent!!
                onCardClicked(cardData.name!!)
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
                Text(text = cardData.name!!,
                    style = MaterialTheme.typography.titleLarge)
            }
            // Bagian kanan
            RadioButton(
                selected = cardData.idAgent == selectedCard.value,
                onClick = {
                    selectedCard.value = cardData.idAgent!!
                    onCardClicked(cardData.name!!)
                }
            )
        }
    }
}


@Composable
fun CardPoAgent(){
    Card (
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 15.dp, vertical = 5.dp
            )
            .clickable {

            },
        elevation = CardDefaults.cardElevation(3.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceContainerLowest)
    ){
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            Image(
                imageVector = ImageVector.vectorResource(R.drawable.price_tag),
                contentDescription = "price tag"
            )
            Column (
                modifier = Modifier.padding(start = 8.dp)
            ){
                Text(
                    text = "Toko Rina",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Roti Tiro",
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.titleSmall
                    )
                Text(
                    text = "23 Desember 2023・23:14",
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontWeight = FontWeight.Light
                    )
                )
            }
            Row (
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ){
                Column (
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.End
                ){
                    Text(text = "Rp100,000")
                    Text(text = "pending")
                }
            }
        }
    }
}

@Preview(apiLevel = 33)
@Composable
fun PrevPo(){
    CardPoAgent()
}