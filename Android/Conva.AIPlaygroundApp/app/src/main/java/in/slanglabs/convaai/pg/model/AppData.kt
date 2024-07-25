package `in`.slanglabs.convaai.pg.model

import android.os.Parcelable
import kotlinx.serialization.Serializable
import kotlinx.parcelize.Parcelize

@Serializable
sealed class AppData : Parcelable {
    abstract val app_name: String
    abstract val assistant_id: String
    abstract val assistant_version: String
    abstract val api_key: String
    abstract val homepage_url: String
    abstract val search_url: String
    abstract val package_name: String
    abstract val icon: String
}

@Serializable
@Parcelize
data class LegacyAppData(
    override val app_name: String,
    override val assistant_id: String,
    override val assistant_version: String,
    override val api_key: String,
    override val homepage_url: String,
    override val search_url: String,
    override val package_name: String,
    override val icon: String,
    val capability_groups: Map<String, List<String>>
) : AppData()

@Serializable
@Parcelize
data class RichAppData(
    override val app_name: String,
    override val assistant_id: String,
    override val assistant_version: String,
    override val api_key: String,
    override val homepage_url: String,
    override val search_url: String,
    override val package_name: String,
    override val icon: String,
    val capability_groups: List<String>,
    val capabilities: List<String>
) : AppData()