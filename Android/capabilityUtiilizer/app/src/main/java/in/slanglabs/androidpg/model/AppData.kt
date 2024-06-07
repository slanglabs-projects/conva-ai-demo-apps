package `in`.slanglabs.androidpg.model

import android.os.Parcelable
import kotlinx.serialization.Serializable
import kotlinx.parcelize.Parcelize

@Serializable
@Parcelize
data class AppData(
    val app_name: String,
    val assistant_id: String,
    val assistant_version: String,
    val api_key: String,
    val homepage_url: String,
    val search_url: String,
    val package_name: String,
    val icon: String,
    val capability_groups: Map<String, List<String>>
) : Parcelable