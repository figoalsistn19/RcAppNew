package com.inventoryapp.rcapp.ui.internalnav

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.inventoryapp.rcapp.R
import com.inventoryapp.rcapp.data.model.AgentUser
import com.inventoryapp.rcapp.ui.agentnav.ListItemForInOut
import com.inventoryapp.rcapp.ui.agentnav.viewmodel.InternalProductViewModel
import com.inventoryapp.rcapp.ui.agentnav.viewmodel.internalProducts
import com.inventoryapp.rcapp.ui.internalnav.viewmodel.VerificationAgentViewModel
import com.inventoryapp.rcapp.ui.internalnav.viewmodel.agentUserList

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AgentStockMonitoringScreen(){
    val verifAgentViewModel = VerificationAgentViewModel()
    val query by verifAgentViewModel.searchText.collectAsState()
    val onQueryChange by verifAgentViewModel.isSearching.collectAsState()
    val agentList by verifAgentViewModel.agentUsersList.collectAsState()
    val internalProductViewModel = InternalProductViewModel()
    val queryAgentStock by internalProductViewModel.searchText.collectAsState()
    val onQueryChangeAgentStock by internalProductViewModel.isSearching.collectAsState()
    val productsList by internalProductViewModel.productsList.collectAsState()
    val sheetState = rememberModalBottomSheetState()
    var checked by remember { mutableStateOf(true) }
    var showDetailAgentStock by remember { mutableStateOf(false) }
    Scaffold (
        containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
    ){
        Column {
            TopAppBar(
                title = {
                    Text(text = "Stok Agen",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Medium))
                },
                navigationIcon = {
                    IconButton(onClick = {

                    }) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.back_btn),
                            contentDescription = "tombol kembali",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                )
            )
            SearchBar(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 10.dp),
                query = query,
                onQueryChange = verifAgentViewModel::onSearchTextChange,
                onSearch = verifAgentViewModel::onSearchTextChange,
                active = onQueryChange,
                onActiveChange = { verifAgentViewModel.onToogleSearch()},
                trailingIcon = {
                    Icon(imageVector = Icons.Rounded.Search, contentDescription = "cari" )
                },
                placeholder = {
                    Text(text = "Cari nama agen disini...")
                },
                shadowElevation = 2.dp,
                colors = SearchBarDefaults.colors(MaterialTheme.colorScheme.surfaceContainerLowest)
            ) {
                LazyColumn {
                    items(agentList) { user ->
                        CardAgentVerification(
                            agentUser = user,
                            onCardClick = {user ->
                                showDetailAgentStock = true
                            })
                    }
                }
            }
            LazyColumn(
                modifier = Modifier.padding(top=8.dp, bottom = 80.dp)
            ){
                items(agentUserList){ user ->
                    CardAgentStockMonitoring(
                        agentUser = user,
                        onCardClick = {user ->
                            showDetailAgentStock = true
                        }
                    )
                }
            }
        }
        if (showDetailAgentStock){
            ModalBottomSheet(
                onDismissRequest = {
                    showDetailAgentStock=false
                },
                sheetState = sheetState,
                containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
            ){
                Column (
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Text(
                        text = "Toko Rina",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Medium
                        )
                    )
                    SearchBar(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(bottom = 10.dp),
                        query = query,
                        onQueryChange = internalProductViewModel::onSearchTextChange,
                        onSearch = internalProductViewModel::onSearchTextChange,
                        active = onQueryChangeAgentStock,
                        onActiveChange = { internalProductViewModel.onToogleSearch()},
                        trailingIcon = {
                            Icon(imageVector = Icons.Rounded.Search, contentDescription = "cari" )
                        },
                        placeholder = {
                            Text(text = "Cari barang disini...")
                        },
                        shadowElevation = 2.dp,
                        colors = SearchBarDefaults.colors(MaterialTheme.colorScheme.surfaceContainerLowest)
                    ) {
                        LazyColumn(modifier = Modifier.padding(start = 10.dp, end = 10.dp, top = 10.dp)) {
                            items(productsList) { item ->
                                ListItemForInOut(item = item)
                            }
                        }
                    }
                    LazyColumn (modifier = Modifier.padding(horizontal = 10.dp)){
                        items(internalProducts) { item ->
                            ListItemForInOut(item = item)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CardAgentStockMonitoring(
    agentUser: AgentUser,
    onCardClick: (String) -> Unit
){
    Card (
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 15.dp, vertical = 5.dp
            )
            .clickable {
                onCardClick(agentUser.idAgent)
            },
        elevation = CardDefaults.cardElevation(1.dp),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceContainerLowest)
    ){
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp, vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            Image(
                imageVector = ImageVector.vectorResource(id = R.drawable.profile_circle),
                contentDescription = "ini agent"
            )
            Text(
                modifier = Modifier.padding(start = 10.dp),
                text = agentUser.name,
                style = MaterialTheme.typography.labelLarge.copy(
                    color = MaterialTheme.colorScheme.onSurface,
                )
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ){
                Text(
                    modifier = Modifier.padding(start = 5.dp),
                    text = "100 barang",
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}