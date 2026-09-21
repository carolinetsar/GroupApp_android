package com.groupapp.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.groupapp.ui.screens.CreateGatheringScreen
import com.groupapp.ui.screens.DetailScreen
import com.groupapp.ui.screens.FeedScreen
import com.groupapp.ui.screens.MyGatheringsScreen
import com.groupapp.ui.screens.ProfileScreen

object Routes {
    const val FEED = "feed"
    const val MY = "my"
    const val PROFILE = "profile"
    const val CREATE = "create"
    const val DETAIL = "detail/{id}"

    fun detail(id: String): String = "detail/$id"
}

private data class Tab(val route: String, val label: String, val icon: ImageVector)

private val TABS = listOf(
    Tab(Routes.FEED, "Сборы", Icons.Default.Home),
    Tab(Routes.MY, "Мои сборы", Icons.Default.List),
    Tab(Routes.PROFILE, "Профиль", Icons.Default.Person)
)

@Composable
fun AppBottomBar(currentRoute: String, onNavigate: (String) -> Unit) {
    NavigationBar {
        TABS.forEach { tab ->
            NavigationBarItem(
                selected = currentRoute == tab.route,
                onClick = { onNavigate(tab.route) },
                icon = { Icon(tab.icon, contentDescription = tab.label) },
                label = { Text(tab.label) }
            )
        }
    }
}

@Composable
fun AppNav() {
    val navController = rememberNavController()
    val vm: AppViewModel = viewModel()

    // Single shared "go to tab" action: keeps one entry per tab and restores scroll state.
    val goTab: (String) -> Unit = remember(navController) {
        { target ->
            navController.navigate(target) {
                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                launchSingleTop = true
                restoreState = true
            }
        }
    }

    NavHost(navController = navController, startDestination = Routes.FEED) {
        composable(Routes.FEED) {
            FeedScreen(vm = vm, nav = navController, goTab = goTab)
        }
        composable(Routes.MY) {
            MyGatheringsScreen(vm = vm, nav = navController, goTab = goTab)
        }
        composable(Routes.PROFILE) {
            ProfileScreen(vm = vm, nav = navController, goTab = goTab)
        }
        composable(Routes.CREATE) {
            CreateGatheringScreen(vm = vm, nav = navController)
        }
        composable(
            route = Routes.DETAIL,
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) { entry ->
            DetailScreen(
                vm = vm,
                nav = navController,
                gatheringId = entry.arguments?.getString("id").orEmpty()
            )
        }
    }
}
