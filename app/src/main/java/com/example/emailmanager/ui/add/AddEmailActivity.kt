package com.example.emailmanager.ui.add

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.emailmanager.R
import com.example.emailmanager.databinding.ActivityAddEmailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddEmailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddEmailBinding
    private val viewModel: AddEmailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEmailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupObservers()
        setupListeners()
        viewModel.loadLookupData()
    }

    private fun setupObservers() {
        viewModel.uiState.observe(this, Observer { state ->
            // Update UI based on state
            if (state.isLoading) {
                // Show loading indicator
            } else {
                // Hide loading indicator
                state.error?.let { showError(it) }
                if (state.isSuccess) {
                    // Handle success (e.g., navigate back)
                }
                // Populate tags, groups, and use cases in UI
            }
        })
    }

    private fun setupListeners() {
        binding.btnSave.setOnClickListener {
            val email = Email(
                email_id = binding.etEmailId.text.toString(),
                first_name = binding.etFirstName.text.toString(),
                last_name = binding.etLastName.text.toString(),
                tab_group = binding.etTabGroup.text.toString()
            )
            val selectedTagIds = getSelectedTagIds() // Implement this method
            val selectedGroupIds = getSelectedGroupIds() // Implement this method
            val selectedUseCaseIds = getSelectedUseCaseIds() // Implement this method
            viewModel.addEmail(email, selectedTagIds, selectedGroupIds, selectedUseCaseIds)
        }
    }

    private fun showError(message: String) {
        // Show error message to the user
    }
}