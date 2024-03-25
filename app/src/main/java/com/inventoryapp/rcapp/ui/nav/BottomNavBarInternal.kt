package com.inventoryapp.rcapp.ui.nav

import android.annotation.SuppressLint
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.inventoryapp.rcapp.R
import com.inventoryapp.rcapp.ui.agentnav.viewmodel.StateHolder

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun BottomNavBarInternal(viewModel: BottomNavAgentViewModel, navController: NavController){
//    val updateSelectedIndex by viewModel.selectedItemIndexed
//    val selectedIndexState by viewModel.selectedItemIndexed.collectAsState()
//    var selectedItemUpdated = viewModel
    val count by viewModel.count.collectAsState()
    val selectedNavBar = StateHolder(0)
    val items = listOf(
        BottomNavAgentItem(
            title = "Beranda",
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home,
            hasNews = false,
            route = ROUTE_HOME_INTERNAL_SCREEN
        ), BottomNavAgentItem(
            title = "Stok Barang",
            selectedIcon = ImageVector.vectorResource(id = R.drawable._dcube),
            unselectedIcon = ImageVector.vectorResource(id = R.drawable.unselected_stock),
            hasNews = true,
            route = ROUTE_INTERNAL_STOCK_SCREEN
        ), BottomNavAgentItem(
            title = "Penjualan",
            selectedIcon = ImageVector.vectorResource(id = R.drawable._dcube),
            unselectedIcon = ImageVector.vectorResource(id = R.drawable.unselected_request_order),
            hasNews = false,
            badgeCount = 5,
            route = ROUTE_INTERNAL_SALES_SCREEN
        )
    )
    NavigationBar(
        contentColor = Color.Green,
        containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
        tonalElevation = 30.dp,
    ){
        items.forEachIndexed{index, item ->
            NavigationBarItem(
                selected = selectedNavBar.valueNavBar == index,
                onClick = {
//                    viewModel.updateCount(index)
                    selectedNavBar.updateValueNavBar(index)
                    navController.navigate(item.route)
                },
                label = { Text(text = item.title) } ,
                icon = { BadgedBox(
                    badge = {
                        if (item.badgeCount != null){
                            Badge {
                                Text(text = item.badgeCount.toString())
                            }
                        }else if(item.hasNews){
                            Badge()
                        }
                    }
                ) {
                    Icon(
                        imageVector = if (selectedNavBar.valueNavBar == index)
                        {
                            item.selectedIcon
                        }else item.unselectedIcon,
                        contentDescription = item.title
                    )
                }
                }
            )
        }
    }
}