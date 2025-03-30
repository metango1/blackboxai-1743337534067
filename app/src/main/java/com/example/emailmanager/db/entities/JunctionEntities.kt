package com.example.emailmanager.db.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import java.util.Date

@Entity(
    tableName = "email_tags",
    primaryKeys = ["email_id", "tag_id"],
    foreignKeys = [
        ForeignKey(
            entity = Email::class,
            parentColumns = ["email_id"],
            childColumns = ["email_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Tag::class,
            parentColumns = ["tag_id"],
            childColumns = ["tag_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class EmailTagCrossRef(
    val email_id: String,
    val tag_id: Int,
    val created_at: Date = Date()
)

@Entity(
    tableName = "email_browser_groups",
    primaryKeys = ["email_id", "browser_group_id"],
    foreignKeys = [
        ForeignKey(
            entity = Email::class,
            parentColumns = ["email_id"],
            childColumns = ["email_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = BrowserGroup::class,
            parentColumns = ["browser_group_id"],
            childColumns = ["browser_group_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class EmailBrowserGroupCrossRef(
    val email_id: String,
    val browser_group_id: Int,
    val created_at: Date = Date()
)

@Entity(
    tableName = "email_usecases",
    primaryKeys = ["email_id", "usecase_id"],
    foreignKeys = [
        ForeignKey(
            entity = Email::class,
            parentColumns = ["email_id"],
            childColumns = ["email_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = UseCase::class,
            parentColumns = ["usecase_id"],
            childColumns = ["usecase_id"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class EmailUseCaseCrossRef(
    val email_id: String,
    val usecase_id: Int,
    val created_at: Date = Date()
)