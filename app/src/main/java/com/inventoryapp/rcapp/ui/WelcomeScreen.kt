package com.inventoryapp.rcapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavController
import com.inventoryapp.rcapp.R
import com.inventoryapp.rcapp.ui.auth.FirstPageHeader
import com.inventoryapp.rcapp.ui.nav.ROUTE_LOGIN_AGENT
import com.inventoryapp.rcapp.ui.nav.ROUTE_LOGIN_INTERNAL
import com.inventoryapp.rcapp.ui.theme.spacing

@Composable
fun WelcomeScreen (navController: NavController) {
    ConstraintLayout(
        modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.surfaceContainerLowest),
    ) {
        val (refHeader, refVersion, refTitle) = createRefs()
        val spacing = MaterialTheme.spacing
        Box(
            modifier = Modifier
                .constrainAs(refHeader) {
                    top.linkTo(parent.top, spacing.medium)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                }
                .size(width = 20.dp, height = 330.dp)
                .padding(start = 50.dp, end = 40.dp)
        ) {
            FirstPageHeader()
        }
        Column (
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.constrainAs(refTitle) {
                top.linkTo(refHeader.bottom, 20.dp)
                bottom.linkTo(refVersion.top)
                width = Dimension.fillToConstraints
            }
        ) {
            Text(
                text = "Welcome!",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = stringResource(id = R.string.welcoming_word),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.secondary,
                fontWeight = FontWeight.Light,
                modifier = Modifier.padding(10.dp)
            )
            Row (horizontalArrangement = Arrangement.spacedBy(15.dp),
                modifier = Modifier.padding(top = 25.dp)
            ){
                ExtendedFloatingActionButton(
                    onClick = { navController.navigate(ROUTE_LOGIN_INTERNAL)
//                    {
//                        popUpTo(ROUTE_LOGIN_INTERNAL) { inclusive = true }
//                    }
                              },
                    modifier = Modifier
                        .size(158.dp, 53.dp),
                    containerColor = MaterialTheme.colorScheme.onPrimary,
                    contentColor = MaterialTheme.colorScheme.primary
                ){
                    Text(text = "Tim Rc", fontSize = 20.sp)
                }
                ExtendedFloatingActionButton(
                    onClick = { navController.navigate(ROUTE_LOGIN_AGENT)
//                    {
//                        popUpTo(ROUTE_LOGIN_AGENT) { inclusive = true }
//                    }
                              },
                    modifier = Modifier
                        .size(158.dp, 53.dp),
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ){
                    Text(text = "Agen", fontSize = 20.sp)
                }
            }
        }

        Text(text = "Version 1.0.0",
            modifier=Modifier
                .padding(bottom = 8.dp)
                .constrainAs(refVersion){
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            fontWeight = FontWeight.Light
        )
    }

}