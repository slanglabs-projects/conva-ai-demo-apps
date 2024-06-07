package `in`.slanglabs.androidpg.model

import android.os.Bundle
import androidx.navigation.NavType
import com.google.gson.Gson

class AppDataType : NavType<AppData>(isNullableAllowed = false) {
    override fun get(bundle: Bundle, key: String): AppData? {
        return bundle.getParcelable(key)
    }
    override fun parseValue(value: String): AppData {
        return Gson().fromJson(value, AppData::class.java)
    }
    override fun put(bundle: Bundle, key: String, value: AppData) {
        bundle.putParcelable(key, value)
    }
}