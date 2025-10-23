package com.example.cachorro.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "comments")
data class Comment(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val petId: Int,
    val text: String,
    val author: String = "Anônimo",
    val createdAt: Long = System.currentTimeMillis()
)

