// Localização: ui/formulario/cadastro_pet_perdido.kt

package com.example.cachorro.ui.formulario

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cachorro.ui.componentes.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetPerdidoScreen(
    petViewModel: PetViewModel = viewModel(),
    onNavigateBack: () -> Unit,
    petId: Int? = null
) {
    val uiState by petViewModel.formUiState.collectAsState()

    LaunchedEffect(petId) {
        if (petId != null) {
            petViewModel.loadPetParaEdicao(petId)
        } else {
            petViewModel.resetFormState()
        }
    }

    val tituloTela = if (uiState.isEditing) "Editar Pet" else "Cadastrar Pet"
    val textoBotao = if (uiState.isEditing) "Salvar Alterações" else "Publicar"

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(tituloTela, fontWeight = FontWeight.Bold) },
                navigationIcon = { IconButton(onClick = onNavigateBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Voltar") } }
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            SectionHeader("Nome do Pet")
            OutlinedTextField(
                value = uiState.nome,
                onValueChange = { petViewModel.updateNome(it) },
                label = { Text("Nome") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
            )
            Spacer(Modifier.height(16.dp))

            SectionHeader("Informações básicas")
            // --- CONECTANDO O SELETOR DE TIPO ---
            SingleChoiceSegment("Tipo de pet", listOf("Cachorro", "Gato"), uiState.tipo) { petViewModel.updateTipo(it) }
            SingleChoiceSegment("Sexo", listOf("Macho", "Fêmea", "Não sei"), uiState.sexo) { petViewModel.updateSexo(it) }

            // ... O resto do arquivo continua exatamente igual ...
            // (pode copiar e colar o resto do seu arquivo aqui se preferir)

            SectionHeader("Características físicas")
            SingleChoiceSegment("Idade", listOf("Filhote", "Adulto"), uiState.idade) { petViewModel.updateIdade(it) }
            SingleChoiceSegment("Tamanho", listOf("Pequeno", "Médio", "Grande"), uiState.porte) { petViewModel.updatePorte(it) }
            OutlinedTextField(
                value = uiState.raca,
                onValueChange = { petViewModel.updateRaca(it) },
                label = { Text("Raça") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
            )
            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = uiState.cor,
                onValueChange = { petViewModel.updateCor(it) },
                label = { Text("Cores do pet") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
            )
            Spacer(Modifier.height(16.dp))

            SectionHeader("Onde o pet foi perdido")
            OutlinedTextField(
                value = uiState.cidade,
                onValueChange = { petViewModel.updateCidade(it) },
                label = { Text("Cidade") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
            )
            LargeTextField(
                value = uiState.localDescricao,
                onValueChange = { petViewModel.updateLocalDescricao(it) },
                label = "Descreva o local"
            )

            SectionHeader("Características especiais")
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(selected = uiState.temManchas, onClick = { petViewModel.toggleManchas() }, label = { Text("Manchas") })
                FilterChip(selected = uiState.temOlhosDiferentes, onClick = { petViewModel.toggleOlhosDiferentes() }, label = { Text("Olhos diferentes") })
            }
            Spacer(Modifier.height(24.dp))
            LargeTextField(
                value = uiState.descricao,
                onValueChange = { petViewModel.updateDescricao(it) },
                label = "Conte sobre particularidades do pet"
            )

            SectionHeader("Termos de uso")
            TermsAndConditionsCheckbox(checked = uiState.termosAceitos, onCheckedChange = { petViewModel.updateTermos(it) })

            Spacer(Modifier.height(24.dp))
            Button(
                onClick = {
                    petViewModel.savePet()
                    onNavigateBack()
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                enabled = uiState.termosAceitos && uiState.nome.isNotBlank() && uiState.tipo.isNotBlank()
            ) {
                Text(textoBotao, fontSize = 16.sp)
            }
            Spacer(Modifier.height(16.dp))
        }
    }
}