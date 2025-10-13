// --- NOVO ARQUIVO ---
// anote/cole em: PetViewModel.kt

package com.example.cachorro

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

// Usamos AndroidViewModel para ter acesso ao Contexto do aplicativo.
class PetViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PetRepository

    init {
        val petDao = AppDatabase.getDatabase(application).petDao()
        repository = PetRepository(petDao)
    }

    // Pega a lista de pets do repositório e a expõe como um StateFlow.
    // A UI irá observar isso para se atualizar automaticamente.
    val allPets = repository.getAllPets()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun getPetById(id: Int, onResult: (Pet?) -> Unit) {
        viewModelScope.launch {
            val pet = repository.getPetById(id)
            onResult(pet)
        }
    }

    fun addOrUpdatePet(pet: Pet) {
        viewModelScope.launch {
            repository.addOrUpdatePet(pet)
        }
    }

    fun deletePet(id: Int) {
        viewModelScope.launch {
            repository.deletePetById(id)
        }
    }

    // A lógica de criar o objeto Pet agora fica dentro do ViewModel
    fun criarPetComFormulario(
        id: Int = 0,
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
        imageName: String? = null
    ) {
        val pet = repository.criarPetComFormulario(
            id, nome, sexo, raca, cidade, localDescricao, porte, cor, idade, descricao, caracteristicas, imageName
        )
        addOrUpdatePet(pet)
    }
}