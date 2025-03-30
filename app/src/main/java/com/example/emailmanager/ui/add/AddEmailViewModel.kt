package com.example.emailmanager.ui.add

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.emailmanager.data.EmailRepository
import com.example.emailmanager.db.entities.Email
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEmailViewModel @Inject constructor(
    private val repository: EmailRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddEmailUiState())
    val uiState: StateFlow<AddEmailUiState> = _uiState

    fun addEmail(email: Email, tagIds: List<Int>, groupIds: List<Int>, useCaseIds: List<Int>) {
        viewModelScope.launch {
            try {
                repository.createEmailWithRelations(email, tagIds, groupIds, useCaseIds)
                _uiState.value = _uiState.value.copy(
                    isSuccess = true,
                    isLoading = false,
                    error = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message,
                    isLoading = false
                )
            }
        }
    }

    fun loadLookupData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val tags = repository.getAllTags()
                val groups = repository.getAllBrowserGroups()
                val useCases = repository.getAllUseCases()
                _uiState.value = _uiState.value.copy(
                    tags = tags,
                    browserGroups = groups,
                    useCases = useCases,
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    error = e.message,
                    isLoading = false
                )
            }
        }
    }
}

data class AddEmailUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null,
    val tags: List<Tag> = emptyList(),
    val browserGroups: List<BrowserGroup> = emptyList(),
    val useCases: List<UseCase> = emptyList()
)