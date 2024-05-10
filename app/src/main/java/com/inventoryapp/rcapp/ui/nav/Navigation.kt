package com.inventoryapp.rcapp.ui.nav

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.inventoryapp.rcapp.ui.WelcomeScreen
import com.inventoryapp.rcapp.ui.agentnav.MainAgentScreen
import com.inventoryapp.rcapp.ui.viewmodel.AgentProductViewModel
import com.inventoryapp.rcapp.ui.viewmodel.AgentTransactionViewModel
import com.inventoryapp.rcapp.ui.viewmodel.SalesOrderViewModel
import com.inventoryapp.rcapp.ui.auth.LoginScreen
import com.inventoryapp.rcapp.ui.auth.agentauth.AuthAgentViewModel
import com.inventoryapp.rcapp.ui.auth.agentauth.LoginAgentScreen
import com.inventoryapp.rcapp.ui.auth.agentauth.RegisterAgentScreen
import com.inventoryapp.rcapp.ui.auth.internalauth.AuthViewModel
import com.inventoryapp.rcapp.ui.auth.internalauth.LoginInternalScreen
import com.inventoryapp.rcapp.ui.auth.internalauth.RegisterInternalScreen
import com.inventoryapp.rcapp.ui.internalnav.AgentStockMonitoringScreen
import com.inventoryapp.rcapp.ui.internalnav.AgentVerificationScreen
import com.inventoryapp.rcapp.ui.internalnav.CartScreen
import com.inventoryapp.rcapp.ui.internalnav.InternalHomeScreen
import com.inventoryapp.rcapp.ui.internalnav.InternalSalesScreen
import com.inventoryapp.rcapp.ui.internalnav.InternalStockInScreen
import com.inventoryapp.rcapp.ui.internalnav.InternalStockOutScreen
import com.inventoryapp.rcapp.ui.internalnav.InternalStockScreen
import com.inventoryapp.rcapp.ui.internalnav.MainInternalScreen
import com.inventoryapp.rcapp.ui.internalnav.OfferingPoForAgentScreen
import com.inventoryapp.rcapp.ui.internalnav.SalesOrderByDateScreen
import com.inventoryapp.rcapp.ui.internalnav.temporary.CartScreenRc
import com.inventoryapp.rcapp.ui.viewmodel.AgentUserViewModel
import com.inventoryapp.rcapp.ui.viewmodel.InternalProductViewModel
import com.inventoryapp.rcapp.ui.viewmodel.InternalTransactionViewModel
import com.inventoryapp.rcapp.ui.viewmodel.OfferingPoViewModel
import com.inventoryapp.rcapp.ui.theme.BtnAgenMitra

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
        composable(ROUTE_LOGIN_AGENT) {
            LoginAgentScreen(authViewModelAgent, navController)
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
            CartScreenRc(
                agentUserViewModel,
                internalProductViewModel = internalProductViewModel,
                salesOrderViewModel = salesOrderViewModel,
                navController = navController
            )
        }
        composable("login"){
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
        composable(ROUTE_AGENT_VERIFICATION_SCREEN){
            AgentVerificationScreen(agentUserViewModel)
        }
        composable(ROUTE_REGISTER_INTERNAL){
            RegisterInternalScreen(viewModel = authViewModelInternal, navController = navController)
        }
        composable(ROUTE_INTERNAL_STOCK_IN_SCREEN){
            InternalStockInScreen(internalTransactionViewModel, internalProductViewModel, navController)
        }
        composable(ROUTE_INTERNAL_STOCK_OUT_SCREEN){
            InternalStockOutScreen(internalTransactionViewModel, internalProductViewModel, navController)
        }
        composable(ROUTE_AGENT_STOCK_MONITORING_SCREEN){
            AgentStockMonitoringScreen(agentUserViewModel)
        }
        composable(ROUTE_OFFERING_PO_FOR_AGENT_SCREEN){
            OfferingPoForAgentScreen(offeringPoViewModel, agentUserViewModel, internalProductViewModel, navController)
        }
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

@Composable
fun SmallButtonPrimary(text: String, navController: Unit, modifier: Modifier){
    Button(onClick = {
        navController
                     },
        modifier = modifier
            .width(190.dp)
            .height(40.dp),
        colors = ButtonColors(containerColor = MaterialTheme.colorScheme.primary, contentColor = MaterialTheme.colorScheme.onPrimary, disabledContainerColor = Color(0xFFF9F9F9), disabledContentColor = Color(0xFFF9F9F9))
    ) {
        Text(
            text = text,
            style = TextStyle(
                fontSize = 14.sp,
                lineHeight = 20.sp,
                fontWeight = FontWeight(500),
                textAlign = TextAlign.Center,
                letterSpacing = 0.1.sp,
            )
        )
    }
}

@Composable
fun ButtonRegister(text: String, onNavigateTo: () -> Unit, validation: () -> Unit, modifier: Modifier){
    val showToast = remember { mutableStateOf(false) }
    Button(onClick = {
        validation()
        onNavigateTo()
    },
        modifier = modifier
            .width(190.dp)
            .height(40.dp),
        colors = ButtonColors(containerColor = MaterialTheme.colorScheme.primary, contentColor = MaterialTheme.colorScheme.onPrimary, disabledContainerColor = Color(0xFFF9F9F9), disabledContentColor = Color(0xFFF9F9F9))
    ) {
        Text(
            text = text,
            style = TextStyle(
                fontSize = 14.sp,
                lineHeight = 20.sp,
                fontWeight = FontWeight(500),
                textAlign = TextAlign.Center,
                letterSpacing = 0.1.sp,
            )
        )
    }
}

@Composable
fun SmallButtonSecondary(text: String, onNavigateTo: () -> Unit){
    Button(onClick = {onNavigateTo()},
        modifier = Modifier
            .width(190.dp)
            .height(40.dp),
        colors = ButtonColors(MaterialTheme.colorScheme.primary,MaterialTheme.colorScheme.onPrimary, MaterialTheme.colorScheme.secondary, MaterialTheme.colorScheme.onSecondary)
    ) {
        Text(
            text = text,
            style = TextStyle(
                fontSize = 14.sp,
                lineHeight = 20.sp,
                fontWeight = FontWeight(500),
                color = Color(0xFFFFFFFF),
                textAlign = TextAlign.Center,
                letterSpacing = 0.1.sp,
            )
        )
    }
}



@Composable
fun LargeBtn(text: String, navController: Unit){
    Button(onClick = {navController},
        modifier = Modifier
            .width(239.dp)
            .height(55.dp)
            .offset(y = 10.dp),
        colors = ButtonColors(containerColor = BtnAgenMitra, contentColor = MaterialTheme.colorScheme.onPrimary, disabledContainerColor = Color(0xFFF9F9F9), disabledContentColor = Color(0xFFF9F9F9))
    )
    {
        Text(
            text = text,
            style = TextStyle(
                fontSize = 20.sp,
                lineHeight = 20.sp,
                fontWeight = FontWeight(600),
                textAlign = TextAlign.Center,
            )
        )
    }
}

@Composable
fun Greeting(modifier: Modifier = Modifier, title: String, subtitle: String) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .width(350.dp)
            .padding(top = 10.dp)
    )
    {
        Text(
            title,
            style = TextStyle(
                fontSize = 42.sp,
                lineHeight = 39.5.sp,
                fontWeight = FontWeight(300),
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
            )
        )
        Text(
            text = subtitle,
            style = TextStyle(
                fontSize = 16.sp,
                lineHeight = 17.5.sp,
                fontWeight = FontWeight(300),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
        )
    }
}