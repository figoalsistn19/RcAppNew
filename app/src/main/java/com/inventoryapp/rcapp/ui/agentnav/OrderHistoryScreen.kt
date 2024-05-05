package com.inventoryapp.rcapp.ui.agentnav

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.inventoryapp.rcapp.R
import com.inventoryapp.rcapp.data.model.SalesOrder
import com.inventoryapp.rcapp.ui.agentnav.viewmodel.SalesOrderViewModel
import com.inventoryapp.rcapp.ui.nav.ROUTE_HOME_AGENT_SCREEN
import com.inventoryapp.rcapp.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderHistoryScreen(
    salesOrderViewModel: SalesOrderViewModel,
    navController: NavController
){
    val searchText by salesOrderViewModel.searchText.collectAsState()
    val isSearching by salesOrderViewModel.isSearching.collectAsState()
    val salesOrderList by salesOrderViewModel.salesOrderList.collectAsState()

    val salesOrder by salesOrderViewModel.salesOrder.observeAsState()

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val sheetState = rememberModalBottomSheetState(true)
    var showDetailOrder by remember { mutableStateOf(false) }

    val selectedOrderStateFlow = MutableStateFlow<SalesOrder?>(null)

    var qtyProduct by remember { mutableStateOf("") }
    var descProduct by remember { mutableStateOf("") }
    val selectedCard = remember { mutableStateOf("") }
    Scaffold(
        containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Text(text = "Riwayat Belanja",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Medium))
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                        titleContentColor = MaterialTheme.colorScheme.onSurface,
                    ),
                    navigationIcon = {
                        IconButton(onClick = { navController.navigate(ROUTE_HOME_AGENT_SCREEN) }) {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.back_btn),
                                contentDescription = "tombol kembali",
                                tint = MaterialTheme.colorScheme.onSurface
                                )
                        }
                    },
                    actions = {},
                    scrollBehavior = scrollBehavior)
                SearchBar(
                    query = searchText,//text showed on SearchBar
                    onQueryChange = salesOrderViewModel::onSearchTextChange, //update the value of searchText
                    onSearch = salesOrderViewModel::onSearchTextChange, //the callback to be invoked when the input service triggers the ImeAction.Search action
                    active = isSearching, //whether the user is searching or not
                    onActiveChange = { salesOrderViewModel.onToogleSearch() }, //the callback to be invoked when this search bar's active state is changed
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 23.dp, top = 8.dp, end = 23.dp),
                    trailingIcon = {
                        Icon(imageVector = Icons.Rounded.Search, contentDescription = "cari" )
                    },
                    placeholder = {
                                  Text(text = "Cari pesanan disini...")
                    }
                ) {
                    LazyColumn (modifier = Modifier.padding(horizontal = 8.dp, vertical =10.dp)){
                        items(salesOrderList) { item ->
                            CardOrderHistory(
                                order = item,
                                onCardClick = {
                                    selectedCard.value = it
                                    showDetailOrder = true
                                              },
                                onCardData = { data ->
                                    selectedOrderStateFlow.value = data
                                }
                            )


                        }
                    }
                }
                LaunchedEffect(Unit) {
                    salesOrderViewModel.fetchSalesOrder()
                }
                when (salesOrder) {
                    is Resource.Success -> {
                        val salesOrders = (salesOrder as Resource.Success<List<SalesOrder>>).result
                        if (salesOrders.isEmpty()){
                            Text(
                                modifier = Modifier.padding(top=20.dp),
                                text = "Data masih kosong")
                        }
                        else {
                            LazyColumn (modifier = Modifier.padding(start = 8.dp, end = 8.dp, top =25.dp)){
                                items(salesOrders) { item ->
                                  CardOrderHistory(
                                      order = item,
                                      onCardClick = {
                                          selectedCard.value = it
                                          showDetailOrder = true
                                                    },
                                      onCardData = { data ->
                                          selectedOrderStateFlow.value = data
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
                        val error = (salesOrder as Resource.Failure).throwable
                        Text(text = "Error: ${error.message}")
                    }
                    else -> {
                        // Tampilkan pesan default jika diperlukan
                        Text(text = "No data available")
                    }
                }
            }
        }
    ) {
        if (showDetailOrder){
            ModalBottomSheet(
                onDismissRequest = {
                    showDetailOrder=false
                },
                sheetState = sheetState
            ){
               InvoiceScreen(selectedOrderStateFlow.value!!)
            }
        }
    }
}


@Preview(apiLevel = 33)
@Composable
fun PrevOrderHistory(){
//    OrderHistoryScreen(navController = rememberNavController())
}