package com.inventoryapp.rcapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.remember
import com.inventoryapp.rcapp.ui.auth.agentauth.AuthAgentViewModel
import com.inventoryapp.rcapp.ui.nav.MainNavigation
import com.inventoryapp.rcapp.ui.theme.RcAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val authViewModel by viewModels<AuthAgentViewModel>()

    @OptIn(ExperimentalMaterial3Api::class)
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getSupportActionBar()?.hide()
        setContent {
            val snackbarHostState = remember { SnackbarHostState() }
            Scaffold (
                snackbarHost = { SnackbarHost (snackbarHostState) },
                content = {
                    RcAppTheme {
                        MainNavigation(authViewModel)
                    }
                }
            )
        }
    }
}




