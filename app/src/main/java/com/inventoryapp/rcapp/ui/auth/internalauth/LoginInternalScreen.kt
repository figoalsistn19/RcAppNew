package com.inventoryapp.rcapp.ui.auth.internalauth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.inventoryapp.rcapp.ui.nav.Logo
import com.inventoryapp.rcapp.ui.nav.ROUTE_HOME
import com.inventoryapp.rcapp.ui.nav.SmallButtonPrimary
import com.inventoryapp.rcapp.ui.theme.RcAppTheme

@Composable
fun LoginInternalScreen(navController: NavController){
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    RcAppTheme {
        Surface (
            modifier = Modifier.fillMaxSize()
        ){
            Column (
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Logo(modifier = Modifier.padding(top=50.dp))
                Text(text = "Login Internal",
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                OutlinedTextField(
                    value = email,
                    onValueChange = { newText ->
                        email = newText.trimStart { it == '0' } },
                    label = { Text("Email") },
                    modifier = Modifier
                        .width(308.dp)
                        .padding(top = 30.dp),
                    maxLines = 1
                )
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    maxLines = 1,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.width(308.dp)
                )
                SmallButtonPrimary(text = "LOGIN", navController.navigate(ROUTE_HOME), modifier = Modifier.padding(15.dp))
            }
            Column (verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Version 1.0",
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontWeight = FontWeight(200),
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                )
            }
        }
    }
}






@Composable
fun Greeting2(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    RcAppTheme {
        Greeting2("Android")
    }
}