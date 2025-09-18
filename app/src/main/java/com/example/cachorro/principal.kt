package com.example.cachorro

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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension

// --- Definição de Cores ---
val DarkBlue = Color(0xFF2A3F6F)
val TealGreen = Color(0xFF00A99D)
val LightGray = Color(0xFFF0F0F0)

// --- Composable Principal da Tela ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EncontreSeuPetScreen(
    onNavigateToPetPerdido: () -> Unit,
    onNavigateToPetEncontrado: () -> Unit,
    onNavigateToPetDetail: (Int) -> Unit // Parâmetro para navegar para os detalhes
) {
    var textoBusca by remember { mutableStateOf("") }
    var filtroSelecionado by remember { mutableStateOf("Todos") }

    val pets = PetRepository.getPets()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Image(
                        painter = painterResource(id = R.drawable.logo),
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
                items(pets) { pet ->
                    PetCard(
                        pet = pet,
                        onVerDetalhesClick = { onNavigateToPetDetail(pet.id) }
                    )
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
fun PetCard(pet: Pet, onVerDetalhesClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            val (image, textColumn, button) = createRefs()

            Image(
                painter = painterResource(id = pet.imageResId),
                contentDescription = "Foto do ${pet.nome}",
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .constrainAs(image) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    },
                contentScale = ContentScale.Crop
            )

            Button(
                onClick = { onVerDetalhesClick() },
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = DarkBlue),
                modifier = Modifier.constrainAs(button) {
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
            ) {
                Text("Ver detalhes")
            }

            Column(
                modifier = Modifier.constrainAs(textColumn) {
                    start.linkTo(image.end, margin = 16.dp)
                    end.linkTo(button.start, margin = 8.dp)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    width = Dimension.fillToConstraints
                }
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = pet.nome,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false)
                    )
                    Text(
                        text = pet.tempo,
                        color = Color.Gray,
                        fontSize = 12.sp
                    )
                }
                Text(
                    text = pet.raca,
                    fontSize = 14.sp,
                    color = Color.DarkGray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Female, contentDescription = "Sexo", modifier = Modifier.size(16.dp), tint = Color.Gray)
                    Text(
                        text = pet.sexo,
                        fontSize = 14.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(start = 4.dp),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.LocationOn, contentDescription = "Local", modifier = Modifier.size(16.dp), tint = Color.Gray)
                    Text(
                        text = pet.local,
                        fontSize = 14.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(start = 4.dp),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

// --- Preview ---
@Preview(showBackground = true)
@Composable
fun EncontreSeuPetScreenPreview() {
    EncontreSeuPetScreen(
        onNavigateToPetPerdido = {},
        onNavigateToPetEncontrado = {},
        onNavigateToPetDetail = {}
    )
}