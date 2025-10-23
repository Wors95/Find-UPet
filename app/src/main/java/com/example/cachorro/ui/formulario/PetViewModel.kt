// Localização: ui/formulario/PetViewModel.kt

package com.example.cachorro.ui.formulario

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.cachorro.data.PetRepository
import com.example.cachorro.data.local.AppDatabase
import com.example.cachorro.data.local.model.Comment
import com.example.cachorro.data.local.model.Pet
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

// PetFormUiState continua igual...
data class PetFormUiState(
    val id: Int = 0, val nome: String = "", val tipo: String = "", val raca: String = "",
    val sexo: String = "", val cidade: String = "", val localDescricao: String = "",
    val porte: String = "", val cor: String = "", val idade: String = "", val descricao: String = "",
    val temManchas: Boolean = false, val temOlhosDiferentes: Boolean = false, val imageName: String? = null,
    val termosAceitos: Boolean = false, val isEditing: Boolean = false
)

class PetViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PetRepository
    private val _allPets: StateFlow<List<Pet>>
    private val _filtroSelecionado = MutableStateFlow("Todos")
    val filtroSelecionado: StateFlow<String> = _filtroSelecionado.asStateFlow()
    private val _textoBusca = MutableStateFlow("")
    val textoBusca: StateFlow<String> = _textoBusca.asStateFlow()
    val petsFiltrados: StateFlow<List<Pet>>
    private val _formUiState = MutableStateFlow(PetFormUiState())
    val formUiState: StateFlow<PetFormUiState> = _formUiState.asStateFlow()
    private val _comments = MutableStateFlow<List<Comment>>(emptyList())
    val comments: StateFlow<List<Comment>> = _comments.asStateFlow()

    init {
        val db = AppDatabase.getDatabase(application)
        repository = PetRepository(db.petDao(), db.commentDao())
        _allPets = repository.getAllPets()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList<Pet>()
            )

        petsFiltrados = combine(_allPets, _filtroSelecionado, _textoBusca) { pets, filtro, busca ->
            val petsPorTipo = when (filtro) {
                "Todos" -> pets
                "Cachorro" -> pets.filter { it.tipo == "Cachorro" }
                "Gato" -> pets.filter { it.tipo == "Gato" }
                else -> pets
            }
            if (busca.isBlank()) {
                petsPorTipo
            } else {
                petsPorTipo.filter { pet ->
                    pet.nome.contains(busca, ignoreCase = true) ||
                            pet.raca.contains(busca, ignoreCase = true) ||
                            pet.local.contains(busca, ignoreCase = true)
                }
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )
    }

    // --- Funções de Comentários ---
    fun loadComments(petId: Int) {
        viewModelScope.launch {
            repository.getCommentsForPet(petId).collect { commentList ->
                _comments.value = commentList
            }
        }
    }

    fun addComment(petId: Int, text: String) {
        if (text.isBlank()) return
        viewModelScope.launch {
            val newComment = Comment(petId = petId, text = text)
            repository.addComment(newComment)
        }
    }

    // --- NOVA FUNÇÃO ADICIONADA ---
    fun deleteComment(comment: Comment) {
        viewModelScope.launch {
            repository.deleteComment(comment)
        }
    }


    fun atualizarFiltro(novoFiltro: String) { _filtroSelecionado.value = novoFiltro }
    fun atualizarTextoBusca(novoTexto: String) { _textoBusca.value = novoTexto }
    fun updateNome(nome: String) { _formUiState.update { it.copy(nome = nome) } }
    fun updateTipo(tipo: String) { _formUiState.update { it.copy(tipo = tipo) } }
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

    fun loadPetParaEdicao(petId: Int) {
        viewModelScope.launch {
            val pet = repository.getPetById(petId)
            pet?.let {
                _formUiState.value = PetFormUiState(
                    id = it.id, nome = it.nome, tipo = it.tipo, raca = it.raca, sexo = it.sexo,
                    cidade = it.local.substringBefore(" -").trim(),
                    localDescricao = it.local.substringAfter(" - ", "").trim(),
                    porte = it.porte, cor = it.cor, idade = it.idade, descricao = it.descricao,
                    temManchas = it.caracteristicas.contains("Manchas"),
                    temOlhosDiferentes = it.caracteristicas.contains("Olhos de cores diferentes"),
                    imageName = it.imageName,
                    termosAceitos = true, isEditing = true
                )
            }
        }
    }

    fun resetFormState() { _formUiState.value = PetFormUiState() }

    fun savePet() {
        viewModelScope.launch {
            val currentState = formUiState.value
            val caracteristicas = mutableListOf<String>()
            if (currentState.temManchas) caracteristicas.add("Manchas")
            if (currentState.temOlhosDiferentes) caracteristicas.add("Olhos de cores diferentes")
            val pet = repository.criarPetComFormulario(
                id = currentState.id, nome = currentState.nome, tipo = currentState.tipo, sexo = currentState.sexo, raca = currentState.raca,
                cidade = currentState.cidade, localDescricao = currentState.localDescricao, porte = currentState.porte, cor = currentState.cor,
                idade = currentState.idade, descricao = currentState.descricao, caracteristicas = caracteristicas,
                imageName = currentState.imageName
            )
            addOrUpdatePet(pet)
        }
    }

    private suspend fun addOrUpdatePet(pet: Pet) { repository.addOrUpdatePet(pet) }
    fun deletePet(id: Int) { viewModelScope.launch { repository.deletePetById(id) } }
}