package com.example.emailmanager.ui.search

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.example.emailmanager.R
import com.example.emailmanager.databinding.ActivitySearchBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private val viewModel: SearchViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupSearchView()
        setupObservers()
    }

    private fun setupSearchView() {
        binding.searchView.apply {
            setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    query?.let { viewModel.onSearchQueryChanged(it) }
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    newText?.let { viewModel.onSearchQueryChanged(it) }
                    return true
                }
            })
        }
    }

    private fun setupObservers() {
        viewModel.searchState.observe(this, Observer { state ->
            if (state.isLoading) {
                // Show loading indicator
            } else {
                // Hide loading indicator
                state.error?.let { showError(it) }
                displayResults(state.searchResults)
            }
        })
    }

    private fun displayResults(emails: List<Email>) {
        if (emails.isEmpty()) {
            binding.rvSearchResults.visibility = View.GONE
            binding.tvEmpty.visibility = View.VISIBLE
        } else {
            binding.rvSearchResults.visibility = View.VISIBLE
            binding.tvEmpty.visibility = View.GONE
            val adapter = EmailAdapter(emails) { email ->
                // Handle email item click (e.g., show details)
                showEmailDetails(email)
            }
            binding.rvSearchResults.adapter = adapter
        }
    }

    private fun showEmailDetails(email: Email) {
        // Implement email details display
        Toast.makeText(this, "Selected: ${email.email_id}", Toast.LENGTH_SHORT).show()
    }

    private fun showError(message: String) {
        // Show error message to user
    }
}