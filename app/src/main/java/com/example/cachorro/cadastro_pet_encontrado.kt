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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetEncontradoScreen(
    onNavigateBack: () -> Unit
) {
    // --- Estados do Formulário ---
    var tipoPet by remember { mutableStateOf("") }
    var sexo by remember { mutableStateOf("") }
    var idade by remember { mutableStateOf("") }
    var tamanho by remember { mutableStateOf("") }
    var raca by remember { mutableStateOf("") }
    var cores by remember { mutableStateOf("") }
    var cidade by remember { mutableStateOf("") }
    var localDescricao by remember { mutableStateOf("") }
    var temManchas by remember { mutableStateOf(false) }
    var temOlhosDiferentes by remember { mutableStateOf(false) }
    var particularidades by remember { mutableStateOf("") }
    var termosAceitos by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Viu um pet?", fontWeight = FontWeight.Bold) },
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
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White
                )
            )
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = cores,
                onValueChange = { cores = it },
                label = { Text("Cores do pet") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White
                )
            )
            Spacer(modifier = Modifier.height(16.dp))

            SectionHeader("Onde o pet foi visto")

            OutlinedTextField(
                value = cidade,
                onValueChange = { cidade = it },
                label = { Text("Cidade") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White
                )
            )
            LargeTextField(
                value = localDescricao,
                onValueChange = { localDescricao = it },
                label = "Descreva o local em que foi visto pela última vez"
            )

            SectionHeader("Características especiais")
            Text("O pet que você viu possui alguma dessas características?", style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FilterChip(
                    selected = temManchas,
                    onClick = { temManchas = !temManchas },
                    label = { Text("Manchas") }
                )
                FilterChip(
                    selected = temOlhosDiferentes,
                    onClick = { temOlhosDiferentes = !temOlhosDiferentes },
                    label = { Text("Olhos de cores diferentes") }
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            LargeTextField(
                value = particularidades,
                onValueChange = { particularidades = it },
                label = "Conte sobre particularidades do pet (comportamento, coleira, etc.)"
            )

            SectionHeader("Fotos do pet que você viu")
            Text(
                text = "Para que seu anúncio tenha melhor alcance e siga as regras da plataforma, envie apenas fotos recentes e bem iluminadas do pet do anúncio. É proibido incluir pessoas ou outros animais e, se necessário, edite a imagem para focar no rosto e corpo do pet, facilitando sua identificação.",
                style = MaterialTheme.typography.bodySmall,
                lineHeight = 18.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            WarningBox()
            Spacer(modifier = Modifier.height(16.dp))
            AddPhotoButton()

            SectionHeader("Termos de uso")
            TermsAndConditionsCheckbox(
                checked = termosAceitos,
                onCheckedChange = { termosAceitos = it }
            )



            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = {
                    val caracteristicas = mutableListOf<String>()
                    if (temManchas) caracteristicas.add("Manchas")
                    if (temOlhosDiferentes) caracteristicas.add("Olhos de cores diferentes")

                    val novoPet = PetRepository.criarPetComFormulario(
                        nome = "Vi seu Pet",
                        sexo = sexo,
                        raca = raca,
                        cidade = cidade,
                        localDescricao = localDescricao,
                        porte = tamanho,
                        cor = cores,
                        idade = idade,
                        descricao = particularidades,
                        caracteristicas = caracteristicas
                    )
                    PetRepository.addPet(novoPet)

                    onNavigateBack()
                },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = DarkBlue)
            ) {
                Text("Publicar Avistamento", fontSize = 16.sp)
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PetEncontradoScreenPreview() {
    MaterialTheme {
        PetEncontradoScreen(onNavigateBack = {})
    }
}