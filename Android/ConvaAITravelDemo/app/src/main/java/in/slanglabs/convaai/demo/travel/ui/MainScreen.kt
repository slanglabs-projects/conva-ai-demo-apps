package `in`.slanglabs.convaai.demo.travel.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import `in`.slanglabs.convaai.demo.travel.R
import `in`.slanglabs.convaai.demo.travel.data.repository.SearchRepository
import `in`.slanglabs.convaai.demo.travel.ui.navigation.AppNavigation
import `in`.slanglabs.convaai.demo.travel.viewmodel.CopilotViewModel

@Composable
fun MainScreen(copilotViewModel: CopilotViewModel, searchRepository: SearchRepository) {
    val navController = rememberNavController()
    val isCopilotReady by copilotViewModel.isCopilotReady.collectAsState()
    Scaffold(
        bottomBar = { BottomNavBar(navController) },
        floatingActionButton = {
            // Only display the FAB once the copilot is initialized
            if (isCopilotReady) {
                FloatingActionButton(
                    onClick = { copilotViewModel.invokeCopilot() },
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_copilot),
                        contentDescription = "Invoke ConvaAI Copilot"
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            // Your content here
            AppNavigation(navController, searchRepository)
        }
    }
}

@Composable
fun BottomNavBar(navController: NavController) {
    val items = listOf(
        BottomNavItem("home", R.drawable.ic_home, "Home"),
        BottomNavItem("trips", R.drawable.ic_trips, "Trips"),
        BottomNavItem("account", R.drawable.ic_account, "Account")
    )
    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(painterResource(id = item.icon), contentDescription = item.title) },
                label = { Text(item.title) },
                selected = currentRoute == item.route,
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }
    }
}

data class BottomNavItem(val route: String, val icon: Int, val title: String)