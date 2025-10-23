// Localização: data/PetRepository.kt

package com.example.cachorro.data

import com.example.cachorro.data.local.CommentDao
import com.example.cachorro.data.local.PetDao
import com.example.cachorro.data.local.model.Comment
import com.example.cachorro.data.local.model.Pet

class PetRepository(
    private val petDao: PetDao,
    private val commentDao: CommentDao
) {

    // Funções de Pet (sem alteração)
    fun getAllPets() = petDao.getAllPets()
    suspend fun getPetById(id: Int) = petDao.getPetById(id)
    suspend fun addOrUpdatePet(pet: Pet) = petDao.upsertPet(pet)
    suspend fun deletePetById(id: Int) {
        val pet = petDao.getPetById(id)
        pet?.let { petDao.deletePet(it) }
    }

    // Funções de Comentário
    fun getCommentsForPet(petId: Int) = commentDao.getCommentsForPet(petId)
    suspend fun addComment(comment: Comment) = commentDao.insertComment(comment)

    // --- NOVA FUNÇÃO ADICIONADA ---
    suspend fun deleteComment(comment: Comment) {
        commentDao.deleteComment(comment)
    }


    fun criarPetComFormulario(
        id: Int = 0,
        nome: String,
        tipo: String,
        sexo: String,
        raca: String,
        cidade: String,
        localDescricao: String,
        porte: String,
        cor: String,
        idade: String,
        descricao: String,
        caracteristicas: List<String>,
        imageName: String?
    ): Pet {
        val randomImageName = listOf("pet1", "pet2", "pet3", "pet4").random()
        val localCompleto = "$cidade - $localDescricao".take(40)

        return Pet(
            id = id,
            nome = nome,
            tipo = tipo,
            raca = raca,
            sexo = sexo,
            local = localCompleto,
            porte = porte,
            cor = cor,
            idade = idade,
            descricao = descricao,
            caracteristicas = caracteristicas,
            imageName = imageName ?: randomImageName,
            createdAt = System.currentTimeMillis()
        )
    }
}