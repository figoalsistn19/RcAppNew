package com.inventoryapp.rcapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import com.inventoryapp.rcapp.ui.nav.BottomNavAgentItem
import com.inventoryapp.rcapp.ui.theme.RcAppTheme

class AgentActivity : ComponentActivity() {

    private lateinit var navController: NavController
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RcAppTheme {
               val items = listOf(
                   BottomNavAgentItem(
                   title = "Beranda",
                   selectedIcon = Icons.Filled.Home,
                   unselectedIcon = Icons.Outlined.Home,
                   hasNews = false,
               ), BottomNavAgentItem(
                   title = "Stok Barang",
                   selectedIcon = ImageVector.vectorResource(id = R.drawable._dcube),
                   unselectedIcon = ImageVector.vectorResource(id = R.drawable.unselected_stock),
                   hasNews = true,
               ), BottomNavAgentItem(
                   title = "RequestOrder",
                   selectedIcon = ImageVector.vectorResource(id = R.drawable._dcube),
                   unselectedIcon = ImageVector.vectorResource(id = R.drawable.unselected_request_order),
                   hasNews = false,
                   badgeCount = 5
               )
               )
                var selectedItemIndexed by rememberSaveable {
                     mutableIntStateOf(0  )
                }
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Scaffold (
                        bottomBar = {
                            NavigationBar{
                            items.forEachIndexed{index, item ->
                                NavigationBarItem(
                                    selected = selectedItemIndexed == index,
                                    onClick = {
                                              selectedItemIndexed = index
                                        navController.navigate(item.title)
                                              },
                                    label = { Text(text = item.title)} ,
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
                                        androidx.compose.material3.Icon(
                                            imageVector = if (index==selectedItemIndexed)
                                            {
                                            item.selectedIcon
                                            }else item.unselectedIcon,
                                            contentDescription = item.title
                                        )
                                    }
                                    })

                            }
                        }
                        }
                    )  {
                        
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting3(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    RcAppTheme {
        Greeting3("Android")
    }
}