package com.example.emailmanager.ui.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.emailmanager.databinding.ItemEmailBinding
import com.example.emailmanager.db.entities.Email

class EmailAdapter(
    private val emails: List<Email>,
    private val onItemClick: (Email) -> Unit
) : RecyclerView.Adapter<EmailAdapter.EmailViewHolder>() {

    inner class EmailViewHolder(private val binding: ItemEmailBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(email: Email) {
            binding.apply {
                tvEmailId.text = email.email_id
                tvName.text = "${email.first_name} ${email.last_name}"
                tvTabGroup.text = email.tab_group
                root.setOnClickListener { onItemClick(email) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EmailViewHolder {
        val binding = ItemEmailBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return EmailViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EmailViewHolder, position: Int) {
        holder.bind(emails[position])
    }

    override fun getItemCount() = emails.size
}