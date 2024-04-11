package com.inventoryapp.rcapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.remember
import com.inventoryapp.rcapp.ui.agentnav.viewmodel.AgentProductViewModel
import com.inventoryapp.rcapp.ui.agentnav.viewmodel.AgentTransactionViewModel
import com.inventoryapp.rcapp.ui.auth.agentauth.AuthAgentViewModel
import com.inventoryapp.rcapp.ui.auth.internalauth.AuthInternalViewModel
import com.inventoryapp.rcapp.ui.internalnav.viewmodel.AgentUserViewModel
import com.inventoryapp.rcapp.ui.internalnav.viewmodel.InternalProductViewModel
import com.inventoryapp.rcapp.ui.nav.MainNavigation
import com.inventoryapp.rcapp.ui.theme.RcAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val agentTransactionViewModel by viewModels<AgentTransactionViewModel>()
    private val authAgentViewModel by viewModels<AuthAgentViewModel>()
    private val authInternalViewModel by viewModels<AuthInternalViewModel>()
    private val internalProductViewModel by viewModels<InternalProductViewModel>()
    private val agentUserViewModel by viewModels<AgentUserViewModel>()
    private val agentProductViewModel by viewModels<AgentProductViewModel>()
//    private val internal: Internal
//    private val viewModel by lazy { ViewModelProviders.of(this, InternalProductVMFactory(InternalImp(InternalRepoImp()))).get(InternalProductViewModel::class.java) }

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContent {
            val snackbarHostState = remember { SnackbarHostState() }
            Scaffold (
                snackbarHost = { SnackbarHost (snackbarHostState) },
                content = {
                    RcAppTheme {
                        Surface (color = MaterialTheme.colorScheme.background) {
                            MainNavigation(agentTransactionViewModel, agentProductViewModel,agentUserViewModel,authAgentViewModel,authInternalViewModel,internalProductViewModel)
                        }
                    }
                }
            )
        }
    }
}




