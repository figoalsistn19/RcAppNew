package com.inventoryapp.rcapp.ui.internalnav

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.inventoryapp.rcapp.R
import com.inventoryapp.rcapp.ui.agentnav.CardReqOrder
import com.inventoryapp.rcapp.ui.agentnav.InvoiceScreen
import com.inventoryapp.rcapp.ui.agentnav.viewmodel.AgentOrderViewModel
import com.inventoryapp.rcapp.ui.agentnav.viewmodel.reqOrders
import com.inventoryapp.rcapp.ui.auth.internalauth.AuthInternalViewModel
import com.inventoryapp.rcapp.ui.nav.ROUTE_HOME
import com.inventoryapp.rcapp.ui.nav.ROUTE_HOME_AGENT_SCREEN
import com.inventoryapp.rcapp.ui.theme.spacing
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun InternalSalesScreen(viewModel: AuthInternalViewModel, navController: NavController){
    val agentOrderViewModel = AgentOrderViewModel()
    val spacing = MaterialTheme.spacing
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val searchHistoryOrder by agentOrderViewModel.searchText.collectAsState()
    val historyOrderIsSearching by agentOrderViewModel.isSearching.collectAsState()
    val historyOrderList by agentOrderViewModel.historyOrderList.collectAsState()
    val sheetState = rememberModalBottomSheetState()
    var showDetailOrder by remember { mutableStateOf(false) }
    var qtyProduct by remember { mutableStateOf("") }
    var descProduct by remember { mutableStateOf("") }
    val selectedCard = remember { mutableStateOf("") }
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
                Column (
                    modifier = Modifier.padding(top=55.dp)
                ){
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
//                                CardReqOrder(reqOrder = item, onCardClick = {item ->
//                                    showDetailOrder = true
//                                })
                            }
                        }
                    }
                    LazyColumn (modifier = Modifier.padding(start = 8.dp, end = 8.dp, top =25.dp)){
                        items(historyOrderList) { item ->
//                            CardReqOrder(
//                                reqOrder = item,
//                                onCardClick = {item ->
//                                    showDetailOrder = true
//                                }
//                            )
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
//                    InvoiceScreen(reqOrders[1])
//                InvoiceScreen(invoice = historyOrderList[2])
//                InvoiceScreen(invoice = historyOrderList[3])

                }
            }
        }
    }
}