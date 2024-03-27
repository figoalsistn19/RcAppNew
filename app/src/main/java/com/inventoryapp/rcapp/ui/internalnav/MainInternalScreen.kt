package com.inventoryapp.rcapp.ui.internalnav

//noinspection UsingMaterialAndMaterial3Libraries
//noinspection UsingMaterialAndMaterial3Libraries
//noinspection UsingMaterialAndMaterial3Libraries
import android.annotation.SuppressLint
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.icons.sharp.Search
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.inventoryapp.rcapp.ui.auth.internalauth.AuthInternalViewModel
import com.inventoryapp.rcapp.ui.nav.BottomNavAgentItem
import com.inventoryapp.rcapp.ui.nav.ROUTE_AGENT_REQUEST_ORDER_SCREEN
import com.inventoryapp.rcapp.ui.nav.ROUTE_AGENT_STOCK_MONITORING_SCREEN
import com.inventoryapp.rcapp.ui.nav.ROUTE_AGENT_STOCK_SCREEN
import com.inventoryapp.rcapp.ui.nav.ROUTE_AGENT_VERIFICATION_SCREEN
import com.inventoryapp.rcapp.ui.nav.ROUTE_HOME_AGENT_SCREEN
import com.inventoryapp.rcapp.ui.nav.ROUTE_HOME_INTERNAL_SCREEN
import com.inventoryapp.rcapp.ui.nav.ROUTE_INTERNAL_SALES_SCREEN
import com.inventoryapp.rcapp.ui.nav.ROUTE_INTERNAL_STOCK_IN_SCREEN
import com.inventoryapp.rcapp.ui.nav.ROUTE_INTERNAL_STOCK_OUT_SCREEN
import com.inventoryapp.rcapp.ui.nav.ROUTE_OFFERING_PO_FOR_AGENT_SCREEN
import com.inventoryapp.rcapp.ui.nav.ROUTE_STOCK_IN_SCREEN

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainInternalScreen(
    viewModel: AuthInternalViewModel
//    navController: NavHostController
){
//    val navController = rememberNavController()
    val navController = rememberNavController()

    Scaffold(
        bottomBar = {
            BottomBar(navController = navController)
        }
    ) {
        NavHost(navController = navController, startDestination = BottomBarScreen.Home.route,
        ){
            composable(BottomBarScreen.Home.route){
                InternalHomeScreen(navController = navController)
            }
            composable(BottomBarScreen.Stock.route){
                InternalStockScreen(viewModel, navController)
            }
            composable(BottomBarScreen.SalesInternal.route){
                InternalSalesScreen(viewModel, navController)
            }
            composable(ROUTE_AGENT_VERIFICATION_SCREEN){
                AgentVerificationScreen()
            }
            composable(ROUTE_INTERNAL_STOCK_IN_SCREEN){
                InternalStockInScreen(navController = navController)
            }
            composable(ROUTE_INTERNAL_STOCK_OUT_SCREEN){
                InternalStockOutScreen(navController = navController)
            }
            composable(ROUTE_AGENT_STOCK_MONITORING_SCREEN){
                AgentStockMonitoringScreen()
            }
            composable(ROUTE_OFFERING_PO_FOR_AGENT_SCREEN){
                OfferingPoForAgentScreen(navController)
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

@Composable
fun BottomBar(navController: NavHostController) {
    val screens = listOf(
        BottomBarScreen.Home,
        BottomBarScreen.Stock,
        BottomBarScreen.SalesInternal,
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar (
        contentColor = Color.Green,
        containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
        tonalElevation = 30.dp
    ){
        screens.forEach { screen ->
            AddItem(
                screen = screen,
                currentDestination = currentDestination,
                navController = navController
            )
        }
    }
}


sealed class BottomBarScreen(
    val route: String,
    val title: String,
    val badgeCount: Int? = null,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val hasNews: Boolean
) {
    object Home : BottomBarScreen(
        title = "Beranda",
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home,
        hasNews = false,
        route = ROUTE_HOME_AGENT_SCREEN
    )

    object Stock : BottomBarScreen(
        title = "Stok Barang",
        selectedIcon = Icons.Filled.Search,
        unselectedIcon = Icons.Sharp.Search,
        hasNews = true,
        route = ROUTE_AGENT_STOCK_SCREEN
    )

    object Sales : BottomBarScreen(
        title = "Request Order",
        selectedIcon = Icons.Filled.ShoppingCart,
        unselectedIcon = Icons.Outlined.ShoppingCart,
        hasNews = false,
        badgeCount = 5,
        route = ROUTE_AGENT_REQUEST_ORDER_SCREEN
    )

    object SalesInternal : BottomBarScreen(
        title = "Penjualan",
        selectedIcon = Icons.Filled.ShoppingCart,
        unselectedIcon = Icons.Outlined.ShoppingCart,
        hasNews = false,
        badgeCount = 5,
        route = ROUTE_AGENT_REQUEST_ORDER_SCREEN
    )
}
@Composable
fun RowScope.AddItem(
    screen: BottomBarScreen,
    currentDestination: NavDestination?,
    navController: NavHostController
) {
    NavigationBarItem(
        label = {
            Text(text = screen.title)
        },
        icon = {
            BadgedBox(
                badge = {
                    if(screen.badgeCount != null) {
                        Badge {
                            Text(text = screen.badgeCount.toString())
                        }
                    } else if(screen.hasNews) {
                        Badge()
                    }
                }
            ) {
                Icon(
                    imageVector = if (screen.route == currentDestination?.route) {
                        screen.selectedIcon
                    } else screen.unselectedIcon,
                    contentDescription = screen.title
                )
            }
        },
        selected = currentDestination?.hierarchy?.any {
            it.route == screen.route
        } == true,
        onClick = {
            navController.navigate(screen.route) {
                popUpTo(navController.graph.findStartDestination().id)
                launchSingleTop = true
            }
        }
    )
}