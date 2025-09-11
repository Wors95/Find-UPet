package com.example.cachorro // Substitua pelo seu nome de pacote

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.HelpOutline
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// --- Definição de Cores ---
val DarkBlues = Color(0xFF2A3F6F)
val LightGrayForm = Color(0xFFF0F0F0) // Renomeado para não conflitar
val LightYellow = Color(0xFFFFF9C4)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetPerdidoScreen(
    onNavigateBack: () -> Unit
) {
    // --- Estados do Formulário ---
    var tipoPet by remember { mutableStateOf("") }
    var sexo by remember { mutableStateOf("") }
    var idade by remember { mutableStateOf("") }
    var tamanho by remember { mutableStateOf("") }
    var raca by remember { mutableStateOf("") }
    var cidade by remember { mutableStateOf("") }
    var localDescricao by remember { mutableStateOf("") }
    var temManchas by remember { mutableStateOf(false) }
    var temOlhosDiferentes by remember { mutableStateOf(false) }
    var particularidades by remember { mutableStateOf("") }
    var termosAceitos by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pet perdido?", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) { // Ação de navegação aqui
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
            SimpleDropdownField(label = "Raça")
            SimpleClickableField(label = "Selecione as cores")

            SectionHeader("Onde seu pet foi perdido")
            SimpleDropdownField(label = "Cidade")
            LargeTextField(
                value = localDescricao,
                onValueChange = { localDescricao = it },
                label = "Descreva o local em que foi visto pela última vez"
            )

            SectionHeader("Características especiais")
            Text("Seu pet possui alguma dessas características?", style = MaterialTheme.typography.bodyMedium)
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
                label = "Conte sobre particularidades do seu pet (comportamento, hábitos, etc.)"
            )

            SectionHeader("Fotos do seu pet")
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
                onClick = { /* TODO: Lógica de envio do formulário */ },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = DarkBlues)
            ) {
                Text("Vamos encontrar juntos", fontSize = 16.sp)
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}


// --- Componentes do Formulário ---

@Composable
private fun SectionHeader(title: String) {
    Column {
        Spacer(modifier = Modifier.height(24.dp))
        Text(text = title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun SingleChoiceSegment(
    title: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(title, fontWeight = FontWeight.SemiBold, style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            options.forEach { option ->
                val isSelected = selectedOption == option
                Button(
                    onClick = { onOptionSelected(option) },
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isSelected) Color.DarkGray else LightGrayForm,
                        contentColor = if (isSelected) Color.White else Color.Black
                    ),
                    elevation = ButtonDefaults.buttonElevation(0.dp)
                ) {
                    Text(option)
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun SimpleDropdownField(label: String) {
    OutlinedTextField(
        value = "", onValueChange = {}, readOnly = true,
        label = { Text(label) },
        trailingIcon = { Icon(Icons.Default.ArrowDropDown, contentDescription = null) },
        modifier = Modifier.fillMaxWidth().clickable { /* TODO: Abrir dropdown */ },
        shape = RoundedCornerShape(8.dp),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedContainerColor = Color.White,
            focusedContainerColor = Color.White
        )
    )
    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
fun SimpleClickableField(label: String) {
    OutlinedTextField(
        value = "", onValueChange = {}, readOnly = true,
        label = { Text(label, color = Color.Gray) },
        modifier = Modifier.fillMaxWidth().clickable { /* TODO: Abrir seleção de cor */ },
        shape = RoundedCornerShape(8.dp),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedContainerColor = Color.White,
            focusedContainerColor = Color.White
        )
    )
    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
fun LargeTextField(value: String, onValueChange: (String) -> Unit, label: String) {
    OutlinedTextField(
        value = value, onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth().height(120.dp),
        shape = RoundedCornerShape(8.dp),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedContainerColor = Color.White,
            focusedContainerColor = Color.White
        )
    )
    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
fun WarningBox() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(LightYellow)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = Icons.Default.Warning, contentDescription = "Aviso", tint = Color(0xFFFBC02D))
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = "Sua foto não será exibida se for identificada alguma suspeita de conteúdo sensível, impróprio ou fora das regras da plataforma.",
            style = MaterialTheme.typography.bodySmall, lineHeight = 16.sp
        )
    }
}

@Composable
fun AddPhotoButton() {
    Card(
        modifier = Modifier.fillMaxWidth().height(80.dp).clickable { /* TODO: Abrir galeria */ },
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(1.dp, Color.LightGray),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(Icons.Default.Add, contentDescription = "Adicionar Foto", tint = Color.Gray, modifier = Modifier.size(28.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Adicionar foto", color = Color.Gray, fontSize = 16.sp)
        }
    }
}

@Composable
fun TermsAndConditionsCheckbox(checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    val uriHandler = LocalUriHandler.current
    val annotatedString = buildAnnotatedString {
        append("Declaro que li, compreendi e concordo com os ")
        pushStringAnnotation(tag = "TERMOS", annotation = "https://example.com/termos")
        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)) {
            append("Termos de Uso")
        }
        pop()
        append(" e com a ")
        pushStringAnnotation(tag = "POLITICA", annotation = "https://example.com/politica")
        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)) {
            append("Política de Privacidade")
        }
        pop()
        append(".")
    }

    Row(verticalAlignment = Alignment.CenterVertically) {
        Checkbox(checked = checked, onCheckedChange = onCheckedChange)
        ClickableText(
            text = annotatedString,
            style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 20.sp),
            onClick = { offset ->
                annotatedString.getStringAnnotations(tag = "TERMOS", start = offset, end = offset)
                    .firstOrNull()?.let { annotation -> uriHandler.openUri(annotation.item) }
                annotatedString.getStringAnnotations(tag = "POLITICA", start = offset, end = offset)
                    .firstOrNull()?.let { annotation -> uriHandler.openUri(annotation.item) }
            }
        )
    }
}

// --- Preview ---
@Preview(showBackground = true)
@Composable
fun PetPerdidoScreenPreview() {
    MaterialTheme {
        PetPerdidoScreen(onNavigateBack = {})
    }
}