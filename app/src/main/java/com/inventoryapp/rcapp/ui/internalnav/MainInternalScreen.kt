package com.inventoryapp.rcapp.ui.internalnav

//noinspection UsingMaterialAndMaterial3Libraries
//noinspection UsingMaterialAndMaterial3Libraries
//noinspection UsingMaterialAndMaterial3Libraries
import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Refresh
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
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
import com.inventoryapp.rcapp.ui.auth.internalauth.AuthViewModel
import com.inventoryapp.rcapp.ui.auth.internalauth.RegisterInternalScreen
import com.inventoryapp.rcapp.ui.internalnav.temporary.HomeScreenRc
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
import com.inventoryapp.rcapp.ui.nav.ROUTE_REGISTER_INTERNAL
import com.inventoryapp.rcapp.ui.viewmodel.AgentUserViewModel
import com.inventoryapp.rcapp.ui.viewmodel.InternalProductViewModel
import com.inventoryapp.rcapp.ui.viewmodel.InternalTransactionViewModel
import com.inventoryapp.rcapp.ui.viewmodel.OfferingPoViewModel
import com.inventoryapp.rcapp.ui.viewmodel.SalesOrderViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "MutableCollectionMutableState")
@Composable
fun MainInternalScreen(
    internalTransactionViewModel: InternalTransactionViewModel,
    salesOrderViewModel: SalesOrderViewModel,
    offeringPoViewModel: OfferingPoViewModel,
    agentUserViewModel: AgentUserViewModel,
    authViewModel: AuthViewModel?,
    internalProductViewModel: InternalProductViewModel,
    navController: NavHostController
){
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(
        rememberTopAppBarState()
    )
    val context = LocalContext.current
    val badgeSize by salesOrderViewModel.salesOrderInternalListSize.collectAsState()
    val navControllerNonHost = rememberNavController()

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = MaterialTheme.colorScheme.surfaceContainerLowest
            ) {
                Column (
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.Start
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
                            if (internalProductViewModel.userRole != null){
                                Text(
                                    text = internalProductViewModel.userRole,
                                    style = MaterialTheme.typography.titleLarge.copy(
                                        fontWeight = FontWeight.Bold,
                                        fontStyle = FontStyle.Italic
                                    ),
                                    modifier = Modifier.padding(start = 8.dp, top = 4.dp, bottom = 6.dp)
                                )
                            }
                        }
                    }
                    HorizontalDivider()
                    Row (
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        IconButton(onClick = {
                            authViewModel?.resetPassword()
                            Toast.makeText(context, "Link reset password telah dikirim ke email anda", Toast.LENGTH_SHORT).show()
                        },
                            modifier = Modifier
                                .padding(8.dp)
                                .size(40.dp)
                        ) {
                            Icon(imageVector = Icons.Filled.Refresh, contentDescription = "logout")
                        }
                        Text(
                            text = "ResetPassword",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Medium
                            ),
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }
                    HorizontalDivider()
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
                BottomBarInternal(
                    badgeCount = badgeSize.size,
                    navController = navControllerNonHost)
            },
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(text = "Halo ${authViewModel?.currentUser?.displayName}",
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
                        IconButton(onClick = {
                            scope.launch {
                                if (internalProductViewModel.userRole == "SalesManager" || internalProductViewModel.userRole == "Sales") {
                                    navController.navigate("cart")
                                } else Toast.makeText(context, "Role tidak diizinkan", Toast.LENGTH_SHORT).show()
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Filled.ShoppingCart,
                                contentDescription = "Localized description",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        },
                    scrollBehavior = scrollBehavior,
                    modifier = Modifier
                )
            }
        ) {
            NavHost(navController = navControllerNonHost, startDestination = BottomBarScreen.HomeInternal.route,
            ){
                composable(BottomBarScreen.HomeInternal.route){
                    HomeScreenRc(internalProductViewModel, navController = navControllerNonHost)
                }
                composable(BottomBarScreen.StockInternal.route){
                    InternalStockScreen(internalProductViewModel, navControllerNonHost)
                }
                composable(BottomBarScreen.SalesInternal.route){
                    InternalSalesScreen(salesOrderViewModel)
                }
                composable(ROUTE_AGENT_VERIFICATION_SCREEN){
                    AgentVerificationScreen(agentUserViewModel)
                }
                composable(ROUTE_REGISTER_INTERNAL){
                    RegisterInternalScreen(viewModel = authViewModel, navController = navControllerNonHost)
                }
                composable(ROUTE_INTERNAL_STOCK_IN_SCREEN){
                    InternalStockInScreen(internalTransactionViewModel, internalProductViewModel, navControllerNonHost)
                }
                composable(ROUTE_INTERNAL_STOCK_OUT_SCREEN){
                    InternalStockOutScreen(internalTransactionViewModel, internalProductViewModel, navControllerNonHost)
                }
                composable(ROUTE_AGENT_STOCK_MONITORING_SCREEN){
                    AgentStockMonitoringScreen(agentUserViewModel)
                }
                composable(ROUTE_OFFERING_PO_FOR_AGENT_SCREEN){
                    OfferingPoForAgentScreen(offeringPoViewModel, agentUserViewModel, internalProductViewModel, navControllerNonHost)
                }
            }
        }
    }
}

@Composable
fun BottomBarInternal(
    badgeCount: Int?,
    navController: NavHostController) {
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
                navController = navController,
                badgeCount = badgeCount
            )
        }
    }
}

@Composable
fun BottomBar(
    badgeCount: Int?,
    navController: NavHostController) {
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
                navController = navController,
                badgeCount = badgeCount
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

    data object Home : BottomBarScreen(
        title = "Beranda",
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home,
        hasNews = false,
        route = ROUTE_HOME_AGENT_SCREEN
    )

    data object HomeInternal : BottomBarScreen(
        title = "Beranda",
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home,
        hasNews = false,
        route = ROUTE_HOME_INTERNAL_SCREEN
    )
    data object Stock : BottomBarScreen(
        title = "Stok Barang",
        selectedIcon = Icons.Filled.Search,
        unselectedIcon = Icons.Sharp.Search,
        hasNews = true,
        route = ROUTE_AGENT_STOCK_SCREEN
    )

    data object StockInternal : BottomBarScreen(
        title = "Stok Barang",
        selectedIcon = Icons.Filled.Search,
        unselectedIcon = Icons.Sharp.Search,
        hasNews = false,
        route = ROUTE_INTERNAL_STOCK_SCREEN
    )

    data object Sales : BottomBarScreen(
        title = "Request Order",
        selectedIcon = Icons.Filled.ShoppingCart,
        unselectedIcon = Icons.Outlined.ShoppingCart,
        hasNews = false,
        badgeCount = 1,
        route = ROUTE_AGENT_REQUEST_ORDER_SCREEN
    )


    data object SalesInternal : BottomBarScreen(
        title = "Penjualan",
        selectedIcon = Icons.Filled.ShoppingCart,
        unselectedIcon = Icons.Outlined.ShoppingCart,
        hasNews = true,
//        badgeCount = 1,
        route = ROUTE_AGENT_REQUEST_ORDER_SCREEN
    )
}
@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun RowScope.AddItem(
    screen: BottomBarScreen,
    currentDestination: NavDestination?,
    badgeCount: Int?,
    navController: NavHostController
) {
//    val offeringAgentSearchList by offeringPoViewModel.offeringAgentList.collectAsState()
    NavigationBarItem(
        label = {
            Text(text = screen.title)
        },
        icon = {
            BadgedBox(
                badge = {
                    if(screen.badgeCount != null) {
                        Badge {
                            Text(text = badgeCount.toString())
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