package `in`.slanglabs.convaai.demo.travel.utils

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

object Utils {
    // Helper function to convert date format
    fun convertDateFormat(dateString: String?): String {
        if (dateString == null) return ""
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return try {
            val formatDate = inputFormat.parse(dateString)
            outputFormat.format(formatDate!!)
        } catch (e: ParseException) {
            "" // Return empty string if there was an error
        }
    }

    // Helper function to encode date
    fun encodeDate(dateString: String): String {
        return dateString.replace("/", "-")
    }

    // Helper function to decode date
    fun decodeDate(dateString: String): String {
        return dateString.replace("-", "/")
    }
}