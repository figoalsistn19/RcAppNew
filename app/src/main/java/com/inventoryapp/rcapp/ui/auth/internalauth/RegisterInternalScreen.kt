package com.inventoryapp.rcapp.ui.auth.internalauth

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.inventoryapp.rcapp.R
import com.inventoryapp.rcapp.data.model.InternalUser
import com.inventoryapp.rcapp.data.model.UserRole
import com.inventoryapp.rcapp.ui.theme.RcAppTheme
import com.inventoryapp.rcapp.ui.theme.spacing
import com.inventoryapp.rcapp.util.Resource
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterInternalScreen (viewModel: AuthInternalViewModel?, navController: NavController) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    val onBackPressed = remember { mutableStateOf(false) }
    val authResource =viewModel?.registerFlow?.collectAsState()
    val list = UserRole.entries.map { it.name }
    var expanded by remember { mutableStateOf(false) }
    var selectedMenu by remember {
        mutableStateOf(list[0])
    }
    val enumValue = UserRole.entries.find { it.name == selectedMenu }
    val context = LocalContext.current

    fun getUserObj(): InternalUser {
        return InternalUser(
            idUser = "",
            name = name,
            phoneNumber = phoneNumber,
            email = email,
            createAt = Date(),
            userRole = enumValue!!
        )
    }
    val userObj: InternalUser = getUserObj()

    ConstraintLayout(
    modifier = Modifier.fillMaxSize()
        .background(MaterialTheme.colorScheme.surfaceContainerLowest)
    ) {
        val (refHeader, refName, refEmail, refPassword, refButtonSignup, refLoading, refConfirmPw, refPhoneNumber, refRole) = createRefs()
        val spacing = MaterialTheme.spacing
        Box(
            modifier = Modifier
                .constrainAs(refHeader) {
                    top.linkTo(parent.top, spacing.extraLarge)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                }
        ) {
//            AuthHeader("Daftar Internal")
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    modifier = Modifier
                        .size(128.dp, 128.dp),
                    painter = painterResource(id = R.drawable.rc_logo),
                    contentDescription = stringResource(id = R.string.app_name)
                )
            }
        }
        OutlinedTextField(
            value = name,
            onValueChange = {
                name = it
            },
            label = {
                Text("name")
            },
            modifier = Modifier.constrainAs(refName) {
                top.linkTo(refHeader.bottom, spacing.small)
                start.linkTo(parent.start, spacing.large)
                end.linkTo(parent.end, spacing.large)
                width = Dimension.fillToConstraints
            },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.None,
                autoCorrect = false,
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            maxLines = 1
        )

        OutlinedTextField(
            value = email,
            onValueChange = {
                email = it
            },
            label = {
                Text(text = "email")
            },
            modifier = Modifier.constrainAs(refEmail) {
                top.linkTo(refName.bottom, spacing.small)
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

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
            },
            label = {
                Text(text = "password")
            },
            modifier = Modifier.constrainAs(refPassword) {
                top.linkTo(refEmail.bottom, spacing.small)
                start.linkTo(parent.start, spacing.large)
                end.linkTo(parent.end, spacing.large)
                width = Dimension.fillToConstraints
            },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.None,
                autoCorrect = false,
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            maxLines = 1
        )
        OutlinedTextField(
            value = confirmPassword,
            onValueChange = {
                confirmPassword = it
            },
            label = {
                Text(text = "konfirmasi password")
            },
            modifier = Modifier.constrainAs(refConfirmPw) {
                top.linkTo(refPassword.bottom, spacing.small)
                start.linkTo(parent.start, spacing.large)
                end.linkTo(parent.end, spacing.large)
                width = Dimension.fillToConstraints
            },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.None,
                autoCorrect = false,
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            maxLines = 1
        )
        OutlinedTextField(
            value = phoneNumber,
            onValueChange = {
                phoneNumber = it
            },
            label = {
                Text(text = "no telepon")
            },
            modifier = Modifier.constrainAs(refPhoneNumber) {
                top.linkTo(refConfirmPw.bottom, spacing.small)
                start.linkTo(parent.start, spacing.large)
                end.linkTo(parent.end, spacing.large)
                width = Dimension.fillToConstraints
            },
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.None,
                autoCorrect = false,
                keyboardType = KeyboardType.Phone,
                imeAction = ImeAction.Done
            ),
            maxLines = 1
        )
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
            modifier = Modifier
                .constrainAs(refRole) {
                    top.linkTo(refPhoneNumber.bottom, spacing.medium)
                    start.linkTo(refPhoneNumber.start)
                    end.linkTo(refPhoneNumber.end)
                    width = Dimension.fillToConstraints
                }
                .fillMaxWidth()
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .menuAnchor()
                    .constrainAs(refRole) {
                        top.linkTo(refPhoneNumber.bottom, spacing.medium)
                        width = Dimension.fillToConstraints
                    }
                    .fillMaxWidth(),
                value = selectedMenu,
                onValueChange = {},
                readOnly = true,
                label = { Text(text = "pilih role")},
                trailingIcon = { Icon(imageVector = Icons.Outlined.ArrowDropDown, contentDescription = "pilih role")},
            )
            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                list.forEachIndexed{index, text ->
                    DropdownMenuItem(
                        modifier = Modifier.background(color = MaterialTheme.colorScheme.secondaryContainer),
                        text = { Text(text = text) },
                        onClick = {
                            selectedMenu = list[index]
                            expanded = false
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                    )
                }
            }
            // Add an icon if desired (optional)
            // Icon(imageVector = Icons.ArrowDropDown, contentDescription = null)
        }
        Button(
            onClick = {
                viewModel?.registerUser(name, email, password, user = userObj)
                navController.popBackStack()
            },
            modifier = Modifier.constrainAs(refButtonSignup) {
                top.linkTo(refRole.bottom, spacing.medium)
                start.linkTo(parent.start, spacing.extraLarge)
                end.linkTo(parent.end, spacing.extraLarge)
                width = Dimension.fillToConstraints
            }
        ) {
            Text(text = "Register", style = MaterialTheme.typography.titleMedium)
        }
//        Text(
//            modifier = Modifier
//                .constrainAs(refTextSignup) {
//                    top.linkTo(refButtonSignup.bottom, spacing.small)
//                    start.linkTo(parent.start, spacing.extraLarge)
//                    end.linkTo(parent.end, spacing.extraLarge)
//                }
//                .clickable {
//                    navController.navigate(ROUTE_LOGIN_AGENT) {
//                        popUpTo(ROUTE_REGISTER_AGENT) { inclusive = true }
//                    }
//                },
//            text = "Login disini",
//            style = MaterialTheme.typography.bodyLarge,
//            textAlign = TextAlign.Center,
//            color = MaterialTheme.colorScheme.onSurface
//        )
        LaunchedEffect(key1 = onBackPressed) {
            if (onBackPressed.value) {
                navController.popBackStack() // Or use other methods if needed
            }
        }
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
//                    LaunchedEffect(Unit) {
//                        navController.navigate(ROUTE_HOME_INTERNAL_SCREEN)
//                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserRoleDropdownMenu(
) {
    val list = listOf("nwdnquw","weqweq","qdqddqw","dwqdq")
    var expanded by remember { mutableStateOf(false) }
    var selectedMenu by remember {
        mutableStateOf(list[0])
    }
    Column {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            TextField(
                modifier = Modifier.menuAnchor(),
                value = selectedMenu,
                onValueChange = {},
                readOnly = true,
            )

            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                list.forEachIndexed{index, text ->
                    DropdownMenuItem(
                        text = { Text(text = text) },
                        onClick = {
                            selectedMenu = list[index]
                            expanded = false
                                  },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                    )
                }
            }
            // Add an icon if desired (optional)
            // Icon(imageVector = Icons.ArrowDropDown, contentDescription = null)
        }
    }
}


@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO, apiLevel = 33)
@Composable
fun RegisterScreenPreviewLight() {
    RcAppTheme {
        RegisterInternalScreen(null, rememberNavController())
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES, apiLevel = 33)
@Composable
fun RegisterScreenPreviewDark() {
    RcAppTheme {
        RegisterInternalScreen(null, rememberNavController())
    }
}



