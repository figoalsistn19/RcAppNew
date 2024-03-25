package com.inventoryapp.rcapp.ui.internalnav

import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.inventoryapp.rcapp.R
import com.inventoryapp.rcapp.ui.agentnav.ListItem
import com.inventoryapp.rcapp.ui.agentnav.viewmodel.AgentProductViewModel
import com.inventoryapp.rcapp.ui.agentnav.viewmodel.internalProducts
import com.inventoryapp.rcapp.ui.nav.BottomNavAgentViewModel
import com.inventoryapp.rcapp.ui.nav.ROUTE_AGENT_STOCK_SCREEN
import com.inventoryapp.rcapp.ui.nav.ROUTE_ORDER_HISTORY_SCREEN
import com.inventoryapp.rcapp.ui.nav.ROUTE_STOCK_IN_SCREEN
import com.inventoryapp.rcapp.ui.nav.ROUTE_STOCK_OUT_SCREEN
import com.inventoryapp.rcapp.ui.theme.spacing
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InternalHomeScreen(
    navController: NavHostController,
){
    val state = remember { ScrollState(0) }
    val agentProductViewModel = AgentProductViewModel()
    val agentProductList by agentProductViewModel.agentProductList.collectAsState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    Column (modifier = Modifier
        .verticalScroll(state)
        .padding(bottom = 100.dp)) {
        ConstraintLayout (
            modifier = Modifier
                .size(1200.dp)
                .padding(bottom = 20.dp)
        ) {
            val (refTopAppBar, refCardPromotion, refMenu, refMenuDetail, refMenuTitle, refMenu1Detail,refMenu1Title, refProduct, refProductList) = createRefs()
            val spacing = MaterialTheme.spacing
            val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(
                rememberTopAppBarState()
            )

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
                    BadgedBox(badge = { Badge { Text(agentProductList.size.toString()) } }) {
                        Icon(
                            Icons.Filled.Notifications,
                            contentDescription = "Home"
                        )
                    } },
                scrollBehavior = scrollBehavior,
                modifier = Modifier
                    .constrainAs(refTopAppBar) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        width = Dimension.fillToConstraints
                    }
            )
            Card(modifier = Modifier
                .constrainAs(refCardPromotion) {
                    top.linkTo(refTopAppBar.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                }
                .padding(
                    12.dp, 12.dp
                )){
                Image(modifier = Modifier.fillMaxWidth()
                    , painter = painterResource(id = R.drawable.rc_logo) , contentDescription ="description" )
            }
            Text(text = "Menu",
                modifier = Modifier.constrainAs(refMenu){
                    top.linkTo(refCardPromotion.bottom, spacing.medium)
                    start.linkTo(parent.start,spacing.large)
                },
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Medium)
            )
            Row (modifier = Modifier
                .constrainAs(refMenuDetail) {
                    top.linkTo(refMenu.bottom, spacing.small)
                    start.linkTo(parent.start, spacing.medium)
                    end.linkTo(parent.end, spacing.medium)
                }
                .fillMaxWidth()
                .padding(horizontal = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ){
                Column (
                    modifier = Modifier.width(80.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    ElevatedCard(
                        onClick = {
                            navController.navigate(ROUTE_ORDER_HISTORY_SCREEN)
                        },
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 3.dp
                        )
                    ) {
                        IconButton(onClick = {
                            navController.navigate(ROUTE_ORDER_HISTORY_SCREEN)
                        }) {
                            Icon(imageVector = ImageVector.vectorResource(id = R.drawable.unselected_request_order),
                                contentDescription = "ini icon",
                                tint = MaterialTheme.colorScheme.primary)
                        }
                    }
                    Text(
                        modifier = Modifier.padding(top = 5.dp),
                        text = "Penjualan",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Light,
                            fontSize = 10.sp))
                }
                Column (
                    modifier = Modifier.width(80.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    ElevatedCard(onClick = { /*TODO*/ },
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 3.dp
                        )
                    ) {
                        IconButton(onClick = {
                            navController.navigate(ROUTE_AGENT_STOCK_SCREEN)
                        }) {
                            Icon(imageVector = ImageVector.vectorResource(id = R.drawable.stok_barang),
                                contentDescription = "ini icon",
                                tint = MaterialTheme.colorScheme.primary)
                        }
                    }
                    Text(
                        modifier = Modifier.padding(top = 5.dp),
                        text = "Stok Barang",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Light,
                            fontSize = 10.sp))
                }
                Column (
                    modifier = Modifier.width(80.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    ElevatedCard(onClick = { /*TODO*/ },
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 5.dp
                        )
                    ) {
                        IconButton(onClick = {
                            navController.navigate(ROUTE_STOCK_IN_SCREEN)
                        }) {
                            Icon(imageVector = ImageVector.vectorResource(id = R.drawable.barang_masuk),
                                contentDescription = "ini icon",
                                tint = MaterialTheme.colorScheme.primary)
                        }
                    }
                    Text(
                        modifier = Modifier.padding(top = 5.dp),
                        text = "Barang Masuk",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Light,
                            fontSize = 10.sp))
                }
                Column (
                    modifier = Modifier.width(80.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    ElevatedCard(onClick = { /*TODO*/ },
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 3.dp
                        )
                    ) {
                        IconButton(onClick = {
                            navController.navigate(ROUTE_STOCK_OUT_SCREEN)
                        }) {
                            Icon(imageVector = ImageVector.vectorResource(id = R.drawable.barang_keluar),
                                contentDescription = "ini icon",
                                tint = MaterialTheme.colorScheme.primary)
                        }
                    }
                    Text(
                        modifier = Modifier.padding(top = 5.dp),
                        text = "Barang Keluar",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Light,
                            fontSize = 10.sp))
                }

            }
            Row (modifier = Modifier
                .constrainAs(refMenuTitle) {
                    top.linkTo(refMenuDetail.bottom, spacing.small)
                    start.linkTo(parent.start, spacing.medium)
                    end.linkTo(parent.end, spacing.medium)
                }
                .fillMaxWidth()
                .padding(horizontal = 10.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ){
                Column (
                    modifier = Modifier.width(80.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    ElevatedCard(
                        onClick = {
                            navController.navigate(ROUTE_ORDER_HISTORY_SCREEN)
                        },
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 3.dp
                        )
                    ) {
                        IconButton(onClick = {
                            navController.navigate(ROUTE_ORDER_HISTORY_SCREEN)
                        }) {
                            Icon(imageVector = ImageVector.vectorResource(id = R.drawable.stock_agent),
                                contentDescription = "ini icon",
                                tint = MaterialTheme.colorScheme.primary)
                        }
                    }
                    Text(
                        modifier = Modifier.padding(top = 5.dp),
                        text = "Stok Agen",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Light,
                            fontSize = 10.sp))
                }
                Column (
                    modifier = Modifier.width(80.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    ElevatedCard(onClick = { /*TODO*/ },
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 3.dp
                        )
                    ) {
                        IconButton(onClick = {
                            navController.navigate(ROUTE_AGENT_STOCK_SCREEN)
                        }) {
                            Icon(imageVector = ImageVector.vectorResource(id = R.drawable.verifikasi_agent),
                                contentDescription = "ini icon",
                                tint = MaterialTheme.colorScheme.primary)
                        }
                    }
                    Text(
                        modifier = Modifier.padding(top = 5.dp),
                        text = "Verifikasi Agen",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Light, fontSize = 10.sp))
                }
                Column (
                    modifier = Modifier.width(80.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    ElevatedCard(onClick = { /*TODO*/ },
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 3.dp
                        )
                    ) {
                        IconButton(onClick = {
                            navController.navigate(ROUTE_STOCK_IN_SCREEN)
                        }) {
                            Icon(imageVector = ImageVector.vectorResource(id = R.drawable.buat_po_agent),
                                contentDescription = "ini icon",
                                tint = MaterialTheme.colorScheme.primary)
                        }
                    }
                    Text(
                        modifier = Modifier.padding(top = 5.dp),
                        text = "Buat PO Agen",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Light,
                            fontSize = 10.sp))
                }
                Column (
                    modifier = Modifier.width(80.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    ElevatedCard(onClick = { /*TODO*/ },
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 3.dp
                        )
                    ) {
                        IconButton(onClick = {
                            navController.navigate(ROUTE_STOCK_OUT_SCREEN)
                        }) {
                            Icon(imageVector = ImageVector.vectorResource(id = R.drawable.kelola_banner),
                                contentDescription = "ini icon",
                                tint = MaterialTheme.colorScheme.primary)
                        }
                    }
                    Text(
                        modifier = Modifier.padding(top = 5.dp),
                        text = "Kelola Banner",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Light,
                            fontSize = 10.sp))
                }
            }

            Text(text = "Product",
                modifier = Modifier.constrainAs(refProduct){
                    top.linkTo(refMenuTitle.bottom,spacing.medium)
                    start.linkTo(parent.start,spacing.large)
                },
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Medium)
            )
            LazyColumn (modifier = Modifier
                .constrainAs(refProductList) {
                    top.linkTo(refProduct.bottom)
                    start.linkTo(parent.start, spacing.medium)
                    end.linkTo(parent.end, spacing.medium)
                }
                .padding(horizontal = 10.dp, vertical = 1.dp))
            {
                items(internalProducts) { item ->
                    ListItem(item) // Replace with your composable for each item
                }
            }
        }
    }
}

@Preview(apiLevel = 33)
@Composable
fun PrevHomeInternal(){
    InternalHomeScreen(navController = rememberNavController())
}