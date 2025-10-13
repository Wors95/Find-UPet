// anote/cole em: pet.kt

package com.example.cachorro

import androidx.room.Entity
import androidx.room.PrimaryKey

// --- MUDANÇA SIGNIFICATIVA ---
// Agora esta classe representa uma tabela no banco de dados.
@Entity(tableName = "pets")
data class Pet(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0, // O ID agora é gerado pelo banco de dados
    val nome: String,
    val raca: String,
    val sexo: String,
    val local: String,
    val porte: String,
    val cor: String,
    val idade: String,
    val descricao: String,
    val caracteristicas: List<String>,

    // Não salvamos o resource ID, mas o nome da imagem. É mais flexível.
    val imageName: String,

    // É uma boa prática salvar quando o registro foi criado.
    val createdAt: Long = System.currentTimeMillis()
)