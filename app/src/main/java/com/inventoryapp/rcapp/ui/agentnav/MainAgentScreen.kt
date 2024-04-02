package com.inventoryapp.rcapp.ui.agentnav

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.inventoryapp.rcapp.ui.auth.agentauth.AuthAgentViewModel
import com.inventoryapp.rcapp.ui.internalnav.BottomBar
import com.inventoryapp.rcapp.ui.internalnav.BottomBarScreen
import com.inventoryapp.rcapp.ui.nav.ROUTE_HOME
import com.inventoryapp.rcapp.ui.nav.ROUTE_ORDER_HISTORY_SCREEN
import com.inventoryapp.rcapp.ui.nav.ROUTE_STOCK_IN_SCREEN
import com.inventoryapp.rcapp.ui.nav.ROUTE_STOCK_OUT_SCREEN
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainAgentScreen(
    authViewModel: AuthAgentViewModel?
){
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(
        rememberTopAppBarState()
    )
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
                        authViewModel?.logout()
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
            bottomBar = {
                BottomBar(navController = navController)
            },
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(text = "Halo Figo!",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Medium))
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                        titleContentColor = MaterialTheme.colorScheme.onSurface,
                    ),
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch {
                                drawerState.apply {
                                    if (isClosed) open() else close()
                                }
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Filled.Menu,
                                contentDescription = "Localized description",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    },
                    actions = {
                        BadgedBox(badge = { Badge { Text("k") } }) {
                            Icon(
                                Icons.Filled.Notifications,
                                contentDescription = "Home"
                            )
                        } },
                    scrollBehavior = scrollBehavior,
                    modifier = Modifier
                )
            }
        ) {
            NavHost(navController = navController, startDestination = BottomBarScreen.Home.route,
            ){
                composable(BottomBarScreen.Home.route){
                    HomeAgentScreen(navController = navController)
                }
                composable(BottomBarScreen.Stock.route){
                    AgentStockScreen(navController = navController)
                }
                composable(BottomBarScreen.Sales.route){
                    AgentRequestOrderScreen(navController = navController)
                }
                composable(ROUTE_ORDER_HISTORY_SCREEN){
                    OrderHistoryScreen(navController)
                }
                composable(ROUTE_STOCK_IN_SCREEN){
                    StockInScreen(navController)
                }
                composable(ROUTE_STOCK_OUT_SCREEN){
                    StockOutScreen(navController)
                }
            }
        }
    }


//    val state = remember { ScrollState(0) }
//    val agentProductViewModel = AgentProductViewModel()
//    val agentProductList by agentProductViewModel.agentProductList.collectAsState()
//    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
//    val scope = rememberCoroutineScope()
////    val selectedNavBar = StateHolder(0)
//    var selectedItemIndex by rememberSaveable {
//        mutableStateOf(0)
//    }
//    val items = listOf(
//        BottomNavAgentItem(
//            title = "Beranda",
//            selectedIcon = Icons.Filled.Home,
//            unselectedIcon = Icons.Outlined.Home,
//            hasNews = false,
//            route = ROUTE_HOME_INTERNAL_SCREEN
//        ), BottomNavAgentItem(
//            title = "Stok Barang",
//            selectedIcon = ImageVector.vectorResource(id = R.drawable._dcube),
//            unselectedIcon = ImageVector.vectorResource(id = R.drawable.unselected_stock),
//            hasNews = true,
//            route = ROUTE_INTERNAL_STOCK_SCREEN
//        ), BottomNavAgentItem(
//            title = "Penjualan",
//            selectedIcon = ImageVector.vectorResource(id = R.drawable._dcube),
//            unselectedIcon = ImageVector.vectorResource(id = R.drawable.unselected_request_order),
//            hasNews = false,
//            badgeCount = 5,
//            route = ROUTE_INTERNAL_SALES_SCREEN
//        )
//    )
//    ModalNavigationDrawer(
//        drawerState = drawerState,
//        drawerContent = {
//            ModalDrawerSheet(
//                drawerContainerColor = MaterialTheme.colorScheme.surfaceContainerLowest
//            ) {
//                Column {
//                    Text("Drawer title", modifier = Modifier.padding(16.dp))
//                    Divider()
//                    NavigationDrawerItem(
//                        label = { Text(text = "Drawer Item") },
//                        selected = false,
//                        onClick = { /*TODO*/ }
//                    )
//                    IconButton(onClick = {
//                        viewModel!!.logout()
//                        navController.navigate(ROUTE_HOME)
//                    }) {
//                        Icon(imageVector = Icons.Default.Home, contentDescription = "logout")
//                    }
//                }
//
//            }
//        },
//    )
//    {
//        Scaffold(
//            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
//            bottomBar = {
//                NavigationBar(
//                    contentColor = Color.Green,
//                    containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
//                    tonalElevation = 30.dp,
//                ){
//                    items.forEachIndexed{index, item ->
//                        NavigationBarItem(
//                            selected = selectedItemIndex == index,
//                            onClick = {
//                                selectedItemIndex = index
//                                navController.navigate(item.route)
//                            },
//                            label = { Text(text = item.title) } ,
//                            icon = { BadgedBox(
//                                badge = {
//                                    if (item.badgeCount != null){
//                                        Badge {
//                                            Text(text = item.badgeCount.toString())
//                                        }
//                                    }else if(item.hasNews){
//                                        Badge()
//                                    }
//                                }
//                            ) {
//                                Icon(
//                                    imageVector = if (index == selectedItemIndex )
//                                    {
//                                        item.selectedIcon
//                                    }else item.unselectedIcon,
//                                    contentDescription = item.title
//                                )
//                            }
//                            }
//                        )
//                    }
//                }
//            }
//        ){
//            InternalNavigation(viewModelInternal = viewModel!! )
//        }
//    }
}
