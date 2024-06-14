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
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.ShoppingCart
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
import com.inventoryapp.rcapp.ui.auth.agentauth.AuthAgentViewModel
import com.inventoryapp.rcapp.ui.nav.BottomBar
import com.inventoryapp.rcapp.ui.nav.BottomBarScreen
import com.inventoryapp.rcapp.ui.nav.ROUTE_HOME
import com.inventoryapp.rcapp.ui.nav.ROUTE_ORDER_HISTORY_SCREEN
import com.inventoryapp.rcapp.ui.nav.ROUTE_STOCK_IN_SCREEN
import com.inventoryapp.rcapp.ui.nav.ROUTE_STOCK_OUT_SCREEN
import com.inventoryapp.rcapp.ui.viewmodel.AgentProductViewModel
import com.inventoryapp.rcapp.ui.viewmodel.AgentTransactionViewModel
import com.inventoryapp.rcapp.ui.viewmodel.InternalProductViewModel
import com.inventoryapp.rcapp.ui.viewmodel.OfferingPoViewModel
import com.inventoryapp.rcapp.ui.viewmodel.SalesOrderViewModel
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
                        IconButton(onClick = {
                            scope.launch {
                                navController.navigate("cartAgent")
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
}
