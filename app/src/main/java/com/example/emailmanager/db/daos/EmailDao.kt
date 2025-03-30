package com.example.emailmanager.db.daos

import androidx.room.*
import com.example.emailmanager.db.entities.*

@Dao
interface EmailDao {
    // Basic CRUD operations
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEmail(email: Email)

    @Update
    suspend fun updateEmail(email: Email)

    @Delete
    suspend fun deleteEmail(email: Email)

    @Query("SELECT * FROM emails WHERE email_id = :emailId")
    suspend fun getEmailById(emailId: String): Email?

    // Relationship operations
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertEmailTagCrossRef(crossRef: EmailTagCrossRef)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertEmailBrowserGroupCrossRef(crossRef: EmailBrowserGroupCrossRef)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertEmailUseCaseCrossRef(crossRef: EmailUseCaseCrossRef)

    // Complex queries
    @Transaction
    @Query("SELECT * FROM emails")
    suspend fun getAllEmailsWithRelations(): List<EmailWithRelations>

    @Transaction
    @Query("""
        SELECT * FROM emails e
        WHERE e.first_name LIKE '%' || :query || '%' 
        OR e.last_name LIKE '%' || :query || '%'
        OR e.email_id LIKE '%' || :query || '%'
        OR e.tab_group LIKE '%' || :query || '%'
    """)
    suspend fun searchEmails(query: String): List<Email>
}