// Localização: data/local/PetDao.kt

package com.example.cachorro.data.local // <-- A linha mais importante!

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.cachorro.data.local.model.Pet
import kotlinx.coroutines.flow.Flow

@Dao
interface PetDao {
    @Upsert
    suspend fun upsertPet(pet: Pet)

    @Delete
    suspend fun deletePet(pet: Pet)

    @Query("SELECT * FROM pets WHERE id = :id")
    suspend fun getPetById(id: Int): Pet?

    @Query("SELECT * FROM pets ORDER BY createdAt DESC")
    fun getAllPets(): Flow<List<Pet>>
}