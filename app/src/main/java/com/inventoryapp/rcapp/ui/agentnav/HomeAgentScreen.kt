package com.inventoryapp.rcapp.ui.agentnav

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
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
import com.inventoryapp.rcapp.R
import com.inventoryapp.rcapp.data.model.InternalProduct
import com.inventoryapp.rcapp.ui.agentnav.viewmodel.AgentProductViewModel
import com.inventoryapp.rcapp.ui.agentnav.viewmodel.internalProducts
import com.inventoryapp.rcapp.ui.auth.agentauth.AuthAgentViewModel
import com.inventoryapp.rcapp.ui.nav.ROUTE_AGENT_REQUEST_ORDER_SCREEN
import com.inventoryapp.rcapp.ui.nav.ROUTE_AGENT_STOCK_SCREEN
import com.inventoryapp.rcapp.ui.nav.ROUTE_HOME
import com.inventoryapp.rcapp.ui.nav.ROUTE_ORDER_HISTORY_SCREEN
import com.inventoryapp.rcapp.ui.nav.ROUTE_STOCK_IN_SCREEN
import com.inventoryapp.rcapp.ui.nav.ROUTE_STOCK_OUT_SCREEN
import com.inventoryapp.rcapp.ui.theme.RcAppTheme
import com.inventoryapp.rcapp.ui.theme.spacing
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeAgentScreen(viewModel: AuthAgentViewModel?,navController: NavController){
    val state = remember { ScrollState(0) }
    val agentProductViewModel = AgentProductViewModel()
    val agentProductList by agentProductViewModel.agentProductList.collectAsState()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = MaterialTheme.colorScheme.surfaceContainerLowest
            ) {
                Column {
                    Text("Drawer title", modifier = Modifier.padding(16.dp))
                    Divider()
                    NavigationDrawerItem(
                        label = { Text(text = "Drawer Item") },
                        selected = false,
                        onClick = { /*TODO*/ }
                    )
                    IconButton(onClick = {
                        viewModel!!.logout()
                        navController.navigate(ROUTE_HOME)
                    }) {
                        Icon(imageVector = Icons.Default.Home, contentDescription = "logout")
                    }
                }

            }
        },
    )
    {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
            content = {
                Column (modifier = Modifier
                    .verticalScroll(state)
                    .size(1200.dp)
                    .padding(bottom = 80.dp)
                ) {
                    ConstraintLayout (
//                        modifier = Modifier
//                            .size(1200.dp)

                    ) {
                        val (refTopAppBar, refCardPromotion, refMenu, refMenuDetail, refMenuTitle, refProduct, refProductList) = createRefs()
                        val spacing = MaterialTheme.spacing
                        val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

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
                            .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ){
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
                                    Icon(imageVector = ImageVector.vectorResource(id = R.drawable.riwayat_belanja),
                                        contentDescription = "ini icon",
                                        tint = MaterialTheme.colorScheme.primary)
                                }
                            }
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
                            ElevatedCard(onClick = { /*TODO*/ },
                                elevation = CardDefaults.cardElevation(
                                    defaultElevation = 3.dp
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
                            ElevatedCard(onClick = { /*TODO*/ },
                                elevation = CardDefaults.cardElevation(
                                    defaultElevation = 3.dp
                                )
                            ) {
                                IconButton(onClick = {
                                    navController.navigate(ROUTE_AGENT_REQUEST_ORDER_SCREEN)
                                }) {
                                    Icon(imageVector = ImageVector.vectorResource(id = R.drawable.request_order),
                                        contentDescription = "ini icon",
                                        tint = MaterialTheme.colorScheme.primary)
                                }
                            }
                        }
                        Row (modifier = Modifier
                            .constrainAs(refMenuTitle) {
                                top.linkTo(refMenuDetail.bottom, spacing.small)
                                start.linkTo(parent.start, spacing.medium)
                                end.linkTo(parent.end, spacing.medium)
                            }
                            .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ){
                            Text(text = "Riwayat Belanja", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Light, fontSize = 10.sp))
                            Text(text = "Stok Barang", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Light, fontSize = 10.sp))
                            Text(text = "Barang Masuk", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Light, fontSize = 10.sp))
                            Text(text = "Barang Keluar", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Light, fontSize = 10.sp))
                            Text(text = "Request Order", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Light, fontSize = 10.sp))

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
                            .padding(start = 10.dp, end = 10.dp, bottom = 80.dp)
                        ) {
                            items(internalProducts) { item ->
                                ListProduct(
                                    item,
                                    onCardClicked = {

                                    }
                                ) // Replace with your composable for each item
                            }
                        }
                    }
                }

            }
        )
    }

}


@SuppressLint("SimpleDateFormat")
@Composable
fun ListProduct(
    item: InternalProduct,
    onCardClicked: (String) -> Unit
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp, horizontal = 10.dp)
            .clickable { onCardClicked(item.idProduct) }
        ,
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        ),
        colors = CardColors(contentColor = MaterialTheme.colorScheme.onSurface, containerColor = MaterialTheme.colorScheme.surfaceContainerLowest, disabledContentColor = MaterialTheme.colorScheme.onSurface, disabledContainerColor = MaterialTheme.colorScheme.onTertiaryContainer)
    ) {
        ConstraintLayout (modifier = Modifier.fillMaxWidth()) {
            val sdf = SimpleDateFormat("dd MMM yyyy ・ HH:mm")
            val date = item.updateAt
            val fixDate = sdf.format(date)
            val spacing = MaterialTheme.spacing
            val (refIcon, refTitle, refDate, refStock) = createRefs()
            Image(modifier = Modifier
                .constrainAs(refIcon){
                    top.linkTo(parent.top, spacing.medium)
                    start.linkTo(parent.start, spacing.medium)
                    bottom.linkTo(parent.bottom, spacing.medium)
            },
                imageVector = ImageVector.vectorResource(id = R.drawable.bag_icon),
                contentDescription = "ini icon",
               )
            Text(modifier = Modifier
                .constrainAs(refTitle){
                    top.linkTo(parent.top)
                    start.linkTo(refIcon.end, spacing.small)
                    bottom.linkTo(parent.bottom, spacing.medium)
                },
                text = item.productName, style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Medium))
            Text(modifier = Modifier
                .constrainAs(refDate){
                    top.linkTo(refTitle.bottom)
                    start.linkTo(refIcon.end, spacing.small)
                    bottom.linkTo(parent.bottom, spacing.medium)
                },
                text = fixDate,
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Light))
            Text(modifier = Modifier
                .constrainAs(refStock){
                    top.linkTo(parent.top)
                    end.linkTo(parent.end, spacing.small)
                    bottom.linkTo(parent.bottom)
                },
                text = item.qtyProduct.toString()+ " pcs",
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium))
        }

    }
}

@Preview(apiLevel = 33, showBackground = true)
@Composable
fun HomeAgentPreview(){
    RcAppTheme {
        ListProduct(item = internalProducts[1],
            onCardClicked = {})
    }
}


@Preview(showBackground = true, apiLevel = 33)
@Composable
fun GreetingPreview() {
    RcAppTheme {
    }
}