package `in`.slanglabs.convaai.demo.travel.viewmodel

import androidx.lifecycle.ViewModel
import `in`.slanglabs.convaai.demo.travel.data.repository.SearchRepository

class SearchViewModel(private val searchRepository: SearchRepository) : ViewModel() {
    val searchParams = searchRepository.searchParams

    fun updateDate(date: String?) {
        searchRepository.updateDate(date)
    }

    fun updateSource(source: String?) {
        searchRepository.updateSource(source)
    }

    fun updateDestination(destination: String?) {
        searchRepository.updateDestination(destination)
    }
}