package com.example.cachorro



import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetDetailScreen(
    pet: Pet,
    onNavigateBack: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(pet.nome, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                },
                actions = {
                    IconButton(onClick = { /* TODO: Ação de ajuda */ }) {
                        Icon(Icons.Default.HelpOutline, contentDescription = "Ajuda")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = Color(0xFFF7F7F7)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            Image(
                painter = painterResource(id = pet.imageResId),
                contentDescription = "Foto do ${pet.nome}",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                contentScale = ContentScale.Crop
            )

            Column(modifier = Modifier.padding(16.dp)) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Row(
                        modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Estou perdido há ${pet.tempo}",
                            fontWeight = FontWeight.Bold,
                            color = DarkBlue,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(onClick = { /* TODO: Compartilhar */ }) {
                            Icon(Icons.Default.Share, contentDescription = "Compartilhar", tint = DarkBlue)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = pet.nome,
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                SectionDetail(title = "Onde eu perdi:") {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.LocationOn, contentDescription = null, tint = Color.Gray, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(pet.local, style = MaterialTheme.typography.bodyLarge)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(pet.descricao, style = MaterialTheme.typography.bodyLarge, color = Color.DarkGray)
                }

                Divider(modifier = Modifier.padding(vertical = 16.dp))

                SectionDetail(title = "Meus dados:") {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        InfoColumn(
                            "Idade" to pet.idade,
                            "Sexo" to pet.sexo,
                            "Cor" to pet.cor
                        )
                        InfoColumn(
                            "Porte" to pet.porte,
                            "Raça" to pet.raca
                        )
                    }
                }

                if (pet.caracteristicas.isNotEmpty()) {
                    Divider(modifier = Modifier.padding(vertical = 16.dp))
                    SectionDetail(title = "Sobre mim:") {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            pet.caracteristicas.forEach { caracteristica ->
                                FilterChip(
                                    selected = true,
                                    onClick = { },
                                    label = { Text(caracteristica) }
                                )
                            }
                        }
                    }
                }

                // Você pode adicionar as seções de "denúncia" e "comentários" aqui
                // de forma similar às seções acima.
            }
        }
    }
}

@Composable
private fun SectionDetail(title: String, content: @Composable ColumnScope.() -> Unit) {
    Column {
        Text(text = title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        content()
    }
}

@Composable
private fun RowScope.InfoColumn(vararg pairs: Pair<String, String>) {
    Column(modifier = Modifier.weight(1f)) {
        pairs.forEach { (label, value) ->
            Text(label, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(value, fontSize = 16.sp, color = Color.DarkGray)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}