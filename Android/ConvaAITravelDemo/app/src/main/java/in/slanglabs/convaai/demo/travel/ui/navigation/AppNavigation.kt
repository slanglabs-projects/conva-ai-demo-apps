package `in`.slanglabs.convaai.demo.travel.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import `in`.slanglabs.convaai.demo.travel.data.repository.SearchRepository
import `in`.slanglabs.convaai.demo.travel.ui.AccountPage
import `in`.slanglabs.convaai.demo.travel.ui.BusListPage
import `in`.slanglabs.convaai.demo.travel.ui.HomePage
import `in`.slanglabs.convaai.demo.travel.ui.TripsPage
import `in`.slanglabs.convaai.demo.travel.viewmodel.SearchViewModel

@Composable
fun AppNavigation(navController: NavHostController, searchRepository: SearchRepository) {
    val searchViewModel = SearchViewModel(searchRepository)
    NavHost(navController, startDestination = NavigationRoutes.HOME) {
        composable(NavigationRoutes.HOME) { HomePage(navController, searchViewModel) }
        composable(NavigationRoutes.ACCOUNT) { AccountPage() }
        composable(NavigationRoutes.TRIPS) { TripsPage() }
        composable(NavigationRoutes.BUS_LIST) { backStackEntry ->
            val source = backStackEntry.arguments?.getString("source") ?: ""
            val destination = backStackEntry.arguments?.getString("destination") ?: ""
            val date = backStackEntry.arguments?.getString("date") ?: ""
            BusListPage(navController, source, destination, date)
        }
    }
}
