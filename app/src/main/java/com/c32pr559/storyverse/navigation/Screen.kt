package com.c32pr559.storyverse.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("home")
    object Upload : Screen("upload")
    object Bookmark : Screen("bookmark")
    object Welcome : Screen("welcome")
    object Profile : Screen("profile")
    object SettingPage : Screen("setting_page")
    object Explore : Screen("explore")
    object Detail : Screen("detail/{id}") {
        fun createRoute(id: Int) = "detail/$id"
    }
}
