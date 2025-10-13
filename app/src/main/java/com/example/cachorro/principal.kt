// anote/cole em: principal.kt

package com.example.cachorro

import android.content.Context
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.viewmodel.compose.viewModel
import java.util.concurrent.TimeUnit

val DarkBlue = Color(0xFF2A3F6F)
val TealGreen = Color(0xFF00A99D)
val LightGray = Color(0xFFF0F0F0)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EncontreSeuPetScreen(
    petViewModel: PetViewModel = viewModel(),
    onNavigateToPetForm: () -> Unit,
    onNavigateToPetDetail: (Int) -> Unit
) {
    var textoBusca by remember { mutableStateOf("") }
    var filtroSelecionado by remember { mutableStateOf("Todos") }

    // --- MUDANÇA SIGNIFICATIVA ---
    // A lista de pets agora vem do ViewModel e se atualiza sozinha.
    val pets by petViewModel.allPets.collectAsState()

    Scaffold(
        // ... (o TopAppBar continua o mesmo)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                // ... (a parte de busca e filtros continua a mesma)

                AcoesPrincipais(
                    onPerdiMeuPetClick = onNavigateToPetForm,
                    onViSeuPetClick = onNavigateToPetForm
                )
                // ...
            }

            if (pets.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Nenhum pet cadastrado ainda.")
                }
            } else {
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
}
//... O resto do arquivo (BuscaHeader, FiltroPets, AcoesPrincipais) continua igual

// --- MUDANÇA SIGNIFICATIVA NO PetCard ---
@Composable
fun PetCard(pet: Pet, onVerDetalhesClick: () -> Unit) {
    // Função para converter o nome da imagem (String) em um ID de drawable (Int)
    fun getDrawableResourceId(name: String, context: Context): Int {
        return context.resources.getIdentifier(name, "drawable", context.packageName)
    }

    Card( /* ... */ ) {
        ConstraintLayout( /* ... */ ) {
            val (image, textColumn, button) = createRefs()

            Image(
                painter = painterResource(id = getDrawableResourceId(pet.imageName, LocalContext.current)),
                // ...
            )
            // ...
            Column( /* ... */ ) {
                // ...
                Text(
                    text = getTempoFormatado(pet.createdAt), // Usamos o tempo formatado
                    color = Color.Gray,
                    fontSize = 12.sp
                )
                // ...
            }
        }
    }
}

// Função para calcular o tempo decorrido
fun getTempoFormatado(timestamp: Long): String {
    val agora = System.currentTimeMillis()
    val diferenca = agora - timestamp

    val segundos = TimeUnit.MILLISECONDS.toSeconds(diferenca)
    val minutos = TimeUnit.MILLISECONDS.toMinutes(diferenca)
    val horas = TimeUnit.MILLISECONDS.toHours(diferenca)
    val dias = TimeUnit.MILLISECONDS.toDays(diferenca)

    return when {
        segundos < 60 -> "Agora mesmo"
        minutos < 60 -> "Há $minutos min"
        horas < 24 -> "Há $horas h"
        else -> "Há $dias dias"
    }
}
// Cole todo o restante do arquivo principal.kt aqui, sem alterações...