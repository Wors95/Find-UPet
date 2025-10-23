package com.example.cachorro.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.cachorro.data.local.model.Comment
import kotlinx.coroutines.flow.Flow

@Dao
interface CommentDao {
    @Upsert
    suspend fun insertComment(comment: Comment)

    @Delete
    suspend fun deleteComment(comment: Comment)

    @Query("SELECT * FROM comments WHERE petId = :petId ORDER BY createdAt ASC")
    fun getCommentsForPet(petId: Int): Flow<List<Comment>>
}