package `in`.slanglabs.convaai.pg.ui;

import `in`.slanglabs.convaai.pg.BuildConfig
import `in`.slanglabs.convaai.pg.model.AppData
import `in`.slanglabs.convaai.pg.model.ChatHistory
import `in`.slanglabs.convaai.pg.model.LegacyAppData
import `in`.slanglabs.convaai.pg.model.RichAppData
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject

class Utils {
    companion object {
        var defaultAppData = RichAppData(
            app_name = "",
            assistant_id  = BuildConfig.ASSISTANT_ID,
            assistant_version = BuildConfig.ASSISTANT_VERSION,
            api_key = BuildConfig.API_KEY,
            homepage_url = "",
            search_url = "",
            package_name = "",
            icon = "",
            capabilities = emptyList(),
            capability_groups = emptyList()
        )

        fun getChatMessage(firstString: String, secondString: String, params: String, capability: String): ChatHistory {
            return ChatHistory(
                message = firstString,
                jsonString = secondString,
                params = params,
                capability = capability
            )
        }

        fun parseJson(jsonString: String): AppData {
            val json = Json { ignoreUnknownKeys = true }
            val jsonElement = json.parseToJsonElement(jsonString)

            val jsonObject = jsonElement.jsonObject

            return if (jsonObject.containsKey("capabilities")) {
                json.decodeFromJsonElement(RichAppData.serializer(), jsonElement)
            } else {
                json.decodeFromJsonElement(LegacyAppData.serializer(), jsonElement)
            }
        }

        fun getAssistantCapabilityGroup(appData: LegacyAppData): List<String> {
            return appData.capability_groups.keys.toList().takeIf { it.isNotEmpty() } ?: listOf("default")
        }

        fun getAssistantCapabilities(appData: LegacyAppData, selectedGroup: String): List<String> {
            return appData.capability_groups[selectedGroup] ?: listOf("default")
        }
    }
}
