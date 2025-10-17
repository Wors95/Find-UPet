// Localização: ui/principal/principal.kt

package com.example.cachorro.ui.principal

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
import com.example.cachorro.R
import com.example.cachorro.data.local.model.Pet
import com.example.cachorro.ui.formulario.PetViewModel
import com.example.cachorro.ui.theme.CachorroTheme
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EncontreSeuPetScreen(
    petViewModel: PetViewModel = viewModel(),
    onNavigateToPetForm: () -> Unit,
    onNavigateToPetDetail: (Int) -> Unit
) {
    // A tela agora observa todos os estados do ViewModel
    val pets by petViewModel.petsFiltrados.collectAsState()
    val filtroSelecionado by petViewModel.filtroSelecionado.collectAsState()
    val textoBusca by petViewModel.textoBusca.collectAsState() // <-- NOVO ESTADO OBSERVADO

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
                    IconButton(onClick = { /* TODO */ }) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                },
                actions = {
                    IconButton(onClick = { /* TODO */ }) {
                        Icon(imageVector = Icons.Default.Person, contentDescription = "Perfil")
                    }
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.background
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

                // --- MUDANÇA PRINCIPAL AQUI ---
                // O TextField agora é controlado 100% pelo ViewModel
                OutlinedTextField(
                    value = textoBusca,
                    onValueChange = { petViewModel.atualizarTextoBusca(it) },
                    placeholder = { Text("Buscar por nome, raça, localização...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Ícone de busca") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.secondary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                    )
                )

                Spacer(modifier = Modifier.height(12.dp))

                FiltroPets(
                    filtroSelecionado = filtroSelecionado,
                    onFiltroChange = { petViewModel.atualizarFiltro(it) }
                )

                Spacer(modifier = Modifier.height(24.dp))
                AcoesPrincipais(
                    onPerdiMeuPetClick = onNavigateToPetForm,
                    onViSeuPetClick = onNavigateToPetForm
                )
                Spacer(modifier = Modifier.height(32.dp))
                Text(
                    text = "Últimas publicações",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            if (pets.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Nenhum pet encontrado com este filtro.")
                }
            } else {
                LazyColumn(contentPadding = PaddingValues(horizontal = 16.dp)) {
                    items(items = pets, key = { it.id }) { pet ->
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
// ... O resto do arquivo (BuscaHeader, PetCard, etc.) continua exatamente igual.
// Código completo abaixo para garantir.

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
        TextButton(onClick = { /* TODO */ }) {
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
                    containerColor = if (isSelected) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = if (isSelected) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.onSurfaceVariant
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
            modifier = Modifier
                .weight(1f)
                .height(80.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(imageVector = Icons.Default.Warning, contentDescription = null)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Perdi meu Pet!", fontSize = 16.sp)
            }
        }
        Button(
            onClick = onViSeuPetClick,
            modifier = Modifier
                .weight(1f)
                .height(80.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(imageVector = Icons.Default.Search, contentDescription = null)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Vi seu Pet!", fontSize = 16.sp)
            }
        }
    }
}

@Composable
fun PetCard(pet: Pet, onVerDetalhesClick: () -> Unit) {
    fun getDrawableResourceId(name: String, context: Context): Int {
        val resourceId = context.resources.getIdentifier(name, "drawable", context.packageName)
        return if (resourceId == 0) R.drawable.logo else resourceId
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            val (image, textColumn, button) = createRefs()

            Image(
                painter = painterResource(id = getDrawableResourceId(pet.imageName, LocalContext.current)),
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
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
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
                        text = getTempoFormatado(pet.createdAt),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 12.sp
                    )
                }
                Text(
                    text = pet.raca,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Female, contentDescription = "Sexo", modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(
                        text = pet.sexo,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(start = 4.dp),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.LocationOn, contentDescription = "Local", modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.onSurfaceVariant)
                    Text(
                        text = pet.local,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(start = 4.dp),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

fun getTempoFormatado(timestamp: Long): String {
    val agora = System.currentTimeMillis()
    val diferenca = agora - timestamp

    val segundos = TimeUnit.MILLISECONDS.toSeconds(diferenca)
    if (segundos < 60) return "Agora mesmo"

    val minutos = TimeUnit.MILLISECONDS.toMinutes(diferenca)
    if (minutos < 60) return "Há $minutos min"

    val horas = TimeUnit.MILLISECONDS.toHours(diferenca)
    if (horas < 24) return "Há $horas h"

    val dias = TimeUnit.MILLISECONDS.toDays(diferenca)
    return "Há $dias dias"
}

@Preview(showBackground = true)
@Composable
fun EncontreSeuPetScreenPreview() {
    CachorroTheme {
        EncontreSeuPetScreen(
            onNavigateToPetForm = {},
            onNavigateToPetDetail = {}
        )
    }
}