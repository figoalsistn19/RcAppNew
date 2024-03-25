package com.inventoryapp.rcapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.inventoryapp.rcapp.ui.auth.agentauth.AuthAgentViewModel
import com.inventoryapp.rcapp.ui.auth.internalauth.AuthInternalViewModel
import com.inventoryapp.rcapp.ui.nav.BottomNavAgentItem
import com.inventoryapp.rcapp.ui.nav.MainNavigation
import com.inventoryapp.rcapp.ui.theme.RcAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val authAgentViewModel by viewModels<AuthAgentViewModel>()
    private val authInternalViewModel by viewModels<AuthInternalViewModel>()
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
                            MainNavigation(authAgentViewModel,authInternalViewModel)
                        }
                    }
                }
            )
        }
    }
}




