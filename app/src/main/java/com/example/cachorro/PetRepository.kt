// anote/cole em: PetRepository.kt

package com.example.cachorro

// --- MUDANÇA SIGNIFICATIVA ---
// O repositório agora depende do DAO para acessar o banco de dados.
class PetRepository(private val petDao: PetDao) {

    fun getAllPets() = petDao.getAllPets()

    suspend fun getPetById(id: Int) = petDao.getPetById(id)

    suspend fun addOrUpdatePet(pet: Pet) = petDao.upsertPet(pet)

    suspend fun deletePetById(id: Int) {
        val pet = petDao.getPetById(id)
        pet?.let {
            petDao.deletePet(it)
        }
    }

    // A função de criar o pet agora retorna o objeto Pet
    // para ser inserido pelo ViewModel.
    fun criarPetComFormulario(
        id: Int = 0, // Incluímos o ID para o caso de edição
        nome: String,
        sexo: String,
        raca: String,
        cidade: String,
        localDescricao: String,
        porte: String,
        cor: String,
        idade: String,
        descricao: String,
        caracteristicas: List<String>,
        imageName: String? = null // Para manter a imagem ao editar
    ): Pet {
        val randomImageName = listOf("pet1", "pet2", "pet3", "pet4").random()
        val localCompleto = "$cidade - $localDescricao".take(40)

        return Pet(
            id = id,
            nome = nome,
            raca = raca,
            sexo = sexo,
            local = localCompleto,
            porte = porte,
            cor = cor,
            idade = idade,
            descricao = descricao,
            caracteristicas = caracteristicas,
            imageName = imageName ?: randomImageName, // Usa a imagem existente ou uma nova
            createdAt = System.currentTimeMillis()
        )
    }
}