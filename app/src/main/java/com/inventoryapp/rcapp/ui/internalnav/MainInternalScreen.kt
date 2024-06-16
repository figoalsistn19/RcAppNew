package com.inventoryapp.rcapp.ui.internalnav


import android.annotation.SuppressLint
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.firestore.FirebaseFirestore
import com.inventoryapp.rcapp.data.model.InternalUser
import com.inventoryapp.rcapp.data.model.UserRole
import com.inventoryapp.rcapp.ui.auth.AuthViewModel
import com.inventoryapp.rcapp.ui.auth.internalauth.RegisterInternalScreen
import com.inventoryapp.rcapp.ui.nav.BottomBarInternal
import com.inventoryapp.rcapp.ui.nav.BottomBarScreen
import com.inventoryapp.rcapp.ui.nav.ROUTE_AGENT_STOCK_MONITORING_SCREEN
import com.inventoryapp.rcapp.ui.nav.ROUTE_AGENT_VERIFICATION_SCREEN
import com.inventoryapp.rcapp.ui.nav.ROUTE_HOME
import com.inventoryapp.rcapp.ui.nav.ROUTE_INTERNAL_STOCK_ALERT
import com.inventoryapp.rcapp.ui.nav.ROUTE_INTERNAL_STOCK_IN_SCREEN
import com.inventoryapp.rcapp.ui.nav.ROUTE_INTERNAL_STOCK_OUT_SCREEN
import com.inventoryapp.rcapp.ui.nav.ROUTE_OFFERING_PO_FOR_AGENT_SCREEN
import com.inventoryapp.rcapp.ui.nav.ROUTE_REGISTER_INTERNAL
import com.inventoryapp.rcapp.ui.viewmodel.AgentUserViewModel
import com.inventoryapp.rcapp.ui.viewmodel.InternalProductViewModel
import com.inventoryapp.rcapp.ui.viewmodel.InternalTransactionViewModel
import com.inventoryapp.rcapp.ui.viewmodel.OfferingPoViewModel
import com.inventoryapp.rcapp.ui.viewmodel.SalesOrderViewModel
import com.inventoryapp.rcapp.util.FireStoreCollection
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
    val role = remember {
        mutableStateOf("")
    }
    val data = FirebaseFirestore.getInstance()
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
                            if (authViewModel?.currentUser?.uid != null){
                                data.collection(FireStoreCollection.INTERNALUSER).document(
                                    authViewModel.currentUser?.uid!!
                                ).get()
                                    .addOnSuccessListener {
                                        val roles = it.toObject(InternalUser::class.java)
                                        role.value = roles?.userRole.toString()
                                    }
                                    .addOnFailureListener {
                                        navController.navigate("login"){
                                            popUpTo(ROUTE_HOME){
                                                inclusive = true
                                            }
                                        }
                                    }
                            }

                            Text(
                                text = role.value,
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontStyle = FontStyle.Italic
                                ),
                                modifier = Modifier.padding(start = 8.dp, top = 4.dp, bottom = 6.dp)
                            )
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
                                if (internalProductViewModel.role.value != UserRole.SalesManager || internalProductViewModel.role.value != UserRole.Sales || internalProductViewModel.role.value != UserRole.Admin) {
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
                    InternalHomeScreen(internalProductViewModel, navController = navControllerNonHost)
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
                composable(ROUTE_INTERNAL_STOCK_ALERT){
                    InternalStockAlert(internalProductViewModel)
                }
                composable(ROUTE_OFFERING_PO_FOR_AGENT_SCREEN){
                    OfferingPoForAgentScreen(offeringPoViewModel, agentUserViewModel, internalProductViewModel, navControllerNonHost)
                }
            }
        }
    }
}