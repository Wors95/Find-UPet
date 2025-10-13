

package com.example.cachorro

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PetViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PetRepository
    val allPets: StateFlow<List<Pet>>

    init {
        val petDao = AppDatabase.getDatabase(application).petDao()
        repository = PetRepository(petDao)

        allPets = repository.getAllPets()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList<Pet>()
            )
    }

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