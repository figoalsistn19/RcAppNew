package com.inventoryapp.rcapp.ui.agentnav

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.CenterAlignedTopAppBar
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.inventoryapp.rcapp.R
import com.inventoryapp.rcapp.ui.agentnav.viewmodel.AgentOrderViewModel
import com.inventoryapp.rcapp.ui.agentnav.viewmodel.AgentProductViewModel
import com.inventoryapp.rcapp.ui.agentnav.viewmodel.reqOrders
import com.inventoryapp.rcapp.ui.nav.ROUTE_HOME_AGENT_SCREEN
import com.inventoryapp.rcapp.ui.theme.spacing

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderHistoryScreen(navController: NavController){
    val agentOrderViewModel = AgentOrderViewModel()
    val spacing = MaterialTheme.spacing
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val searchHistoryOrder by agentOrderViewModel.searchText.collectAsState()
    val historyOrderIsSearching by agentOrderViewModel.isSearching.collectAsState()
    val historyOrderList by agentOrderViewModel.historyOrderList.collectAsState()
    val sheetState = rememberModalBottomSheetState(true)
    var showDetailOrder by remember { mutableStateOf(false) }
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
                    query = searchHistoryOrder,//text showed on SearchBar
                    onQueryChange = agentOrderViewModel::onSearchTextChange, //update the value of searchText
                    onSearch = agentOrderViewModel::onSearchTextChange, //the callback to be invoked when the input service triggers the ImeAction.Search action
                    active = historyOrderIsSearching, //whether the user is searching or not
                    onActiveChange = { agentOrderViewModel.onToogleSearch() }, //the callback to be invoked when this search bar's active state is changed
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
                        items(historyOrderList) { item ->
                            CardReqOrder(reqOrder = item, onCardClick = {item ->
                                showDetailOrder = true
                            })
                        }
                    }
                }
                LazyColumn (modifier = Modifier.padding(start = 8.dp, end = 8.dp, top =25.dp)){
                    items(historyOrderList) { item ->
                        CardReqOrder(
                            reqOrder = item,
                            onCardClick = {item ->
                                showDetailOrder = true
                            }
                        )
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
               InvoiceScreen(reqOrders[1])
//                InvoiceScreen(invoice = historyOrderList[2])
//                InvoiceScreen(invoice = historyOrderList[3])
                
            }
        }
    }
}


@Preview(apiLevel = 33)
@Composable
fun PrevOrderHistory(){
    OrderHistoryScreen(navController = rememberNavController())
}