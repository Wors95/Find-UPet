

package com.example.cachorro

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetPerdidoScreen(
    petViewModel: PetViewModel = viewModel(),
    onNavigateBack: () -> Unit,
    petId: Int? = null
) {
    val isEditing = petId != null
    var petExistente by remember { mutableStateOf<Pet?>(null) }

    LaunchedEffect(petId) {
        if (isEditing) {
            petViewModel.getPetById(petId!!) { pet ->
                petExistente = pet
            }
        }
    }

    var nomePet by remember(petExistente) { mutableStateOf(petExistente?.nome ?: "") }
    var tipoPet by remember { mutableStateOf("") }
    var sexo by remember(petExistente) { mutableStateOf(petExistente?.sexo ?: "") }
    var idade by remember(petExistente) { mutableStateOf(petExistente?.idade ?: "") }
    var tamanho by remember(petExistente) { mutableStateOf(petExistente?.porte ?: "") }
    var raca by remember(petExistente) { mutableStateOf(petExistente?.raca ?: "") }
    var cores by remember(petExistente) { mutableStateOf(petExistente?.cor ?: "") }
    var cidade by remember(petExistente) { mutableStateOf(petExistente?.local?.substringBefore(" -") ?: "") }
    var localDescricao by remember(petExistente) { mutableStateOf(petExistente?.local?.substringAfter(" - ", "") ?: "") }
    var temManchas by remember(petExistente) { mutableStateOf(petExistente?.caracteristicas?.contains("Manchas") ?: false) }
    var temOlhosDiferentes by remember(petExistente) { mutableStateOf(petExistente?.caracteristicas?.contains("Olhos de cores diferentes") ?: false) }
    var particularidades by remember(petExistente) { mutableStateOf(petExistente?.descricao ?: "") }
    var termosAceitos by remember { mutableStateOf(true) }

    val tituloTela = if (isEditing) "Editar Pet" else "Cadastrar Pet"
    val textoBotao = if (isEditing) "Salvar Alterações" else "Publicar"

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(tituloTela, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                },
                actions = {
                    IconButton(onClick = { /* TODO: Mostrar ajuda */ }) {
                        Icon(Icons.AutoMirrored.Filled.HelpOutline, contentDescription = "Ajuda")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFF7F7F7))
            )
        },
        containerColor = Color(0xFFF7F7F7)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            SectionHeader("Nome do Pet")
            OutlinedTextField(
                value = nomePet,
                onValueChange = { nomePet = it },
                label = { Text("Nome") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
            )
            Spacer(modifier = Modifier.height(16.dp))

            SectionHeader("Informações básicas")
            SingleChoiceSegment("Tipo de pet", listOf("Cachorro", "Gato"), tipoPet) { tipoPet = it }
            SingleChoiceSegment("Sexo", listOf("Macho", "Fêmea", "Não sei"), sexo) { sexo = it }

            SectionHeader("Características físicas")
            SingleChoiceSegment("Idade aproximada", listOf("Filhote", "Adulto"), idade) { idade = it }
            SingleChoiceSegment("Tamanho", listOf("Pequeno", "Médio", "Grande"), tamanho) { tamanho = it }
            OutlinedTextField(
                value = raca,
                onValueChange = { raca = it },
                label = { Text("Raça") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = cores,
                onValueChange = { cores = it },
                label = { Text("Cores do pet") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
            )
            Spacer(modifier = Modifier.height(16.dp))

            SectionHeader("Onde o pet foi perdido")
            OutlinedTextField(
                value = cidade,
                onValueChange = { cidade = it },
                label = { Text("Cidade") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
            )
            LargeTextField(
                value = localDescricao,
                onValueChange = { localDescricao = it },
                label = "Descreva o local"
            )

            SectionHeader("Características especiais")
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(selected = temManchas, onClick = { temManchas = !temManchas }, label = { Text("Manchas") })
                FilterChip(selected = temOlhosDiferentes, onClick = { temOlhosDiferentes = !temOlhosDiferentes }, label = { Text("Olhos diferentes") })
            }
            Spacer(modifier = Modifier.height(24.dp))
            LargeTextField(
                value = particularidades,
                onValueChange = { particularidades = it },
                label = "Conte sobre particularidades do pet"
            )

            SectionHeader("Termos de uso")
            TermsAndConditionsCheckbox(checked = termosAceitos, onCheckedChange = { termosAceitos = it })

            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = {
                    val caracteristicas = mutableListOf<String>()
                    if (temManchas) caracteristicas.add("Manchas")
                    if (temOlhosDiferentes) caracteristicas.add("Olhos de cores diferentes")

                    petViewModel.criarPetComFormulario(
                        id = petId ?: 0,
                        nome = nomePet,
                        sexo = sexo,
                        raca = raca,
                        cidade = cidade,
                        localDescricao = localDescricao,
                        porte = tamanho,
                        cor = cores,
                        idade = idade,
                        descricao = particularidades,
                        caracteristicas = caracteristicas,
                        imageName = petExistente?.imageName
                    )
                    onNavigateBack()
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                enabled = termosAceitos && nomePet.isNotBlank()
            ) {
                Text(textoBotao, fontSize = 16.sp)
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}