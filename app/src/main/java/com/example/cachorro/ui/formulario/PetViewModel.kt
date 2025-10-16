// Localização final: ui/formulario/PetViewModel.kt

package com.example.cachorro.ui.formulario

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.cachorro.data.PetRepository
import com.example.cachorro.data.local.AppDatabase
import com.example.cachorro.data.local.model.Pet
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// Classe que representa o estado da tela do formulário
data class PetFormUiState(
    val id: Int = 0,
    val nome: String = "",
    val raca: String = "",
    val sexo: String = "",
    val cidade: String = "",
    val localDescricao: String = "",
    val porte: String = "",
    val cor: String = "",
    val idade: String = "",
    val descricao: String = "",
    val temManchas: Boolean = false,
    val temOlhosDiferentes: Boolean = false,
    val imageName: String? = null,
    val termosAceitos: Boolean = false,
    val isEditing: Boolean = false
)

class PetViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PetRepository

    // StateFlow para a lista principal
    val allPets: StateFlow<List<Pet>>

    // StateFlow para o estado do formulário
    private val _formUiState = MutableStateFlow(PetFormUiState())
    val formUiState: StateFlow<PetFormUiState> = _formUiState.asStateFlow()

    init {
        val petDao = AppDatabase.getDatabase(application).petDao()
        repository = PetRepository(petDao)

        allPets = repository.getAllPets()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )
    }

    // Funções para a UI chamar quando o usuário interagir
    fun updateNome(nome: String) { _formUiState.update { it.copy(nome = nome) } }
    fun updateRaca(raca: String) { _formUiState.update { it.copy(raca = raca) } }
    fun updateSexo(sexo: String) { _formUiState.update { it.copy(sexo = sexo) } }
    fun updateCidade(cidade: String) { _formUiState.update { it.copy(cidade = cidade) } }
    fun updateLocalDescricao(desc: String) { _formUiState.update { it.copy(localDescricao = desc) } }
    fun updatePorte(porte: String) { _formUiState.update { it.copy(porte = porte) } }
    fun updateCor(cor: String) { _formUiState.update { it.copy(cor = cor) } }
    fun updateIdade(idade: String) { _formUiState.update { it.copy(idade = idade) } }
    fun updateDescricao(desc: String) { _formUiState.update { it.copy(descricao = desc) } }
    fun toggleManchas() { _formUiState.update { it.copy(temManchas = !it.temManchas) } }
    fun toggleOlhosDiferentes() { _formUiState.update { it.copy(temOlhosDiferentes = !it.temOlhosDiferentes) } }
    fun updateTermos(aceitos: Boolean) { _formUiState.update { it.copy(termosAceitos = aceitos) } }

    // Carrega um pet existente para o estado do formulário
    fun loadPetParaEdicao(petId: Int) {
        viewModelScope.launch {
            val pet = repository.getPetById(petId)
            pet?.let {
                _formUiState.value = PetFormUiState(
                    id = it.id,
                    nome = it.nome,
                    raca = it.raca,
                    sexo = it.sexo,
                    cidade = it.local.substringBefore(" -").trim(),
                    localDescricao = it.local.substringAfter(" - ", "").trim(),
                    porte = it.porte,
                    cor = it.cor,
                    idade = it.idade,
                    descricao = it.descricao,
                    temManchas = it.caracteristicas.contains("Manchas"),
                    temOlhosDiferentes = it.caracteristicas.contains("Olhos de cores diferentes"),
                    imageName = it.imageName,
                    termosAceitos = true,
                    isEditing = true
                )
            }
        }
    }

    // Limpa o formulário para um novo cadastro
    fun resetFormState() {
        _formUiState.value = PetFormUiState()
    }

    // Salva o pet (novo ou editado) no banco de dados
    fun savePet() {
        viewModelScope.launch {
            val currentState = formUiState.value
            val caracteristicas = mutableListOf<String>()
            if (currentState.temManchas) caracteristicas.add("Manchas")
            if (currentState.temOlhosDiferentes) caracteristicas.add("Olhos de cores diferentes")

            val pet = repository.criarPetComFormulario(
                id = currentState.id,
                nome = currentState.nome,
                sexo = currentState.sexo,
                raca = currentState.raca,
                cidade = currentState.cidade,
                localDescricao = currentState.localDescricao,
                porte = currentState.porte,
                cor = currentState.cor,
                idade = currentState.idade,
                descricao = currentState.descricao,
                caracteristicas = caracteristicas,
                imageName = currentState.imageName
            )
            addOrUpdatePet(pet)
        }
    }

    // Funções privadas que interagem com o repositório
    private suspend fun addOrUpdatePet(pet: Pet) { repository.addOrUpdatePet(pet) }
    fun deletePet(id: Int) { viewModelScope.launch { repository.deletePetById(id) } }
}