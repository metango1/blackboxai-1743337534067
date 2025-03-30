package com.example.emailmanager.db.daos

import androidx.room.*
import com.example.emailmanager.db.entities.*

@Dao
interface TagDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTag(tag: Tag): Long

    @Update
    suspend fun updateTag(tag: Tag)

    @Delete
    suspend fun deleteTag(tag: Tag)

    @Query("SELECT * FROM tags WHERE tag_id = :tagId")
    suspend fun getTagById(tagId: Int): Tag?

    @Query("SELECT * FROM tags WHERE tag_name LIKE '%' || :query || '%'")
    suspend fun searchTags(query: String): List<Tag>

    @Query("SELECT * FROM tags")
    suspend fun getAllTags(): List<Tag>
}

@Dao
interface BrowserGroupDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertBrowserGroup(group: BrowserGroup): Long

    @Update
    suspend fun updateBrowserGroup(group: BrowserGroup)

    @Delete
    suspend fun deleteBrowserGroup(group: BrowserGroup)

    @Query("SELECT * FROM browser_groups WHERE browser_group_id = :groupId")
    suspend fun getBrowserGroupById(groupId: Int): BrowserGroup?

    @Query("SELECT * FROM browser_groups WHERE browser_group_name LIKE '%' || :query || '%'")
    suspend fun searchBrowserGroups(query: String): List<BrowserGroup>

    @Query("SELECT * FROM browser_groups")
    suspend fun getAllBrowserGroups(): List<BrowserGroup>
}

@Dao
interface UseCaseDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUseCase(useCase: UseCase): Long

    @Update
    suspend fun updateUseCase(useCase: UseCase)

    @Delete
    suspend fun deleteUseCase(useCase: UseCase)

    @Query("SELECT * FROM usecases WHERE usecase_id = :useCaseId")
    suspend fun getUseCaseById(useCaseId: Int): UseCase?

    @Query("SELECT * FROM usecases WHERE usecase_name LIKE '%' || :query || '%'")
    suspend fun searchUseCases(query: String): List<UseCase>

    @Query("SELECT * FROM usecases")
    suspend fun getAllUseCases(): List<UseCase>
}