package com.inventoryapp.rcapp.ui.nav

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.icons.sharp.Search
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState


@Composable
fun BottomBarInternal(
    badgeCount: Int?,
    navController: NavHostController
) {
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
    navController: NavHostController
) {
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
        badgeCount = 5,
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