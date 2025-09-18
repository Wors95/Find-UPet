package com.example.cachorro

import androidx.compose.runtime.mutableStateListOf

object PetRepository {

    private val petList = mutableStateListOf(
        Pet(
            id = 1,
            nome = "Milo",
            raca = "Shih Tzu",
            sexo = "Macho",
            local = "Londrina - Ouro Verde",
            tempo = "Há 2 horas",
            imageResId = R.drawable.pet1,
            porte = "Pequeno",
            cor = "Branco e Caramelo",
            idade = "Filhote",
            descricao = "Ele fugiu durante a queima de fogos. Estava com uma coleira azul e é muito dócil.",
            caracteristicas = listOf("Manchas")
        ),
        Pet(
            id = 2,
            nome = "Vi seu Pet",
            raca = "Sem Raça Definida (SRD)",
            sexo = "Fêmea",
            local = "São José dos Pinhais",
            tempo = "Agora mesmo",
            imageResId = R.drawable.pet2,
            porte = "Médio",
            cor = "Preto",
            idade = "Adulto",
            descricao = "Vi este pet próximo ao parque. Parecia perdido e um pouco assustado.",
            caracteristicas = emptyList()
        ),
        Pet(
            id = 3,
            nome = "Fiona",
            raca = "Sem Raça Definida (SRD)",
            sexo = "Fêmea",
            local = "Piraquara",
            tempo = "Há 1 dia",
            imageResId = R.drawable.pet3,
            porte = "Pequeno",
            cor = "Marrom",
            idade = "Adulto",
            descricao = "Minha gatinha sumiu de casa. Ela é medrosa e não costuma sair. Atende por Fiona.",
            caracteristicas = listOf("Olhos de cores diferentes")
        ),
        Pet(
            id = 4,
            nome = "Kiara",
            raca = "Sem Raça Definida (SRD)",
            sexo = "Fêmea",
            local = "Ponta Grossa/PR",
            tempo = "Há 7 minutos",
            imageResId = R.drawable.pet4,
            porte = "Grande",
            cor = "Caramelo, Branco",
            idade = "Adulto",
            descricao = "Ela fugiu as 19 horas no costa rica 3 ontem dia 17/09.",
            caracteristicas = listOf("Olhos de cores diferentes")
        )
    )

    fun getPets(): List<Pet> {
        return petList
    }

    fun getPetById(id: Int): Pet? {
        return petList.find { it.id == id }
    }

    fun addPet(pet: Pet) {
        petList.add(0, pet)
    }

    // Função auxiliar para gerar um pet com dados do formulário
    fun criarPetComFormulario(
        nome: String,
        sexo: String,
        raca: String,
        cidade: String,
        localDescricao: String,
        porte: String,
        cor: String,
        idade: String,
        descricao: String,
        caracteristicas: List<String>
    ): Pet {
        // Como não temos upload de imagem ainda, vamos sortear uma das existentes.
        val randomImage = listOf(R.drawable.pet1, R.drawable.pet2, R.drawable.pet3, R.drawable.pet4).random()
        // O ID precisa ser único, uma forma simples é usar o tempo atual.
        val novoId = System.currentTimeMillis().toInt()

        val localCompleto = "$cidade - $localDescricao".take(40)

        return Pet(
            id = novoId,
            nome = nome,
            raca = raca,
            sexo = sexo,
            local = localCompleto,
            tempo = "Agora mesmo",
            imageResId = randomImage,
            porte = porte,
            cor = cor,
            idade = idade,
            descricao = descricao,
            caracteristicas = caracteristicas
        )
    }
}