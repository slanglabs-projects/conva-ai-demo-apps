package `in`.slanglabs.convaai.demo.webview

import org.json.JSONArray
import org.json.JSONObject

object Utils {
    fun arrayListToJSONArray(arrayList: List<*>): JSONArray {
        val jsonArray = JSONArray()
        for (item in arrayList) {
            jsonArray.put(item)
        }
        return jsonArray
    }

    fun mapToJSONObject(map: Map<String, Any?>): JSONObject {
        val jsonObject = JSONObject()
        for ((key, value) in map) {
            value?.let { jsonObject.put(key, it) }
        }
        return jsonObject
    }
}