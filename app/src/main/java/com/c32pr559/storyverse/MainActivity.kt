package com.c32pr559.storyverse

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.c32pr559.storyverse.navigation.NavigationItem
import com.c32pr559.storyverse.navigation.Screen
import com.c32pr559.storyverse.ui.screen.BookmarkScreen
import com.c32pr559.storyverse.ui.screen.CategoryScreen
import com.c32pr559.storyverse.ui.screen.DetailScreen
import com.c32pr559.storyverse.ui.screen.ExploreScreen
import com.c32pr559.storyverse.ui.screen.HomeScreen
import com.c32pr559.storyverse.ui.screen.LoginScreen
import com.c32pr559.storyverse.ui.screen.ProfileScreen
import com.c32pr559.storyverse.ui.screen.RegisterScreen
import com.c32pr559.storyverse.ui.screen.SettingScreen
import com.c32pr559.storyverse.ui.screen.UploadScreen
import com.c32pr559.storyverse.ui.screen.WelcomeScreen
import com.c32pr559.storyverse.ui.theme.Poppins
import com.c32pr559.storyverse.ui.theme.StoryVerseTheme
import com.c32pr559.storyverse.ui.viewmodel.BookmarkViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        MobileAds.initialize(this)

        setContent {
            StoryVerseTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    StoryVerseApp()
                }
            }
        }
    }

}

@Composable
fun StoryVerseApp(
    modifier: Modifier = Modifier
) {
    val bookmarkViewModel: BookmarkViewModel = viewModel()
    val navController = rememberNavController()
    val auth = Firebase.auth
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val loggedIn by remember {
        derivedStateOf {
            auth.currentUser != null

        }
    }

    Scaffold(
        bottomBar = {
            if (currentRoute != Screen.Detail.route && currentRoute != Screen.SettingPage.route && currentRoute != Screen.Login.route && currentRoute != Screen.Register.route) {
                BottomBar(navController)
            }
        }
    ) {innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "main",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("main") {
                if (loggedIn) {
                    navController.navigate("home") {
                        popUpTo("main") {
                            inclusive = true
                        }
                    }
                } else {
                    navController.navigate("login") {
                        popUpTo("main") {
                            inclusive = true
                        }
                    }
                }
            }
            composable(Screen.Welcome.route) {
                WelcomeScreen(navController)
            }
            composable(Screen.Login.route) {
                LoginScreen(navController)
            }
            composable(Screen.Home.route) {
                HomeScreen(
                    navController = navController,
                    moveToSettingPage = {navController.navigate(Screen.SettingPage.route)},
                    navigateToDetail = {id ->
                        navController.navigate(Screen.Detail.createRoute(id))
                    }
                )
            }
            composable(Screen.Register.route) { RegisterScreen(navController) }
            composable(Screen.SettingPage.route) {
                SettingScreen(
                    navController
                )
            }
            composable(Screen.Explore.route) { ExploreScreen(navController, moveToSettingPage = { navController.navigate(Screen.SettingPage.route) }) }
            composable("category/{category}") { backStackEntry ->
                val category = backStackEntry.arguments?.getString("category")
                if (category != null) {
                    CategoryScreen(navController, category, moveToSettingPage = { navController.navigate(Screen.SettingPage.route) })
                }
            }
            composable(Screen.Upload.route) { UploadScreen(navController) }
            composable(Screen.Profile.route) { ProfileScreen() }

            composable(
                route = Screen.Detail.route,
                arguments = listOf(navArgument("id"){
                    type = NavType.IntType
                })
            ) {
                // Dapatkan itemId dari argumen URL dan ubah menjadi tipe Int
                val itemId = it.arguments?.getInt("id") ?: -1

                if (itemId != null) {
                    DetailScreen(
                        navController,
                        id = itemId,
                        moveToSettingPage = { navController.navigate(Screen.SettingPage.route)},
                    )
                } else {
                    Column() {
                        Text(text = "Invalid item ID")
                        Text(text = "$id")
                    }
                }
            }
            composable(Screen.Bookmark.route) {
                BookmarkScreen(
                    bookmarkViewModel,
                    navigateToDetail = {id ->
                        navController.navigate(Screen.Detail.createRoute(id))
                    }
                )
            }
        }
    }
}

@Composable
fun BottomBar(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    BottomNavigation(modifier = modifier) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        val navigationItems = listOf(
            NavigationItem(
                title = stringResource(R.string.home),
                icon = Icons.Default.Home,
                screen = Screen.Home
            ),
            NavigationItem(
                title = stringResource(R.string.explore),
                icon = Icons.Default.Explore,
                screen = Screen.Explore
            ),
            NavigationItem(
                title = stringResource(R.string.upload),
                icon = Icons.Default.Add,
                screen = Screen.Upload,
            ),
            NavigationItem(
                title = stringResource(R.string.library),
                icon = Icons.Default.Bookmark,
                screen = Screen.Bookmark
            ),
            NavigationItem(
                title = stringResource(R.string.akun),
                icon = Icons.Default.Person,
                screen = Screen.Profile
            )
        )
        BottomNavigation(modifier = modifier.background(color = Color(0xFFA5C67C))) {
            navigationItems.map { item ->
                BottomNavigationItem(
                    icon = {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.title,
                            modifier = Modifier,
                            tint = Color.Black
                        )
                    },
                    label = { Text(item.title, style = TextStyle(fontFamily = Poppins, color = Color.Black))},
                    selected = currentRoute == item.screen.route,
                    onClick = {
                        navController.navigate(item.screen.route) {
                            popUpTo(navController.graph.findStartDestination().id){
                                saveState = true
                            }
                            restoreState = true
                            launchSingleTop = true
                        }
                    },
                    modifier = modifier.background(color = Color(0xFFA5C67C))
                )
            }
        }
    }
}



