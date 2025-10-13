// anote/cole em: cadastro_pet_perdido.kt

package com.example.cachorro

import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
// ... (importe todos os outros componentes necessários)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetPerdidoScreen(
    petViewModel: PetViewModel = viewModel(),
    onNavigateBack: () -> Unit,
    petId: Int? = null
) {
    val isEditing = petId != null
    var petExistente by remember { mutableStateOf<Pet?>(null) }

    // --- MUDANÇA SIGNIFICATIVA ---
    // Usamos LaunchedEffect para carregar os dados do pet do banco de dados
    // apenas uma vez quando a tela é aberta no modo de edição.
    LaunchedEffect(petId) {
        if (isEditing) {
            petViewModel.getPetById(petId!!) { pet ->
                petExistente = pet
            }
        }
    }

    // Estados do formulário. Eles são preenchidos quando 'petExistente' é carregado.
    var nomePet by remember(petExistente) { mutableStateOf(petExistente?.nome ?: "") }
    // ... (defina todos os outros estados da mesma forma, como antes)
    var raca by remember(petExistente) { mutableStateOf(petExistente?.raca ?: "") }
    // ... etc.

    val tituloTela = if (isEditing) "Editar Pet" else "Cadastrar Pet"
    val textoBotao = if (isEditing) "Salvar Alterações" else "Publicar"

    Scaffold( /* ... */ ) { paddingValues ->
        Column( /* ... */ ) {
            // ... (toda a estrutura do formulário continua a mesma)

            Button(
                onClick = {
                    val caracteristicas = mutableListOf<String>()
                    // ... (lógica para preencher as características)

                    petViewModel.criarPetComFormulario(
                        id = petId ?: 0,
                        nome = nomePet,
                        raca = raca,
                        // ... passe todos os outros campos do formulário ...
                        imageName = petExistente?.imageName // Mantém a imagem antiga se estiver editando
                    )
                    onNavigateBack()
                },
                // ...
            ) {
                Text(textoBotao, fontSize = 16.sp)
            }
            // ...
        }
    }
}