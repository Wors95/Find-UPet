package com.example.cachorro

import androidx.compose.runtime.mutableStateListOf
import kotlin.random.Random

// Usamos um 'object' para garantir que haverá apenas uma instância deste repositório
// em todo o app (padrão Singleton).
object PetRepository {

    // A lista de pets inicial.
    // Usamos `mutableStateListOf` para que o Jetpack Compose observe automaticamente
    // quando um novo pet for adicionado e atualize a tela principal.
    private val petList = mutableStateListOf(
        Pet(1, "Vi seu Pet", "Shih Tzu", "Macho", "Londrina - Ouro Verde", "Agora mesmo", R.drawable.pet1),
        Pet(2, "Vi seu Pet", "Sem Raça Definida (SRD)", "Fêmea", "São José dos Pinhais", "Agora mesmo", R.drawable.pet2),
        Pet(3, "Fiona", "Sem Raça Definida (SRD)", "Fêmea", "Piraquara", "Agora mesmo", R.drawable.pet3),
        Pet(4, "Mima", "Sem Raça Definida (SRD)", "Fêmea", "Francisco Beltrão - Vila Nova", "Agora mesmo", R.drawable.pet4)
    )

    // Função para qualquer tela buscar a lista de pets.
    fun getPets(): List<Pet> {
        return petList
    }

    // Função para as telas de cadastro adicionarem um novo pet.
    fun addPet(pet: Pet) {
        petList.add(0, pet) // Adiciona o novo pet no início da lista
    }

    // Função auxiliar para gerar um pet com dados do formulário
    fun criarPetComFormulario(
        nome: String,
        tipoPet: String, // Cachorro ou Gato
        sexo: String,
        tamanho: String,
        raca: String,
        cidade: String,
        localDescricao: String
    ): Pet {
        // Como não temos upload de imagem ainda, vamos sortear uma das existentes.
        val randomImage = listOf(R.drawable.pet1, R.drawable.pet2, R.drawable.pet3, R.drawable.pet4).random()
        // O ID precisa ser único, uma forma simples é usar o tempo atual.
        val novoId = System.currentTimeMillis().toInt()

        val localCompleto = "$cidade - $localDescricao".take(40)

        return Pet(
            id = novoId,
            nome = nome,
            raca = raca, // A raça ainda não está no formulário, então usamos um placeholder
            sexo = sexo,
            local = localCompleto,
            tempo = "Agora mesmo",
            imageResId = randomImage
        )
    }
}