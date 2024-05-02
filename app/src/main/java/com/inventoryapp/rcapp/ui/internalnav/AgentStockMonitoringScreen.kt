package com.inventoryapp.rcapp.ui.internalnav

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.google.firebase.firestore.FirebaseFirestore
import com.inventoryapp.rcapp.R
import com.inventoryapp.rcapp.data.model.AgentProduct
import com.inventoryapp.rcapp.data.model.AgentUser
import com.inventoryapp.rcapp.ui.agentnav.ListProductAgent
import com.inventoryapp.rcapp.ui.internalnav.viewmodel.AgentUserViewModel
import com.inventoryapp.rcapp.util.FireStoreCollection
import com.inventoryapp.rcapp.util.Resource
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "StateFlowValueCalledInComposition")
@Composable
fun AgentStockMonitoringScreen(
    agentUserViewModel: AgentUserViewModel?
){
    val query by agentUserViewModel?.searchTextAgent!!.collectAsState()
    val onQueryChange by agentUserViewModel?.isSearchingAgent!!.collectAsState()
    val agentUserList by agentUserViewModel!!.agentUsers.observeAsState()

    val agentSearchList by agentUserViewModel!!.agentUsersList.collectAsState()

    val queryProductAgent by agentUserViewModel?.searchTextAgentProduct!!.collectAsState()
    val onQueryChangeProductAgent by agentUserViewModel?.isSearchingAgentProduct!!.collectAsState()
    val agentUserProductList by agentUserViewModel!!.agentProducts.observeAsState()

    val agentProductSearchList by agentUserViewModel!!.agentProductList.collectAsState()

    val selectedOrderStateFlow = MutableStateFlow<AgentUser?>(null)

    val sheetState = rememberModalBottomSheetState()
    val refreshScope = rememberCoroutineScope()
    var refreshing by remember { mutableStateOf(false) }
    fun refresh() = refreshScope.launch {
        refreshing = true
        delay(1500)
        agentUserViewModel?.fetchUsers()
        refreshing = false
    }
    val state = rememberPullRefreshState(refreshing, ::refresh)

    var showDetailAgentStock by remember { mutableStateOf(false) }

    Scaffold (
        containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
    ){
        Column {
            TopAppBar(
                title = {
                    Text(text = "Stok Agen",
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
                onQueryChange = agentUserViewModel!!::onSearchTextChange,
                onSearch = agentUserViewModel::onSearchTextChange,
                active = onQueryChange,
                onActiveChange = { agentUserViewModel.onToogleSearch()},
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
                    items(agentSearchList) { user ->
                        CardAgentStockMonitoring(
                            agentUser = user,
                            onCardClick = {
                                showDetailAgentStock = true
                            },
                            onCardData = {selectedOrderStateFlow.value = it}
                        )
                    }
                }
            }
            LaunchedEffect(Unit) {
                agentUserViewModel.fetchUsers()
            }
            when (agentUserList) {
                is Resource.Success -> {
                    val userList = (agentUserList as Resource.Success<List<AgentUser>>).result
                    Box(
                        Modifier
                            .pullRefresh(state)
                            .padding(top = 8.dp, bottom = 80.dp)
                    )
                    {
                        LazyColumn(Modifier.fillMaxSize()) {
                            if (!refreshing) {
                                items(userList){ user ->
                                    CardAgentStockMonitoring(
                                        agentUser = user,
                                        onCardClick = { _ ->
                                            showDetailAgentStock = true
                                        },
                                        onCardData = {selectedOrderStateFlow.value = it}
                                    )
                                }
                            }
                        }
                        PullRefreshIndicator(refreshing, state, Modifier.align(Alignment.TopCenter))
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
//            LazyColumn(
//                modifier = Modifier.padding(top=8.dp, bottom = 80.dp)
//            ){
//                items(agentUserListDummy){ user ->
//                    CardAgentStockMonitoring(
//                        agentUser = user,
//                        onCardClick = {user_ ->
//                            showDetailAgentStock = true
//                        }
//                    )
//                }
//            }
        }
        if (showDetailAgentStock){
            ModalBottomSheet(
                onDismissRequest = {
                    showDetailAgentStock=false
                },
                sheetState = sheetState,
                containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
            ){
                Column (
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Text(
                        text = selectedOrderStateFlow.value!!.name!!,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Medium
                        )
                    )
                    SearchBar(
                        modifier = Modifier
                            .padding(start = 8.dp, end = 8.dp, bottom = 10.dp)
                        ,
                        query = queryProductAgent,
                        onQueryChange = agentUserViewModel!!::onSearchTextChangeProductAgent,
                        onSearch = agentUserViewModel::onSearchTextChangeProductAgent,
                        active = onQueryChangeProductAgent,
                        onActiveChange = { agentUserViewModel.onToogleSearchProductAgent()},
                        trailingIcon = {
                            Icon(imageVector = Icons.Rounded.Search, contentDescription = "cari" )
                        },
                        placeholder = {
                            Text(text = "Cari barang disini...")
                        },
                        shadowElevation = 2.dp,
                        colors = SearchBarDefaults.colors(MaterialTheme.colorScheme.surfaceContainerLowest)
                    ) {
                        LazyColumn(modifier = Modifier.padding(start = 10.dp, end = 10.dp, top = 10.dp)) {
                            items(agentProductSearchList) { item ->
                                ListProductAgent(
                                    item = item,
                                    onCardClicked = {}
                                )
                            }
                        }
                    }
                    LaunchedEffect(Unit) {
                        agentUserViewModel.fetchAgentProducts(selectedOrderStateFlow.value!!.idAgent!!)
                    }
                    when (agentUserProductList) {
                        is Resource.Success -> {
                            val agentProduct = (agentUserProductList as Resource.Success<List<AgentProduct>>).result
                            if (agentProduct.isEmpty()){
                                Text(
                                    modifier = Modifier.padding(top=20.dp).align(Alignment.CenterHorizontally),
                                    text = "Data masih kosong")
                            }
                            else {
                                LazyColumn (modifier = Modifier.padding(start = 8.dp, end = 8.dp, top =10.dp, bottom = 10.dp)){
                                    items(agentProduct) { item ->
                                        ListProductAgent(item,
                                            onCardClicked = {
                                            }
                                        )
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
                            val error = (agentUserProductList as Resource.Failure).throwable
                            Text(text = "Error: ${error.message}")
                        }
                        else -> {
                            // Tampilkan pesan default jika diperlukan
                            Text(text = "No data available")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CardAgentStockMonitoring(
    agentUser: AgentUser,
    onCardClick: (String) -> Unit,
    onCardData: (AgentUser) -> Unit
){
    val db = FirebaseFirestore.getInstance()
    var size by remember { mutableStateOf("0") }
    val collectionSize = db
        .collection(FireStoreCollection.AGENTUSER)
        .document(agentUser.idAgent!!)
        .collection(FireStoreCollection.AGENTPRODUCT)
        .get()
        .addOnSuccessListener {documents ->
            // Periksa jika ada dokumen yang diambil
            if (documents.documents.isNotEmpty()) {
                val count = documents.size() // Jumlah dokumen (harus 1)
                // Anda bisa akses informasi lain dari dokumen pertama (jika diperlukan)
                size = count.toString()
                // ...
            } else {
                // Collection kosong (jumlah dokumen: 0)
            }
        }
        .addOnFailureListener { exception ->
            Log.d(TAG, "Error getting documents: ", exception)
        }
    Card (
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 15.dp, vertical = 5.dp
            )
            .clickable {
                onCardClick(agentUser.idAgent!!)
                onCardData(agentUser)
            },
        elevation = CardDefaults.cardElevation(1.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceContainerLowest)
    ){
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp, vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.profile_circle),
                contentDescription = "ini agent"
            )
            Text(
                modifier = Modifier.padding(start = 10.dp),
                text = agentUser.name!!,
                style = MaterialTheme.typography.labelLarge.copy(
                    color = MaterialTheme.colorScheme.onSurface,
                )
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                    text = "$size Barang",
                    modifier = Modifier.padding(start = 5.dp),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}