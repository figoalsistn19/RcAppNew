package com.inventoryapp.rcapp.ui.internalnav

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.inventoryapp.rcapp.data.model.SalesOrder
import com.inventoryapp.rcapp.ui.agentnav.CardOrderHistoryForInternal
import com.inventoryapp.rcapp.ui.agentnav.InvoiceScreenForInternal
import com.inventoryapp.rcapp.ui.viewmodel.SalesOrderViewModel
import com.inventoryapp.rcapp.util.Resource
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "StateFlowValueCalledInComposition",
    "SuspiciousIndentation"
)
@Composable
fun InternalSalesScreen(
    salesOrderViewModel: SalesOrderViewModel?,
){
//    val firestore = FirebaseFirestore.getInstance()
    val searchText by salesOrderViewModel!!.searchText.collectAsState()
    val isSearching by salesOrderViewModel!!.isSearching.collectAsState()
    val salesOrderList by salesOrderViewModel!!.salesOrderInternalList.collectAsState()

    val salesOrder by salesOrderViewModel?.salesOrderInternal!!.observeAsState()
    val selectedOrderStateFlow = MutableStateFlow<SalesOrder?>(null)

    val modelResource = salesOrderViewModel?.deleteSalesOrderFlow?.collectAsState()
    val userRole = salesOrderViewModel!!.userRole.observeAsState()
//    val userRoleRef = firestore.collection(FireStoreCollection.INTERNALUSER).document(
//        salesOrderViewModel?.currentUser?.uid!!
//    )
//        .get()
//        .addOnSuccessListener {
//            userRole = it.getString("userRole")!!
//        }

    val sheetState = rememberModalBottomSheetState()
    var showDetailOrder by remember { mutableStateOf(false) }
    val selectedCard = remember { mutableStateOf("") }

    val refreshScope = rememberCoroutineScope()
    var refreshing by remember { mutableStateOf(false) }
    fun refresh() = refreshScope.launch {
        refreshing = true
        delay(1500)
        salesOrderViewModel.fetchSalesOrderInternal()
        refreshing = false
    }
    val state = rememberPullRefreshState(refreshing, ::refresh)

    val context = LocalContext.current

    val openAlertDialog = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        salesOrderViewModel.getUserRole()
    }
    Scaffold(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
            topBar = {
                Column (
                    modifier = Modifier.padding(top=55.dp)
                ){
                    SearchBar(
                        query = searchText,//text showed on SearchBar
                        onQueryChange = salesOrderViewModel::onSearchTextChange, //update the value of searchText
                        onSearch = salesOrderViewModel::onSearchTextChange, //the callback to be invoked when the input service triggers the ImeAction.Search action
                        active = isSearching, //whether the user is searching or not
                        onActiveChange = { salesOrderViewModel.onToogleSearch() }, //the callback to be invoked when this search bar's active state is changed
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 23.dp, top = 8.dp, end = 23.dp),
                        trailingIcon = {
                            Icon(imageVector = Icons.Rounded.Search, contentDescription = "cari" )
                        },
                        placeholder = {
                            Text(text = "Cari pesanan disini...")
                        }
                    ) {
                        LazyColumn (modifier = Modifier.padding(horizontal = 8.dp, vertical =10.dp)){
                            items(salesOrderList) { item ->
                                CardOrderHistoryForInternal(
                                    order = item,
                                    onCardClick = {
                                        selectedCard.value = it.idOrder!!
                                        selectedOrderStateFlow.value = it
                                        showDetailOrder = true
                                    },
                                    onCardData = {
                                        selectedOrderStateFlow.value = it
                                        if (userRole.value == "SalesManager"||userRole.value == "Admin"){
                                            openAlertDialog.value = true
                                        }
                                    },
                                    onClickHold = {
                                        selectedCard.value
//                                        openAlertDialog.value = true
                                    }
                                )
                            }
                        }
                    }
                    LaunchedEffect(Unit) {
                        salesOrderViewModel.fetchSalesOrderInternal()
                    }
                    when (salesOrder) {
                        is Resource.Success -> {
                            val salesOrders = (salesOrder as Resource.Success<List<SalesOrder>>).result
                            Box(
                                Modifier
                                    .pullRefresh(state)
                                    .padding(top = 8.dp, bottom = 80.dp)
                            )
                            {
                                if (salesOrders.isEmpty()){
                                    Text(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(start = 8.dp, end = 8.dp, top =25.dp),
                                        text = "Data masih kosong")
                                }
                                else {
                                    LazyColumn (modifier = Modifier.padding(start = 8.dp, end = 8.dp, top =25.dp)){
                                        items(salesOrders) { item ->
                                            CardOrderHistoryForInternal(
                                                order = item,
                                                onCardClick = {
                                                    selectedCard.value = it.idOrder!!
                                                    selectedOrderStateFlow.value = it
                                                    showDetailOrder = true
                                                },
                                                onCardData = {
                                                    selectedOrderStateFlow.value = it
                                                    if (userRole.value == "SalesManager"||userRole.value == "Admin"){
                                                        openAlertDialog.value = true
                                                    }
                                                },
                                                onClickHold = {
                                                    selectedCard.value = it
//                                                    openAlertDialog.value = true
                                                }
                                            )
                                        }
                                    }
                                }
                                PullRefreshIndicator(refreshing, state, Modifier.align(Alignment.TopCenter))
                            }

                        }
                        is Resource.Loading -> {
                            // Tampilkan indikator loading jika diperlukan
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .padding(start = 8.dp, end = 8.dp, top =25.dp)
                            )
                        }
                        is Resource.Failure -> {
                            // Tampilkan pesan error jika diperlukan
                            val error = (salesOrder as Resource.Failure).throwable
                            Text(text = "Error: ${error.message}")
                        }
                        else -> {
                            // Tampilkan pesan default jika diperlukan
                            Text(text = "No data available")
                        }
                    }
                }
            }
        ) {
            when {
                // ...
                openAlertDialog.value -> {
                    AlertDialogExample(
                        onDismissRequest = {
                            openAlertDialog.value = false
                            salesOrderViewModel.fetchSalesOrderInternal()
                        },
                        onConfirmation = {
                            openAlertDialog.value = false
                            salesOrderViewModel.deleteSalesOrder(selectedOrderStateFlow.value?.idOrder!!)
                            salesOrderViewModel.fetchSalesOrderInternal()
                            println("Confirmation registered") // Add logic here to handle confirmation.
                        },
                        dialogTitle = "Yakin untuk hapus pesanan ?",
                        dialogText = "Jika memilih confirm maka pesanan akan di hapus",
                        icon = Icons.Default.Info
                    )
                }
            }
            if (showDetailOrder){
                ModalBottomSheet(
                    onDismissRequest = {
                        showDetailOrder=false
                        salesOrderViewModel.fetchSalesOrderInternal()
                    },
                    sheetState = sheetState
                ){
                    InvoiceScreenForInternal(
                        selectedOrderStateFlow.value!!,
                        salesOrderViewModel
                    ) {
                        showDetailOrder = false
                        salesOrderViewModel.fetchSalesOrderInternal()
                    }
                }
            }
        modelResource?.value?.let {
            when (it) {
                is Resource.Failure -> {
                    Toast.makeText(context, it.throwable.message, Toast.LENGTH_SHORT).show()
                }
                is Resource.Loading -> {
                    CircularProgressIndicator()
                }
                is Resource.Success -> {
                }
            }
        }

        }
}

@Composable
fun AlertDialogExample(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    icon: ImageVector,
) {
    AlertDialog(
        icon = {
            Icon(icon, contentDescription = "Example Icon")
        },
        title = {
            Text(text = dialogTitle)
        },
        text = {
            Text(text = dialogText)
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                }
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text("Dismiss")
            }
        }
    )
}