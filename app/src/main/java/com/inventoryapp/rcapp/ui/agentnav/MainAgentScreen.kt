package com.inventoryapp.rcapp.ui.agentnav

//noinspection UsingMaterialAndMaterial3Libraries
import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.AccountCircle
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.inventoryapp.rcapp.ui.viewmodel.AgentProductViewModel
import com.inventoryapp.rcapp.ui.viewmodel.AgentTransactionViewModel
import com.inventoryapp.rcapp.ui.viewmodel.SalesOrderViewModel
import com.inventoryapp.rcapp.ui.auth.agentauth.AuthAgentViewModel
import com.inventoryapp.rcapp.ui.internalnav.BottomBar
import com.inventoryapp.rcapp.ui.internalnav.BottomBarScreen
import com.inventoryapp.rcapp.ui.viewmodel.InternalProductViewModel
import com.inventoryapp.rcapp.ui.viewmodel.OfferingPoViewModel
import com.inventoryapp.rcapp.ui.nav.ROUTE_HOME
import com.inventoryapp.rcapp.ui.nav.ROUTE_ORDER_HISTORY_SCREEN
import com.inventoryapp.rcapp.ui.nav.ROUTE_STOCK_IN_SCREEN
import com.inventoryapp.rcapp.ui.nav.ROUTE_STOCK_OUT_SCREEN
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainAgentScreen(
    salesOrderViewModel: SalesOrderViewModel,
    offeringPoViewModel: OfferingPoViewModel,
    agentTransactionViewModel: AgentTransactionViewModel,
    agentProductViewModel: AgentProductViewModel,
    internalProductViewModel: InternalProductViewModel,
    authViewModel: AuthAgentViewModel?,
    navController: NavHostController
){
//    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(
        rememberTopAppBarState()
    )
    val offeringAgentSearchList by offeringPoViewModel.offeringAgentList.collectAsState()
    val navControllerNonHost = rememberNavController()
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = MaterialTheme.colorScheme.surfaceContainerLowest
            ) {
                Column (
                    modifier = Modifier.fillMaxSize(),
                ){
                    Row (
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Image(
                            imageVector = Icons.Filled.AccountCircle,
                            contentDescription = "foto profil",
                            contentScale = ContentScale.FillBounds,
                            modifier = Modifier
                                .padding(8.dp)
                                .size(80.dp)
                        )
                        Column (
                            verticalArrangement = Arrangement.SpaceBetween
                        ){
                            Text(
                                text = authViewModel?.currentUser?.displayName?:"",
                                style = MaterialTheme.typography.headlineSmall.copy(
                                    fontWeight = FontWeight.SemiBold
                                ),
                                modifier = Modifier.padding(start = 8.dp, top = 4.dp)
                            )
                            Text(
                                text = authViewModel?.currentUser?.email?:"",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Normal,
                                    fontStyle = FontStyle.Italic
                                ),
                                modifier = Modifier.padding(start = 8.dp, top = 4.dp)
                            )
                        }
                    }
                    Divider()
                    Spacer(modifier = Modifier
                        .fillMaxWidth()
                    )
                    Row (
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        IconButton(onClick = {
                            authViewModel?.logout()
                            navController.navigate("login"){
                                popUpTo(ROUTE_HOME){
                                    inclusive = true
                                }
                            }
                        },
                            modifier = Modifier
                                .padding(8.dp)
                                .size(40.dp)
                        ) {
                            Icon(imageVector = Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "logout")
                        }
                        Text(
                            text = "Keluar Aplikasi",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Medium
                            ),
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }

                }

            }
        },
    )
    {
        Scaffold(
            bottomBar = {
                BottomBar(offeringAgentSearchList.size, navController = navControllerNonHost)
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
            NavHost(navController = navControllerNonHost, startDestination = BottomBarScreen.Home.route,
            ){
                composable(BottomBarScreen.Home.route){
                    HomeAgentScreen(agentProductViewModel,navController = navControllerNonHost)
                }
                composable(BottomBarScreen.Stock.route){
                    AgentStockScreen(agentProductViewModel, internalProductViewModel, navController = navControllerNonHost)
                }
                composable(BottomBarScreen.Sales.route){
                    AgentRequestOrderScreen(salesOrderViewModel, offeringPoViewModel, agentProductViewModel, navController = navControllerNonHost)
                }
                composable(ROUTE_ORDER_HISTORY_SCREEN){
                    OrderHistoryScreen(salesOrderViewModel, navControllerNonHost)
                }
                composable(ROUTE_STOCK_IN_SCREEN){
                    StockInScreen(agentTransactionViewModel, agentProductViewModel, navControllerNonHost)
                }
                composable(ROUTE_STOCK_OUT_SCREEN){
                    StockOutScreen(agentTransactionViewModel, agentProductViewModel, navControllerNonHost)
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
