package com.example.cachorro



// Esta classe representa o modelo de dados para um pet.
data class Pet(
    val id: Int,
    val nome: String,
    val raca: String,
    val sexo: String,
    val local: String,
    val tempo: String,
    val imageResId: Int // Usando Int para recursos drawable. Em um app real, seria uma String de URL.
)