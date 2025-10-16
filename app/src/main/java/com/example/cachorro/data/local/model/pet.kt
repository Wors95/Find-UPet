package com.example.cachorro.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pets")
data class Pet(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nome: String,
    val raca: String,
    val sexo: String,
    val local: String,
    val porte: String,
    val cor: String,
    val idade: String,
    val descricao: String,
    val caracteristicas: List<String>,
    val imageName: String,
    val createdAt: Long = System.currentTimeMillis()
)