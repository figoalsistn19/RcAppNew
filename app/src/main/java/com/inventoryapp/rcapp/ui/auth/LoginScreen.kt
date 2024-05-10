package com.inventoryapp.rcapp.ui.auth

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavController
import com.inventoryapp.rcapp.ui.auth.internalauth.AuthViewModel
import com.inventoryapp.rcapp.ui.nav.ROUTE_REGISTER_AGENT
import com.inventoryapp.rcapp.ui.theme.spacing
import com.inventoryapp.rcapp.util.Resource

@Composable
fun LoginScreen(viewModel: AuthViewModel?, navController: NavController){
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    val authResource = viewModel?.loginFlow?.collectAsState()
    val context = LocalContext.current
    val navigateToScreen by viewModel?.navigateToScreen!!.observeAsState()

//    fun onStart(){
//        viewModel?.getSession { user ->
//            if (user != null){
//                navController.navigate(ROUTE_MAIN_INTERNAL_SCREEN) {
//                    popUpTo(ROUTE_LOGIN_INTERNAL) { inclusive = true }
//                    popUpTo(ROUTE_HOME){ inclusive = true}
//                }
//            }
//        }
//    }
//    LaunchedEffect(navigateToScreen ) {
//        viewModel!!.checkUserLoggedIn()
//        navigateToScreen?.let { screen ->
//            navController.navigate(screen)
//            viewModel.navigateToScreen(screen) // Setelah navigasi selesai, reset nilai LiveData
//        }
//    }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = MaterialTheme.colorScheme.surfaceContainerLowest
            )
    ) {
//        onStart()
        val (refHeader, refEmail, refPassword, refButtonLogin, refTextSignup, refLoading, refVersion) = createRefs()
        val spacing = MaterialTheme.spacing

        Box(
            modifier = Modifier
                .constrainAs(refHeader) {
                    top.linkTo(parent.top, spacing.extraLarge)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                }
                .wrapContentSize()
        ) {
            AuthHeader("Login")
        }
        TextField(
            value = email,
            onValueChange = {
                email = it
            },
            label = {
                Text("email")
            },
            modifier = Modifier.constrainAs(refEmail) {
                top.linkTo(refHeader.bottom, spacing.extraLarge)
                start.linkTo(parent.start, spacing.large)
                end.linkTo(parent.end, spacing.large)
                width = Dimension.fillToConstraints
            },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.None,
                autoCorrect = false,
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            maxLines = 1
        )

        TextField(
            value = password,
            onValueChange = {
                password = it
            },
            label = {
                Text("password")
            },
            modifier = Modifier.constrainAs(refPassword) {
                top.linkTo(refEmail.bottom, spacing.medium)
                start.linkTo(parent.start, spacing.large)
                end.linkTo(parent.end, spacing.large)
                width = Dimension.fillToConstraints
            },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.None,
                autoCorrect = false,
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            maxLines = 1,
            visualTransformation = PasswordVisualTransformation()
        )

        Button(
            onClick = {
                viewModel?.loginUser(email, password)
            },
            modifier = Modifier.constrainAs(refButtonLogin) {
                top.linkTo(refPassword.bottom, spacing.extraLarge)
                start.linkTo(parent.start, spacing.extraLarge)
                end.linkTo(parent.end, spacing.extraLarge)
                bottom.linkTo(refTextSignup.top)
                width = Dimension.wrapContent
            }
        ) {
            Text("LOGIN", style = MaterialTheme.typography.titleMedium)
        }
//        FilledTonalButton(
//            modifier = Modifier
//                .constrainAs(refTextSignup) {
//                    top.linkTo(refButtonLogin.bottom, spacing.medium)
//                    start.linkTo(parent.start, spacing.extraLarge)
//                    end.linkTo(parent.end, spacing.extraLarge)
//                },
//            onClick = {
//                navController.navigate(ROUTE_REGISTER_AGENT)
//            }
//        ){
//            Text(text = "DAFTAR", style = MaterialTheme.typography.titleMedium)
//        }

        Text(text = "Version 1.0.0",
            modifier= Modifier
                .padding(top = 140.dp)
                .constrainAs(refVersion) {
                    bottom.linkTo(parent.bottom, spacing.small)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            fontWeight = FontWeight.Light
        )

        authResource?.value?.let {
            when (it) {
                is Resource.Failure -> {
                    Toast.makeText(context, it.throwable.message, Toast.LENGTH_SHORT).show()
                }
                is Resource.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.constrainAs(refLoading) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    })
                }
                is Resource.Success -> {
                    LaunchedEffect(navigateToScreen) {
                        viewModel.checkUserLoggedIn()
                        navigateToScreen?.let { screen ->
                            navController.navigate(screen)
                            viewModel.navigateToScreen(screen) // Setelah navigasi selesai, reset nilai LiveData
                        }
                    }
                }
            }
        }

    }
}