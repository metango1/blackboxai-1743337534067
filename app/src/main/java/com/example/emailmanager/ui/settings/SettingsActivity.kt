package com.example.emailmanager.ui.settings

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.emailmanager.R
import com.example.emailmanager.databinding.ActivitySettingsBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private val viewModel: SettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupObservers()
        setupListeners()
    }

    private fun setupObservers() {
        viewModel.uiState.observe(this, Observer { state ->
            binding.switchDarkMode.isChecked = state.darkModeEnabled
            binding.switchNotifications.isChecked = state.notificationsEnabled

            if (state.isLoading) {
                // Show loading indicator
            } else {
                // Hide loading indicator
                state.operationError?.let { showError(it) }
                if (state.showClearDataSuccess) {
                    showSuccess(getString(R.string.data_cleared_success))
                }
            }
        })
    }

    private fun setupListeners() {
        binding.switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            viewModel.toggleDarkMode(isChecked)
        }

        binding.switchNotifications.setOnCheckedChangeListener { _, isChecked ->
            viewModel.toggleNotifications(isChecked)
        }

        binding.btnClearData.setOnClickListener {
            showClearDataConfirmation()
        }
    }

    private fun showClearDataConfirmation() {
        MaterialAlertDialogBuilder(this)
            .setTitle(R.string.clear_data_title)
            .setMessage(R.string.clear_data_message)
            .setPositiveButton(R.string.clear) { _, _ ->
                viewModel.clearAllData()
            }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }

    private fun showError(message: String) {
        // Show error message
    }

    private val backupLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            result.data?.data?.let { uri ->
                try {
                    val file = File(uri.path ?: return@let)
                    viewModel.backupData(file)
                    showSuccess(getString(R.string.backup_success))
                } catch (e: Exception) {
                    showError(getString(R.string.backup_failed))
                }
            }
        }
    }

    private val restoreLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            result.data?.data?.let { uri ->
                MaterialAlertDialogBuilder(this)
                    .setTitle(R.string.restore)
                    .setMessage(R.string.restore_confirmation)
                    .setPositiveButton(R.string.restore) { _, _ ->
                        try {
                            val file = File(uri.path ?: return@setPositiveButton)
                            viewModel.restoreData(file)
                            showSuccess(getString(R.string.restore_success))
                        } catch (e: Exception) {
                            showError(getString(R.string.restore_failed))
                        }
                    }
                    .setNegativeButton(R.string.cancel, null)
                    .show()
            }
        }
    }

    private fun showSuccess(message: String) {
        // Show success message
    }
}