package com.inventoryapp.rcapp.ui.agentnav

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
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
import com.inventoryapp.rcapp.Greeting3
import com.inventoryapp.rcapp.R
import com.inventoryapp.rcapp.data.model.AgentProduct
import com.inventoryapp.rcapp.data.model.InternalProduct
import com.inventoryapp.rcapp.ui.agentnav.viewmodel.internalProducts
import com.inventoryapp.rcapp.ui.theme.RcAppTheme
import com.inventoryapp.rcapp.ui.theme.spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeAgentScreen(){
    ConstraintLayout (
        modifier = Modifier.fillMaxSize()
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
                containerColor = MaterialTheme.colorScheme.background,
                titleContentColor = MaterialTheme.colorScheme.onSurface,
            ),
            navigationIcon = {
                IconButton(onClick = { /* do something */ }) {
                    Icon(
                        imageVector = Icons.Filled.AccountCircle,
                        contentDescription = "Localized description",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            },
            actions = {
                BadgedBox(badge = { Badge { Text("8") } }) {
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
            ElevatedCard(onClick = { /*TODO*/ },
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 3.dp
                )
            ) {
                IconButton(onClick = { /*TODO*/ }) {
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
                IconButton(onClick = { /*TODO*/ }) {
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
                IconButton(onClick = { /*TODO*/ }) {
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
                IconButton(onClick = { /*TODO*/ }) {
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
                IconButton(onClick = { /*TODO*/ }) {
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
            .padding(horizontal = 10.dp, vertical = 10.dp)) {
            items(internalProducts) { item ->
                ListItem(item) // Replace with your composable for each item
            }
        }
    }
}

data class ListItem(val title: String, val description: String, val stock:Int)

@Composable
fun ListItem(item: InternalProduct) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp, horizontal = 10.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 3.dp
        ),
        colors = CardColors(contentColor = MaterialTheme.colorScheme.onSurface, containerColor = MaterialTheme.colorScheme.surfaceContainerLow, disabledContentColor = MaterialTheme.colorScheme.onSurface, disabledContainerColor = MaterialTheme.colorScheme.onTertiaryContainer)
    ) {
        ConstraintLayout (modifier = Modifier.fillMaxWidth()) {
            val spacing = MaterialTheme.spacing
            val (refIcon, refTitle, refDate, refStock) = createRefs()
            Icon(modifier = Modifier
                .constrainAs(refIcon){
                    top.linkTo(parent.top, spacing.medium)
                    start.linkTo(parent.start, spacing.medium)
                    bottom.linkTo(parent.bottom, spacing.medium)
            },
                imageVector = ImageVector.vectorResource(id = R.drawable.bag_icon), contentDescription = "ini icon", tint = MaterialTheme.colorScheme.primary)
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
                text = item.updateAt.toString(),
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

val sampleData = listOf(
    ListItem("afghanistan", "Diperbarui 23 Des 2023", 100),
    ListItem("bengkulu", "Diperbarui 23 Des 2023",100),
    ListItem("chsda", "Diperbarui 23 Des 2023", 100),
    ListItem("dewdea", "Diperbarui 23 Des 2023",100),
    ListItem("ewefwfe", "Diperbarui 23 Des 2023", 100),
    ListItem("fefeea", "Diperbarui 23 Des 2023",100),
    ListItem("gaenaf", "Diperbarui 23 Des 2023", 100),
    ListItem("hadnqwi", "Diperbarui 23 Des 2023",100),
    ListItem("iwqjndw", "Diperbarui 23 Des 2023", 100),
    ListItem("jewkjwenf", "Diperbarui 23 Des 2023",100),
    ListItem("kdlqwkld", "Diperbarui 23 Des 2023", 100),
    ListItem("lwdqjnwqj", "Diperbarui 23 Des 2023",100)
    // ... more items
)

@Preview(apiLevel = 33, showBackground = true)
@Composable
fun HomeAgentPreview(){
    RcAppTheme {
        HomeAgentScreen()
    }
}


@Preview(showBackground = true, apiLevel = 33)
@Composable
fun GreetingPreview() {
    RcAppTheme {
        Greeting3("Android")
    }
}