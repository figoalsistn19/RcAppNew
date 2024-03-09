package com.inventoryapp.rcapp.ui.nav

import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavAgentItem (
    val badgeCount: Int? = null,
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val hasNews: Boolean

)