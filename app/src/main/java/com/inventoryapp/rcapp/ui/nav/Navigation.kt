package com.inventoryapp.rcapp.ui.nav

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.inventoryapp.rcapp.ui.WelcomeScreen
import com.inventoryapp.rcapp.ui.agentnav.CartScreenForAgent
import com.inventoryapp.rcapp.ui.agentnav.MainAgentScreen
import com.inventoryapp.rcapp.ui.auth.AuthViewModel
import com.inventoryapp.rcapp.ui.auth.LoginScreen
import com.inventoryapp.rcapp.ui.auth.agentauth.AuthAgentViewModel
import com.inventoryapp.rcapp.ui.auth.agentauth.RegisterAgentScreen
import com.inventoryapp.rcapp.ui.auth.internalauth.LoginInternalScreen
import com.inventoryapp.rcapp.ui.auth.internalauth.RegisterInternalScreen
import com.inventoryapp.rcapp.ui.internalnav.CartScreen
import com.inventoryapp.rcapp.ui.internalnav.InternalHomeScreen
import com.inventoryapp.rcapp.ui.internalnav.InternalSalesScreen
import com.inventoryapp.rcapp.ui.internalnav.InternalStockScreen
import com.inventoryapp.rcapp.ui.internalnav.MainInternalScreen
import com.inventoryapp.rcapp.ui.internalnav.SalesOrderByDateScreen
import com.inventoryapp.rcapp.ui.viewmodel.AgentProductViewModel
import com.inventoryapp.rcapp.ui.viewmodel.AgentTransactionViewModel
import com.inventoryapp.rcapp.ui.viewmodel.AgentUserViewModel
import com.inventoryapp.rcapp.ui.viewmodel.InternalProductViewModel
import com.inventoryapp.rcapp.ui.viewmodel.InternalTransactionViewModel
import com.inventoryapp.rcapp.ui.viewmodel.OfferingPoViewModel
import com.inventoryapp.rcapp.ui.viewmodel.SalesOrderViewModel

@Composable
fun MainNavigation(
    internalTransactionViewModel: InternalTransactionViewModel,
    salesOrderViewModel: SalesOrderViewModel,
    offeringPoViewModel: OfferingPoViewModel,
    agentTransactionViewModel: AgentTransactionViewModel,
    agentProductViewModel: AgentProductViewModel,
    agentUserViewModel: AgentUserViewModel,
    authViewModelAgent: AuthAgentViewModel,
    authViewModelInternal: AuthViewModel,
    internalProductViewModel: InternalProductViewModel,
    navController: NavHostController = rememberNavController()
) {
    NavHost(navController = navController, startDestination = ROUTE_HOME, route = "main_nav") {
        composable(ROUTE_HOME) {
            WelcomeScreen(authViewModelInternal, navController)
        }
        composable(ROUTE_REGISTER_AGENT){
            RegisterAgentScreen(authViewModelAgent, navController)
        }
        composable(ROUTE_LOGIN_INTERNAL){
            LoginInternalScreen(authViewModelInternal, navController)
        }
        composable(ROUTE_REGISTER_INTERNAL){
            RegisterInternalScreen (authViewModelInternal,navController)
        }
        composable(ROUTE_MAIN_INTERNAL_SCREEN){
            MainInternalScreen(internalTransactionViewModel, salesOrderViewModel, offeringPoViewModel, agentUserViewModel,authViewModelInternal,internalProductViewModel, navController )
        }
        composable(ROUTE_MAIN_AGENT_SCREEN){
            MainAgentScreen(salesOrderViewModel, offeringPoViewModel, agentTransactionViewModel, agentProductViewModel, internalProductViewModel, authViewModelAgent, navController)
        }

        //INTERNAL NAV
        composable(ROUTE_HOME_INTERNAL_SCREEN){
            InternalHomeScreen(internalProductViewModel, navController = navController)
        }
        composable(ROUTE_INTERNAL_STOCK_SCREEN){
            InternalStockScreen(internalProductViewModel, navController)
        }
        composable(ROUTE_INTERNAL_SALES_SCREEN){
            InternalSalesScreen(salesOrderViewModel)
        }
        composable("cart"){
            CartScreen(
                agentUserViewModel,
                internalProductViewModel = internalProductViewModel,
                salesOrderViewModel = salesOrderViewModel,
                navController = navController
            )
        }
        composable("cartAgent"){
            CartScreenForAgent(
                salesOrderViewModel = salesOrderViewModel,
                internalProductViewModel = internalProductViewModel,
                navController = navController
            )
        }
        composable(ROUTE_LOGIN){
            LoginScreen(viewModel = authViewModelInternal, navController = navController)
        }
        composable("datepicker"){
            SalesOrderByDateScreen()
        }
//        composable(ROUTE_MAIN_SALES_MANAGER_SCREEN){
//            MainSalesManagerScreen(
//                internalTransactionViewModel = internalTransactionViewModel,
//                salesOrderViewModel = salesOrderViewModel,
//                offeringPoViewModel = offeringPoViewModel,
//                agentUserViewModel = agentUserViewModel,
//                authViewModel = authViewModelInternal,
//                internalProductViewModel = internalProductViewModel,
//                navController = navController
//            )
//        }
//        composable(ROUTE_AGENT_VERIFICATION_SCREEN){
//            AgentVerificationScreen(agentUserViewModel)
//        }
//        composable(ROUTE_REGISTER_INTERNAL){
//            RegisterInternalScreen(viewModel = authViewModelInternal, navController = navController)
//        }
//        composable(ROUTE_INTERNAL_STOCK_IN_SCREEN){
//            InternalStockInScreen(internalTransactionViewModel, internalProductViewModel, navController)
//        }
//        composable(ROUTE_INTERNAL_STOCK_OUT_SCREEN){
//            InternalStockOutScreen(internalTransactionViewModel, internalProductViewModel, navController)
//        }
//        composable(ROUTE_AGENT_STOCK_MONITORING_SCREEN){
//            AgentStockMonitoringScreen(agentUserViewModel)
//        }
//        composable(ROUTE_OFFERING_PO_FOR_AGENT_SCREEN){
//            OfferingPoForAgentScreen(offeringPoViewModel, agentUserViewModel, internalProductViewModel, navController)
//        }
    }
}



@Preview
@Composable
fun InternalScreen(){
    Box(contentAlignment = Alignment.Center, modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.surface)){
        Text(text = "Selamat datang agen Rc")
    }
}