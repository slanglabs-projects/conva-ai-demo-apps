package `in`.slanglabs.convaai.demo.travel.ui.navigation

object NavigationRoutes {
    const val HOME = "home"
    const val ACCOUNT = "account"
    const val TRIPS = "trips"
    const val BUS_LIST = "bus_list/{source}/{destination}/{date}"

    fun busListRoute(source: String, destination: String, date: String): String {
        return "bus_list/$source/$destination/$date"
    }
}