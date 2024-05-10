package com.inventoryapp.rcapp.ui.internalnav

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.inventoryapp.rcapp.R
import com.inventoryapp.rcapp.data.model.AgentUser
import com.inventoryapp.rcapp.data.model.VerifAccountStatus
import com.inventoryapp.rcapp.ui.viewmodel.AgentUserViewModel
import com.inventoryapp.rcapp.util.Resource
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "StateFlowValueCalledInComposition")
@Composable
fun AgentVerificationScreen(
    agentUserViewModel: AgentUserViewModel?
){
    val query by agentUserViewModel!!.searchTextAgent.collectAsState()
    val onQueryChange by agentUserViewModel!!.isSearchingAgent.collectAsState()
    val agentUserList by agentUserViewModel!!.agentUsers.observeAsState()

    val agentSearchList by agentUserViewModel!!.agentUsersList.collectAsState()

    val sheetState = rememberModalBottomSheetState()
    var showDetailAgent by remember { mutableStateOf(false) }
    val selectedOrderStateFlow = MutableStateFlow<AgentUser?>(null)
    val context = LocalContext.current

    val refreshScope = rememberCoroutineScope()
    var refreshing by remember { mutableStateOf(false) }
    fun refresh() = refreshScope.launch {
        refreshing = true
        delay(1500)
        agentUserViewModel?.fetchUsers()
        refreshing = false
    }
    val state = rememberPullRefreshState(refreshing, ::refresh)

    Scaffold (
        modifier = Modifier.padding(top = 60.dp),
        containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
    ){
        Column {
            SearchBar(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 10.dp),
                query = query,
                onQueryChange = agentUserViewModel!!::onSearchTextChange,
                onSearch = agentUserViewModel::onSearchTextChange,
                active = onQueryChange,
                onActiveChange = { agentUserViewModel.onToogleSearch()},
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
                    items(agentSearchList) { user ->
                        CardAgentVerification(
                            agentUser = user,
                            onCardClick = { _ ->
                                showDetailAgent = true
                            },
                            onCardData = { data ->
                                selectedOrderStateFlow.value = data
                            }
                        )
                    }
                }
            }
            LaunchedEffect(Unit) {
                agentUserViewModel.fetchUsers()
            }
            when (agentUserList) {
                is Resource.Success -> {
                    val userList = (agentUserList as Resource.Success<List<AgentUser>>).result
                    Box(Modifier
                        .pullRefresh(state)
                        .padding(top=8.dp, bottom = 80.dp)
                    )
                    {
                        LazyColumn(Modifier.fillMaxSize()) {
                            if (!refreshing) {
                                items(userList){ user ->
                                    CardAgentVerification(
                                        agentUser = user,
                                        onCardClick = { _ ->
                                            showDetailAgent = true
                                        },
                                        onCardData = { data ->
                                            selectedOrderStateFlow.value = data
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
                    CircularProgressIndicator()
                }
                is Resource.Failure -> {
                    // Tampilkan pesan error jika diperlukan
                    val error = (agentUserList as Resource.Failure).throwable
                    Text(text = "Error: ${error.message}")
                }
                else -> {
                    // Tampilkan pesan default jika diperlukan
                    Text(text = "No data available")
                }
            }
        }
        if (showDetailAgent){
            ModalBottomSheet(
                onDismissRequest = {
                    showDetailAgent=false
                    agentUserViewModel?.fetchUsers()
                },
                sheetState = sheetState,
                containerColor = MaterialTheme.colorScheme.surfaceContainerLowest
            ){
                ModalSheetDetailAgent(
                    agentUser = selectedOrderStateFlow.value!!,
                    agentUserViewModel
                )
            }
        }
    }
}

@Composable
fun ModalSheetDetailAgent(
    agentUser: AgentUser,
    agentUserViewModel: AgentUserViewModel?
){
    val modelResource = agentUserViewModel?.updateStatusAgentFlow?.collectAsState()
    val iconStatus = when (agentUser.verificationStatus){
        VerifAccountStatus.APPROVED -> ImageVector.vectorResource(R.drawable.icon_verified)
        VerifAccountStatus.PENDING -> ImageVector.vectorResource(R.drawable.icon_pending)
        null -> ImageVector.vectorResource(R.drawable.icon_pending)
    }

    val color = when (agentUser.verificationStatus) {
        VerifAccountStatus.APPROVED -> Color.Green
        VerifAccountStatus.PENDING -> MaterialTheme.colorScheme.error
        null -> {MaterialTheme.colorScheme.error}
    }
    var checked by remember {
        mutableStateOf(
            agentUser.verificationStatus == VerifAccountStatus.APPROVED
        )
    }
//    checked = if (agentUser.verificationStatus == VerifAccountStatus.APPROVED ){
//        true
//    } else false

    when (checked){
        true -> agentUserViewModel?.updateStatusAgent(
            agentUser.idAgent!!,
            VerifAccountStatus.APPROVED
        )
        false -> agentUserViewModel?.updateStatusAgent(
            agentUser.idAgent!!,
            VerifAccountStatus.PENDING
        )
    }
//    checked = if (agentUser.verificationStatus == VerifAccountStatus.APPROVED ){
//        true
//    } else false
    val context = LocalContext.current
    Column (
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(
            text = "Aktivasi Agen",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Medium
            )
        )
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp, vertical = 25.dp),
            colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surfaceContainerLowest),
            elevation = CardDefaults.cardElevation(5.dp)
        ){
            Column (
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 20.dp)
            ){
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            horizontal = 10.dp
                        ),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Image(
                        imageVector = ImageVector.vectorResource(id = R.drawable.profile_circle),
                        contentDescription = "ini agent"
                    )
                    Text(
                        modifier = Modifier.padding(start = 10.dp),
                        text = agentUser.name!!,
                        style = MaterialTheme.typography.titleMedium.copy(
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = 10.dp,
                                vertical = 10.dp
                            ),
                        horizontalArrangement = Arrangement.End,
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Image(
                            imageVector = iconStatus,
                            contentDescription = "icon status")
                        Text(
                            modifier = Modifier.padding(start = 5.dp),
                            text = agentUser.verificationStatus.toString(),
                            color = color
                        )
                    }
                }
                Text(
                    modifier = Modifier.padding(start = 10.dp),
                    text = agentUser.email!!,
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Medium
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    modifier = Modifier.padding(start = 10.dp),
                    text = agentUser.phoneNumber!!,
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Medium
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    modifier = Modifier.padding(start = 10.dp),
                    text = agentUser.address!!,
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.Medium
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
        Row (
            modifier = Modifier.padding(bottom = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(30.dp)
        ){
            Text(
                text = "Atur status akun",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Medium
                )
            )
            Switch(
                checked = checked,
                onCheckedChange = {
                    checked = it
                },
                thumbContent = if (checked) {
                    {
                        Icon(
                            imageVector = Icons.Filled.Check,
                            contentDescription = null,
                            modifier = Modifier.size(SwitchDefaults.IconSize),
                        )
                    }
                } else {
                    null
                }
            )
        }
        modelResource?.value.let {
            when (it) {
                is Resource.Failure -> {
                    Toast.makeText(context, it.throwable.message, Toast.LENGTH_SHORT).show()
                }
                is Resource.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                }
                is Resource.Success -> {}
                else -> {}
            }
        }
    }
}

@Composable
fun CardAgentVerification(
    agentUser: AgentUser,
    onCardClick: (String) -> Unit,
    onCardData: (AgentUser) -> Unit,
){
    val color = when (agentUser.verificationStatus) {
        VerifAccountStatus.APPROVED -> Color.Green
        VerifAccountStatus.PENDING -> MaterialTheme.colorScheme.error
        else -> {MaterialTheme.colorScheme.error}
    }
    val iconStatus = when (agentUser.verificationStatus){
        VerifAccountStatus.APPROVED -> ImageVector.vectorResource(R.drawable.icon_verified)
        VerifAccountStatus.PENDING -> ImageVector.vectorResource(R.drawable.icon_pending)
        else -> {ImageVector.vectorResource(R.drawable.icon_pending)}
    }

    val text = when (agentUser.verificationStatus){
        VerifAccountStatus.APPROVED -> "terverifikasi"
        VerifAccountStatus.PENDING -> "pending"
        else -> {"pending"}

    }
    Card (
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 15.dp, vertical = 5.dp
            )
            .clickable {
                onCardClick(agentUser.idAgent!!)
                onCardData(agentUser)
            },
        elevation = CardDefaults.cardElevation(3.dp),
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
                text = agentUser.name!!,
                style = MaterialTheme.typography.labelLarge.copy(
                    color = MaterialTheme.colorScheme.onSurface,
                    )
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ){
                Image(imageVector = iconStatus, contentDescription = "pending")
                Text(
                    modifier = Modifier.padding(start = 5.dp),
                    text = text,
                    color = color
                )
            }
        }
    }
}

@Preview(apiLevel = 33)
@Composable
fun PrevCardAgentVerification(){
//    CardAgentVerification(AgentUser("ada","Figo Alsistani","dqwdq","dqwd","hibrida",VerifAccountStatus.APPROVED,
//        Date()
//    ),onCardClick = {user -> }
//    )
}

@Preview(apiLevel = 33)
@Composable
fun PrevAgentVerification(){
//    AgentVerificationScreen()
}