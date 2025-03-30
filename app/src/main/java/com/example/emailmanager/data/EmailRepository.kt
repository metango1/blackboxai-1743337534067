package com.example.emailmanager.data

import com.example.emailmanager.db.daos.*
import com.example.emailmanager.db.entities.*
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class EmailRepository @Inject constructor(
    private val emailDao: EmailDao,
    private val tagDao: TagDao,
    private val browserGroupDao: BrowserGroupDao,
    private val useCaseDao: UseCaseDao
) {
    // Email operations
    suspend fun insertEmail(email: Email) = emailDao.insertEmail(email)
    suspend fun updateEmail(email: Email) = emailDao.updateEmail(email)
    suspend fun deleteEmail(email: Email) = emailDao.deleteEmail(email)
    suspend fun getEmailById(emailId: String) = emailDao.getEmailById(emailId)

    // Relationship operations
    suspend fun addTagToEmail(emailId: String, tagId: Int) {
        emailDao.insertEmailTagCrossRef(EmailTagCrossRef(emailId, tagId))
    }

    suspend fun addBrowserGroupToEmail(emailId: String, groupId: Int) {
        emailDao.insertEmailBrowserGroupCrossRef(EmailBrowserGroupCrossRef(emailId, groupId))
    }

    suspend fun addUseCaseToEmail(emailId: String, useCaseId: Int) {
        emailDao.insertEmailUseCaseCrossRef(EmailUseCaseCrossRef(emailId, useCaseId))
    }

    // Search operations
    suspend fun searchEmails(query: String) = emailDao.searchEmails(query)
    suspend fun searchTags(query: String) = tagDao.searchTags(query)
    suspend fun searchBrowserGroups(query: String) = browserGroupDao.searchBrowserGroups(query)
    suspend fun searchUseCases(query: String) = useCaseDao.searchUseCases(query)

    // Lookup operations
    suspend fun getAllTags() = tagDao.getAllTags()
    suspend fun getAllBrowserGroups() = browserGroupDao.getAllBrowserGroups()
    suspend fun getAllUseCases() = useCaseDao.getAllUseCases()

    // Complex operations
    suspend fun createEmailWithRelations(
        email: Email,
        tagIds: List<Int>,
        groupIds: List<Int>,
        useCaseIds: List<Int>
    ) {
        emailDao.insertEmail(email)
        tagIds.forEach { addTagToEmail(email.email_id, it) }
        groupIds.forEach { addBrowserGroupToEmail(email.email_id, it) }
        useCaseIds.forEach { addUseCaseToEmail(email.email_id, it) }
    }
}