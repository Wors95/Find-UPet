package com.example.cachorro // Substitua pelo seu nome de pacote

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// --- Definição de Cores ---
val DarkBlue = Color(0xFF2A3F6F)
val TealGreen = Color(0xFF00A99D)
val LightGray = Color(0xFFF0F0F0)

// --- Composable Principal da Tela ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EncontreSeuPetScreen(
    onNavigateToPetPerdido: () -> Unit,
    onNavigateToPetEncontrado: () -> Unit
) {
    var textoBusca by remember { mutableStateOf("") }
    var filtroSelecionado by remember { mutableStateOf("Todos") }

    // Dados de exemplo para a lista (substitua por dados reais do seu ViewModel)
    val petsDeExemplo = listOf(
        Pet(1, "Vi seu Pet", "Shih Tzu", "Macho", "Londrina - Ouro Verde", "Agora mesmo", R.drawable.pet1),
        Pet(2, "Vi seu Pet", "Sem Raça Definida (SRD)", "Fêmea", "São José dos Pinhais", "Agora mesmo", R.drawable.pet2),
        Pet(3, "Fiona", "Sem Raça Definida (SRD)", "Fêmea", "Piraquara", "Agora mesmo", R.drawable.pet3),
        Pet(4, "Mima", "Sem Raça Definida (SRD)", "Fêmea", "Francisco Beltrão - Vila Nova", "Agora mesmo", R.drawable.pet4)
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Image(
                        painter = painterResource(id = R.drawable.logo), // Certifique-se de ter um logo em res/drawable
                        contentDescription = "Logo Encontre Seu Pet",
                        modifier = Modifier.height(32.dp)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { /* TODO: Ação de voltar, se aplicável */ }) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                },
                actions = {
                    IconButton(onClick = { /* TODO: Navegar para Perfil */ }) {
                        Icon(imageVector = Icons.Default.Person, contentDescription = "Perfil")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color.White
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Spacer(modifier = Modifier.height(16.dp))
                BuscaHeader()
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = textoBusca,
                    onValueChange = { textoBusca = it },
                    placeholder = { Text("Buscar por nome, raça, localização...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Ícone de busca") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = TealGreen,
                        unfocusedBorderColor = Color.LightGray
                    )
                )
                Spacer(modifier = Modifier.height(12.dp))
                FiltroPets(
                    filtroSelecionado = filtroSelecionado,
                    onFiltroChange = { filtroSelecionado = it }
                )
                Spacer(modifier = Modifier.height(24.dp))
                AcoesPrincipais(
                    onPerdiMeuPetClick = onNavigateToPetPerdido,
                    onViSeuPetClick = onNavigateToPetEncontrado
                )
                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    text = "Últimas publicações",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            // --- Lista de Pets ---
            LazyColumn(contentPadding = PaddingValues(horizontal = 16.dp)) {
                items(petsDeExemplo) { pet ->
                    PetCard(pet = pet)
                }
            }
        }
    }
}

// --- Componentes da Tela de Busca ---

@Composable
private fun BuscaHeader() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Buscar pets",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )
        TextButton(onClick = { /* TODO: Navegar para busca avançada */ }) {
            Text(text = "Busca avançada →", color = MaterialTheme.colorScheme.primary)
        }
    }
}

@Composable
private fun FiltroPets(filtroSelecionado: String, onFiltroChange: (String) -> Unit) {
    val filtros = listOf("Todos", "Cachorro", "Gato")
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        filtros.forEach { filtro ->
            val isSelected = filtro == filtroSelecionado
            Button(
                onClick = { onFiltroChange(filtro) },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isSelected) TealGreen else LightGray,
                    contentColor = if (isSelected) Color.White else Color.Gray
                ),
                elevation = ButtonDefaults.buttonElevation(0.dp)
            ) {
                Text(text = filtro)
            }
        }
    }
}

@Composable
private fun AcoesPrincipais(onPerdiMeuPetClick: () -> Unit, onViSeuPetClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Button(
            onClick = { onPerdiMeuPetClick() },
            modifier = Modifier.weight(1f).height(80.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = DarkBlue)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(imageVector = Icons.Default.Warning, contentDescription = null)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Perdi meu Pet!", fontSize = 16.sp)
            }
        }
        Button(
            onClick = onViSeuPetClick,
            modifier = Modifier.weight(1f).height(80.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = TealGreen)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(imageVector = Icons.Default.Search, contentDescription = null)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Vi seu Pet!", fontSize = 16.sp)
            }
        }
    }
}


// --- Card Reutilizável para a Lista ---

@Composable
fun PetCard(pet: Pet) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = pet.imageResId),
                contentDescription = "Foto do ${pet.nome}",
                modifier = Modifier.size(80.dp).clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(pet.nome, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(pet.tempo, color = Color.Gray, fontSize = 12.sp)
                }
                Text(pet.raca, fontSize = 14.sp, color = Color.DarkGray)
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Female, contentDescription = "Sexo", modifier = Modifier.size(16.dp), tint = Color.Gray)
                    Text(pet.sexo, fontSize = 14.sp, color = Color.Gray, modifier = Modifier.padding(start = 4.dp))
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.LocationOn, contentDescription = "Local", modifier = Modifier.size(16.dp), tint = Color.Gray)
                    Text(pet.local, fontSize = 14.sp, color = Color.Gray, modifier = Modifier.padding(start = 4.dp))
                }
            }
            Button(
                onClick = { /* TODO: Navegar para detalhes do pet */ },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = DarkBlue)
            ) {
                Text("Ver detalhes")
            }
        }
    }
}


// --- Preview ---
@Preview(showBackground = true)
@Composable
fun EncontreSeuPetScreenPreview() {
    EncontreSeuPetScreen(onNavigateToPetPerdido = {}, onNavigateToPetEncontrado = {})
}