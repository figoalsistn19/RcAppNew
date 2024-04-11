package com.inventoryapp.rcapp.ui.internalnav

//noinspection UsingMaterialAndMaterial3Libraries
//noinspection UsingMaterialAndMaterial3Libraries
//noinspection UsingMaterialAndMaterial3Libraries
import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.icons.sharp.Search
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
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.inventoryapp.rcapp.ui.agentnav.viewmodel.AgentProductViewModel
import com.inventoryapp.rcapp.ui.auth.internalauth.AuthInternalViewModel
import com.inventoryapp.rcapp.ui.internalnav.viewmodel.AgentUserViewModel
import com.inventoryapp.rcapp.ui.internalnav.viewmodel.InternalProductViewModel
import com.inventoryapp.rcapp.ui.nav.ROUTE_AGENT_REQUEST_ORDER_SCREEN
import com.inventoryapp.rcapp.ui.nav.ROUTE_AGENT_STOCK_MONITORING_SCREEN
import com.inventoryapp.rcapp.ui.nav.ROUTE_AGENT_STOCK_SCREEN
import com.inventoryapp.rcapp.ui.nav.ROUTE_AGENT_VERIFICATION_SCREEN
import com.inventoryapp.rcapp.ui.nav.ROUTE_HOME
import com.inventoryapp.rcapp.ui.nav.ROUTE_HOME_AGENT_SCREEN
import com.inventoryapp.rcapp.ui.nav.ROUTE_HOME_INTERNAL_SCREEN
import com.inventoryapp.rcapp.ui.nav.ROUTE_INTERNAL_STOCK_IN_SCREEN
import com.inventoryapp.rcapp.ui.nav.ROUTE_INTERNAL_STOCK_OUT_SCREEN
import com.inventoryapp.rcapp.ui.nav.ROUTE_INTERNAL_STOCK_SCREEN
import com.inventoryapp.rcapp.ui.nav.ROUTE_OFFERING_PO_FOR_AGENT_SCREEN
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainInternalScreen(
    agentProductViewModel: AgentProductViewModel,
    agentUserViewModel: AgentUserViewModel,
    authViewModel: AuthInternalViewModel,
    internalProductViewModel: InternalProductViewModel
//    navController: NavHostController
){
//    val navController = rememberNavController()
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
                        authViewModel.logout()
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
                BottomBarInternal(navController = navController)
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
            NavHost(navController = navController, startDestination = BottomBarScreen.HomeInternal.route,
            ){
                composable(BottomBarScreen.HomeInternal.route){
                    InternalHomeScreen(agentProductViewModel, navController = navController)
                }
                composable(BottomBarScreen.StockInternal.route){
                    InternalStockScreen(agentProductViewModel, internalProductViewModel, authViewModel, navController)
                }
                composable(BottomBarScreen.SalesInternal.route){
                    InternalSalesScreen(authViewModel, navController)
                }
                composable(ROUTE_AGENT_VERIFICATION_SCREEN){
                    AgentVerificationScreen(agentUserViewModel)
                }
                composable(ROUTE_INTERNAL_STOCK_IN_SCREEN){
                    InternalStockInScreen(agentProductViewModel, navController)
                }
                composable(ROUTE_INTERNAL_STOCK_OUT_SCREEN){
                    InternalStockOutScreen(agentProductViewModel, navController)
                }
                composable(ROUTE_AGENT_STOCK_MONITORING_SCREEN){
                    AgentStockMonitoringScreen()
                }
                composable(ROUTE_OFFERING_PO_FOR_AGENT_SCREEN){
                    OfferingPoForAgentScreen(agentUserViewModel, internalProductViewModel, navController)
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

@Composable
fun BottomBarInternal(navController: NavHostController) {
    val screens = listOf(
        BottomBarScreen.HomeInternal,
        BottomBarScreen.StockInternal,
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

@Composable
fun BottomBar(navController: NavHostController) {
    val screens = listOf(
        BottomBarScreen.Home,
        BottomBarScreen.Stock,
        BottomBarScreen.Sales,
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

    object HomeInternal : BottomBarScreen(
        title = "Beranda",
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home,
        hasNews = false,
        route = ROUTE_HOME_INTERNAL_SCREEN
    )
    object Stock : BottomBarScreen(
        title = "Stok Barang",
        selectedIcon = Icons.Filled.Search,
        unselectedIcon = Icons.Sharp.Search,
        hasNews = true,
        route = ROUTE_AGENT_STOCK_SCREEN
    )

    object StockInternal : BottomBarScreen(
        title = "Stok Barang",
        selectedIcon = Icons.Filled.Search,
        unselectedIcon = Icons.Sharp.Search,
        hasNews = true,
        route = ROUTE_INTERNAL_STOCK_SCREEN
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