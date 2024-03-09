package com.inventoryapp.rcapp.ui.nav

sealed class Screen (val route: String){
    object WelcomeScreen: Screen("welcome_screen")
    object AgentScreen: Screen("agent_screen")
    object InternalScreen: Screen("internal_screen")

    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }
}