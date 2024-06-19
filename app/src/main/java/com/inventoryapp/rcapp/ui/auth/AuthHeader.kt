package com.inventoryapp.rcapp.ui.auth

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.inventoryapp.rcapp.R
import com.inventoryapp.rcapp.ui.theme.RcAppTheme
import com.inventoryapp.rcapp.ui.theme.spacing

@Composable
fun AuthHeader(textHeader: String?) {
    Column(
        modifier = Modifier.wrapContentSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val spacing = MaterialTheme.spacing
        Image(
            modifier = Modifier
                .size(128.dp, 128.dp),
            painter = painterResource(id = R.drawable.rc_logo),
            contentDescription = stringResource(id = R.string.app_name)
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            text = textHeader!!,
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun FirstPageHeader() {
    Column {
        ConstraintLayout(
        ) {
            val spacing = MaterialTheme.spacing
            val (refLogo,refVector) = createRefs()
            Image(
                modifier = Modifier
                    .size(120.dp, 120.dp)
                    .constrainAs(refLogo) {
                        top.linkTo(parent.top, spacing.medium)
                        end.linkTo(refVector.end)
                        width = Dimension.fillToConstraints
                    },
                painter = painterResource(id = R.drawable.rc_logo),
                contentDescription = stringResource(id = R.string.app_name)
            )
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(refVector) {
                        top.linkTo(refLogo.bottom, spacing.extraSmall)
                        end.linkTo(refLogo.end)
                        width = Dimension.fillToConstraints
                    },
                imageVector = ImageVector.vectorResource(id = R.drawable.firstpage),
                contentDescription = stringResource(id = R.string.app_name)
            )
        }
    }

}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_NO, apiLevel = 33)
@Composable
fun AppHeaderLight() {
    RcAppTheme {
        AuthHeader("Agent")
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun AppHeaderDark() {
    RcAppTheme {
        AuthHeader("Agent")
    }
}