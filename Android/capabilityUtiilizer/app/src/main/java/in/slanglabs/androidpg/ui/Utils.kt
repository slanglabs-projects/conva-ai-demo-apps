package `in`.slanglabs.androidpg.ui;

import `in`.slanglabs.androidpg.BuildConfig
import `in`.slanglabs.androidpg.model.AppData
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class Utils {
    companion object {
        var defaultAppData = AppData(
            app_name = "",
            assistant_id  = BuildConfig.ASSISTANT_ID,
            assistant_version = BuildConfig.ASSISTANT_VERSION,
            api_key = BuildConfig.API_KEY,
            homepage_url = "",
            search_url = "",
            package_name = "",
            icon = "",
            capability_groups = HashMap()
        )

        fun getChatMessage(firstString: String, secondString: String): Pair<String, String> {
            return firstString to secondString
        }

        fun parseJson(jsonString: String): AppData {
            return Json { ignoreUnknownKeys = true }.decodeFromString(jsonString)
        }

        fun getAssistantCapabilityGroup(appData: AppData): List<String> {
            return appData.capability_groups.keys.toList().takeIf { it.isNotEmpty() } ?: listOf("default")
        }

        fun getAssistantCapabilities(appData: AppData, selectedGroup: String): List<String> {
            return appData.capability_groups[selectedGroup] ?: listOf("default")
        }
    }
}
