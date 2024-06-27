package com.example.lekhantu.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.lekhantu.model.NoteEntity

@Database(entities = [NoteEntity::class], version = 1, exportSchema = false)
abstract class NoteDatabase : RoomDatabase() {

    abstract fun noteDao(): NoteDao

    companion object {
        @Volatile
        private var instance: NoteDatabase? = null
        private val LOCK = Any()
        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also {
                instance = it
            }
        }

        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                NoteDatabase::class.java,
                "note_database"
            ).build()
    }

}