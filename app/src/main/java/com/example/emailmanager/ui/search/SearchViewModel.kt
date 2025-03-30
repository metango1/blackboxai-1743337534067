package com.example.emailmanager.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.emailmanager.data.EmailRepository
import com.example.emailmanager.db.entities.EmailWithRelations
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: EmailRepository
) : ViewModel() {

    private val _searchState = MutableStateFlow(SearchUiState())
    val searchState: StateFlow<SearchUiState> = _searchState

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    init {
        setupSearchDebounce()
    }

    private fun setupSearchDebounce() {
        _searchQuery
            .debounce(300) // 300ms debounce time
            .onEach { query ->
                if (query.isNotEmpty()) {
                    performSearch(query)
                } else {
                    _searchState.value = SearchUiState()
                }
            }
            .launchIn(viewModelScope)
    }

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    private fun performSearch(query: String) {
        viewModelScope.launch {
            _searchState.value = _searchState.value.copy(isLoading = true)
            try {
                val emailResults = repository.searchEmails(query)
                val tagResults = repository.searchTags(query)
                val groupResults = repository.searchBrowserGroups(query)
                val useCaseResults = repository.searchUseCases(query)

                _searchState.value = _searchState.value.copy(
                    searchResults = emailResults,
                    tagResults = tagResults,
                    groupResults = groupResults,
                    useCaseResults = useCaseResults,
                    isLoading = false,
                    error = null
                )
            } catch (e: Exception) {
                _searchState.value = _searchState.value.copy(
                    error = e.message,
                    isLoading = false
                )
            }
        }
    }

    fun clearSearch() {
        _searchQuery.value = ""
        _searchState.value = SearchUiState()
    }
}

data class SearchUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val searchResults: List<Email> = emptyList(),
    val tagResults: List<Tag> = emptyList(),
    val groupResults: List<BrowserGroup> = emptyList(),
    val useCaseResults: List<UseCase> = emptyList()
)