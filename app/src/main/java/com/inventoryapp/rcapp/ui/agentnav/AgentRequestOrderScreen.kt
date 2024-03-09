package com.inventoryapp.rcapp.ui.agentnav

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.inventoryapp.rcapp.R
import com.inventoryapp.rcapp.ui.agentnav.viewmodel.AgentProductViewModel
import com.inventoryapp.rcapp.ui.theme.spacing

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AgentRequestOrderScreen(){
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    var filterBySales by remember { mutableStateOf(true) }
    var filterBySystem by remember { mutableStateOf(false) }
    val agentProductViewModel = AgentProductViewModel()
    val agentProductList by agentProductViewModel.agentProductList.collectAsState()
    Scaffold (
        topBar = {
            Column (
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
                ){
                CenterAlignedTopAppBar(
                    title = {
                        Text(text = "Request Order",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Medium))
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        titleContentColor = MaterialTheme.colorScheme.onSurface,
                    ),
                    navigationIcon = {},
                    actions = {},
                    scrollBehavior = scrollBehavior)
                Row (
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ){
                    FilterChip(
                        onClick = {
                            filterBySales = !filterBySales
                            filterBySystem = !filterBySystem
                                  },
                        label = {
                            Text("By Sales", color = Color.White)
                        },
                        selected = filterBySales,
                        leadingIcon = if (filterBySales) {
                            {
                                Icon(
                                    imageVector = ImageVector.vectorResource(id = R.drawable.by_sales),
                                    contentDescription = "Done icon",
                                    modifier = Modifier.size(FilterChipDefaults.IconSize),
                                    tint = MaterialTheme.colorScheme.background
                                )
                            }
                        } else {
                            null
                        }
                    )
                    FilterChip(
                        onClick = {
                            filterBySystem = !filterBySystem
                            filterBySales = !filterBySales
                                  },
                        label = {
                            Text("By System", color = Color.White)
                        },
                        selected = filterBySystem,
                        leadingIcon = if (filterBySystem) {
                            {
                                Icon(
                                    imageVector = ImageVector.vectorResource(id = R.drawable.by_system),
                                    contentDescription = "Done icon",
                                    modifier = Modifier.size(FilterChipDefaults.IconSize),
                                    tint = MaterialTheme.colorScheme.background
                                )
                            }
                        } else {
                            null
                        }
                    )
                }
                if (filterBySystem){
                    LazyColumn (modifier = Modifier.padding(start = 8.dp, end = 8.dp, top =25.dp)){
                        items(agentProductList) { item ->
                            ListItem(item = item)
                        }
                    }
                } else {
                    LazyColumn (modifier = Modifier.padding(start = 8.dp, end = 8.dp, top =25.dp)){
                        items(30) { item ->
                            CardReqOrder()
                        }
                    }
                }
            }


        }
    ){
        Text(text = "dsqd")
    }
}

@Composable
fun CardReqOrder(){
    ElevatedCard (modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 8.dp, vertical = 6.dp)) {
        ConstraintLayout (modifier = Modifier.height(70.dp)){
            val (refIcon, refIdReqOrder, refDate, refPrice, refStatusOrder)= createRefs()
            val spacing = MaterialTheme.spacing
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.price_tag),
                contentDescription = "icon harga",
                modifier = Modifier.constrainAs(refIcon){
                    top.linkTo(parent.top, spacing.medium)
                    start.linkTo(parent.start, spacing.extraSmall)
                    bottom.linkTo(parent.bottom, spacing.medium)
                },
                tint = MaterialTheme.colorScheme.primary)
            Text(
                text = "IDX-A06-003",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.constrainAs(refIdReqOrder){
                    top.linkTo(parent.top, spacing.medium)
                    start.linkTo(refIcon.end, spacing.extraSmall)
                }
                )
            Text(
                text = "21 Des 2023",
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Light),
                modifier = Modifier.constrainAs(refDate){
                    top.linkTo(refIdReqOrder.bottom)
                    start.linkTo(refIcon.end, spacing.extraSmall)
                }
            )
            Spacer(modifier = Modifier.fillMaxWidth())
            Column (
                modifier = Modifier
                    .constrainAs(refPrice){
                        end.linkTo(parent.end, spacing.small)
                        top.linkTo(parent.top, spacing.medium)
            },
                horizontalAlignment = Alignment.End
            ){
                Text(
                    text = "Rp.500.000",
                    style = MaterialTheme.typography.titleMedium)
                Text(
                    text = "pending",
                    style = MaterialTheme.typography.titleSmall,
                    color = Color.Magenta
                )
            }


//            Text(
//                text = "Rp.500.000",
//                modifier = Modifier.constrainAs(refPrice){
//                    top.linkTo(parent.top)
//                    end.linkTo(parent.end)
//                })
        }
    }

}

@Preview(apiLevel = 33)
@Composable
fun PreviewRequestOrder(){
    AgentRequestOrderScreen()
}