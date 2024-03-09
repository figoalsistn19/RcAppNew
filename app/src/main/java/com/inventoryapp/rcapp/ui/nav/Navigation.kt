package com.inventoryapp.rcapp.ui.nav

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.inventoryapp.rcapp.R
import com.inventoryapp.rcapp.ui.WelcomeScreen
import com.inventoryapp.rcapp.ui.agentnav.AgentRequestOrderScreen
import com.inventoryapp.rcapp.ui.agentnav.AgentStockScreen
import com.inventoryapp.rcapp.ui.agentnav.HomeAgentScreen
import com.inventoryapp.rcapp.ui.auth.agentauth.LoginAgentScreen
import com.inventoryapp.rcapp.ui.auth.agentauth.RegisterAgentScreen
import com.inventoryapp.rcapp.ui.auth.agentauth.AuthAgentViewModel
import com.inventoryapp.rcapp.ui.theme.BtnAgenMitra
import kotlinx.coroutines.delay

@Composable
fun MainNavigation(
    viewModel: AuthAgentViewModel,
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier,
    startDestination: String = ROUTE_HOME
) {
    NavHost(navController = navController, startDestination = startDestination){
        composable(ROUTE_HOME){
            AgentRequestOrderScreen()
        }
        composable(ROUTE_LOGIN_AGENT) {
            LoginAgentScreen(viewModel, navController)
        }
        composable(ROUTE_REGISTER_AGENT){
            RegisterAgentScreen(viewModel, navController)
        }
        composable(ROUTE_LOGIN_INTERNAL){
            LoginAgentScreen(viewModel, navController)
        }
        composable(ROUTE_REGISTER_INTERNAL){
            RegisterAgentScreen (viewModel,navController)
        }
        composable("home_internal"){
            InternalScreen()
        }
        composable("home_agent"){
            InternalScreen()
        }
    }
}

@Composable
fun AgentNavigation(
    viewModel: AuthAgentViewModel,
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier,
    startDestination: String = ROUTE_HOME
) {
    NavHost(navController = navController, startDestination = startDestination){
        composable(ROUTE_HOME){
            WelcomeScreen(navController)
        }
    }
}
//@Composable
//fun AgentNavigation(){
//    val navController = rememberNavController()
//}


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
fun Logo(
    painter: Painter = if (isSystemInDarkTheme()) painterResource(id = R.drawable.rc_logodark) else painterResource(
        id = R.drawable.rc_logo
    ),
    contentDescription: String = "ini logo Rc",
    modifier: Modifier = Modifier
){
    Image(
        painter = painter,
        contentDescription = contentDescription,
        contentScale = ContentScale.Fit,
        modifier = modifier)
}

@Composable
fun CustomToast(message: String) {
    val duration = remember { mutableStateOf(3000) }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier.width(200.dp),
            colors = CardColors(Color.Blue,Color.White,Color.Black,Color.Blue),
            elevation = CardDefaults.cardElevation(10.dp,10.dp,15.dp,15.dp)
        ) {
            Text(
                text = message,
                modifier = Modifier.padding(16.dp),
                color = Color.White
            )
        }
    }

    LaunchedEffect(key1 = duration) {
        delay(duration.value.toLong())
        duration.value = 0
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