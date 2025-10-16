// Localização: data/local/AppDatabase.kt

package com.example.cachorro.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.cachorro.data.local.model.Pet

@Database(entities = [Pet::class], version = 2) // <-- VERSÃO ATUALIZADA
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun petDao(): PetDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "pet_database"
                )
                    .fallbackToDestructiveMigration() // <-- LINHA ADICIONADA
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}