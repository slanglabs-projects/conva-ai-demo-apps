package `in`.slanglabs.convaai.demo.travel.data.repository

import `in`.slanglabs.convaai.demo.travel.data.model.SearchParams
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SearchRepository {
    private val _searchParams = MutableStateFlow<SearchParams?>(null)
    val searchParams: StateFlow<SearchParams?> = _searchParams.asStateFlow()

    fun updateSearchParams(source: String?, destination: String?, date: String?) {
        _searchParams.value = SearchParams(source, destination, date)
    }

    fun updateDate(date: String?) {
        _searchParams.value = _searchParams.value?.copy(date = date)
    }

    fun updateSource(source: String?) {
        _searchParams.value = _searchParams.value?.copy(source = source)
    }

    fun updateDestination(destination: String?) {
        _searchParams.value = _searchParams.value?.copy(destination = destination)
    }
}