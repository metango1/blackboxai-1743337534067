package com.example.emailmanager.data

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject

class NewEmailRepository @Inject constructor(
    private val emailDao: EmailDao,
    private val tagDao: TagDao,
    private val browserGroupDao: BrowserGroupDao,
    private val useCaseDao: UseCaseDao,
    private val context: Context
) {
    // Existing methods...

    fun backupDatabase(file: File): Boolean {
        return try {
            val dbFile = context.getDatabasePath(EmailDatabase.DATABASE_NAME)
            val inputStream: InputStream = dbFile.inputStream()
            val outputStream: OutputStream = FileOutputStream(file)

            inputStream.copyTo(outputStream)
            outputStream.close()
            inputStream.close()
            true
        } catch (e: SQLiteException) {
            false
        }
    }

    fun restoreDatabase(file: File): Boolean {
        return try {
            val dbFile = context.getDatabasePath(EmailDatabase.DATABASE_NAME)
            file.inputStream().use { inputStream ->
                FileOutputStream(dbFile).use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
            }
            true
        } catch (e: Exception) {
            false
        }
    }
}