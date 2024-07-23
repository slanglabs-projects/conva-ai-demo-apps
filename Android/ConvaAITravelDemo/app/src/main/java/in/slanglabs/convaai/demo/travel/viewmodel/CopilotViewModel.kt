package `in`.slanglabs.convaai.demo.travel.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import `in`.slanglabs.convaai.copilot.platform.ConvaAIResponse
import `in`.slanglabs.convaai.demo.travel.convaai.CopilotManager
import `in`.slanglabs.convaai.demo.travel.data.repository.SearchRepository
import `in`.slanglabs.convaai.demo.travel.ui.navigation.NavigationRoutes
import `in`.slanglabs.convaai.demo.travel.utils.Utils
import kotlinx.coroutines.launch

class CopilotViewModel(
    private val copilotManager: CopilotManager,
    private val navController: NavHostController,
    private val searchRepository: SearchRepository
) : ViewModel() {
    val isCopilotReady = copilotManager.isCopilotInitialized
    init {
        viewModelScope.launch {
            copilotManager.copilotResponseState.collect { response ->
                response?.let { handleCopilotResponse(it) }
            }
        }
    }

    fun invokeCopilot() {
        copilotManager.invokeCopilot()
    }

    private fun handleCopilotResponse(response: ConvaAIResponse) {
        when (response.capability) {
            "bus_ticket_booking" -> {
                val source = response.params["source"] as String?
                val destination = response.params["destination"] as String?
                val date = response.params["date_of_travel"] as String?
                val convertedDate = Utils.convertDateFormat(date).ifBlank { date }

                // Update search params in search repository
                searchRepository.updateSearchParams(source, destination, convertedDate)

                if (source != null && destination != null && !convertedDate.isNullOrEmpty()) {
                    navController.navigate(
                        NavigationRoutes.busListRoute(source, destination, Utils.encodeDate(convertedDate))
                    )
                }
            }
        }
    }
}