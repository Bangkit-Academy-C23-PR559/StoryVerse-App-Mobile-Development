package com.c32pr559.storyverse.ui.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.c32pr559.storyverse.R

@Composable
fun TopBar(
    moveToSettingPage: () -> Unit,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    TopAppBar(
        navigationIcon = {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = stringResource(R.string.back),
                modifier = Modifier
                    .padding(16.dp)
                    .clickable { navController.navigateUp() }
            )
        },
        title = {},
        elevation = 0.dp,
        actions = {
            IconButton(
                onClick = moveToSettingPage) {
                Icon(Icons.Default.Settings, contentDescription = "Setting")
            }
        },
        backgroundColor = Color(0xFFA5C67C)
    )
}