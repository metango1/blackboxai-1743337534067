package com.example.emailmanager.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.emailmanager.data.EmailRepository
import com.example.emailmanager.data.PreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val repository: EmailRepository,
    private val preferencesRepository: PreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState

    init {
        viewModelScope.launch {
            preferencesRepository.preferencesFlow.collect { preferences ->
                _uiState.value = _uiState.value.copy(
                    darkModeEnabled = preferences.darkModeEnabled
                )
            }
        }
    }

    fun toggleDarkMode(enabled: Boolean) {
        viewModelScope.launch {
            preferencesRepository.updateDarkMode(enabled)
        }
    }

    fun toggleNotifications(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(
            notificationsEnabled = enabled
        )
    }

    fun clearAllData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                operationError = null
            )
            try {
                repository.clearAllData()
                _uiState.value = _uiState.value.copy(
                    showClearDataSuccess = true,
                    isLoading = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    operationError = e.message,
                    isLoading = false
                )
            }
        }
    }

    fun resetSuccessState() {
        _uiState.value = _uiState.value.copy(
            showClearDataSuccess = false,
            operationError = null
        )
    }

    fun backupData(file: File) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val success = repository.backupDatabase(file)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    operationError = if (!success) "Backup failed" else null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    operationError = "Backup error: ${e.message}"
                )
            }
        }
    }

    fun restoreData(file: File) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                val success = repository.restoreDatabase(file)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    operationError = if (!success) "Restore failed" else null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    operationError = "Restore error: ${e.message}"
                )
            }
        }
    }
}

data class SettingsUiState(
    val darkModeEnabled: Boolean = false,
    val notificationsEnabled: Boolean = true,
    val isLoading: Boolean = false,
    val showClearDataSuccess: Boolean = false,
    val operationError: String? = null
)