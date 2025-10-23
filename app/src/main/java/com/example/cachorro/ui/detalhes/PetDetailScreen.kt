// Localização: ui/detalhes/PetDetailScreen.kt

package com.example.cachorro.ui.detalhes

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.cachorro.R
import com.example.cachorro.data.local.model.Comment
import com.example.cachorro.data.local.model.Pet
import com.example.cachorro.ui.formulario.PetViewModel
import com.example.cachorro.ui.principal.getTempoFormatado
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PetDetailScreen(
    petViewModel: PetViewModel = viewModel(),
    pet: Pet,
    onNavigateBack: () -> Unit,
    onNavigateToEdit: (Int) -> Unit
) {
    LaunchedEffect(pet.id) {
        petViewModel.loadComments(pet.id)
    }

    val comments by petViewModel.comments.collectAsState()
    var newCommentText by remember { mutableStateOf("") }

    fun getDrawableResourceId(name: String, context: Context): Int {
        val resourceId = context.resources.getIdentifier(name, "drawable", context.packageName)
        return if (resourceId == 0) R.drawable.logo else resourceId
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(pet.nome, fontWeight = FontWeight.Bold) },
                navigationIcon = { IconButton(onClick = onNavigateBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Voltar") } },
                actions = {
                    IconButton(onClick = { onNavigateToEdit(pet.id) }) { Icon(Icons.Default.Edit, "Editar") }
                    IconButton(onClick = {
                        petViewModel.deletePet(pet.id)
                        onNavigateBack()
                    }) { Icon(Icons.Default.Delete, "Deletar") }
                }
            )
        },
        bottomBar = {
            CommentInputField(
                value = newCommentText,
                onValueChange = { newCommentText = it },
                onSendClick = {
                    petViewModel.addComment(pet.id, newCommentText)
                    newCommentText = ""
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            Image(
                painter = painterResource(id = getDrawableResourceId(pet.imageName, LocalContext.current)),
                contentDescription = "Foto do ${pet.nome}",
                modifier = Modifier.fillMaxWidth().height(300.dp),
                contentScale = ContentScale.Crop
            )

            Column(modifier = Modifier.padding(16.dp)) {
                // ... (Card do Pet, Nome, etc. continuam iguais)

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                ) {
                    Row(
                        modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Visto ${getTempoFormatado(pet.createdAt)}",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(onClick = { /* TODO */ }) {
                            Icon(Icons.Default.Share, "Compartilhar", tint = MaterialTheme.colorScheme.primary)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text(pet.nome, style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))
                SectionDetail(title = "Localização:") {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.LocationOn, null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(pet.local, style = MaterialTheme.typography.bodyLarge)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(pet.descricao, style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Divider(modifier = Modifier.padding(vertical = 16.dp))
                SectionDetail(title = "Características:") {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        InfoColumn("Idade" to pet.idade, "Sexo" to pet.sexo, "Cor" to pet.cor)
                        InfoColumn("Porte" to pet.porte, "Raça" to pet.raca)
                    }
                }
                if (pet.caracteristicas.isNotEmpty()) {
                    Divider(modifier = Modifier.padding(vertical = 16.dp))
                    SectionDetail(title = "Detalhes Adicionais:") {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            pet.caracteristicas.forEach { caracteristica ->
                                FilterChip(selected = true, onClick = { }, label = { Text(caracteristica) })
                            }
                        }
                    }
                }

                Divider(modifier = Modifier.padding(vertical = 16.dp))
                SectionDetail(title = "Comentários (${comments.size})") {
                    if (comments.isEmpty()) {
                        Text("Nenhum comentário ainda. Seja o primeiro a ajudar!", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    } else {
                        comments.forEach { comment ->
                            // Passando a função de delete para o CommentItem
                            CommentItem(
                                comment = comment,
                                onDeleteClick = { petViewModel.deleteComment(comment) }
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun CommentItem(
    comment: Comment,
    onDeleteClick: () -> Unit // Recebendo a ação de clique
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = comment.author,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold
                )
                // --- BOTÃO DE DELETE ADICIONADO ---
                IconButton(onClick = onDeleteClick, modifier = Modifier.size(24.dp)) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Deletar comentário",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                }
            }
            Text(
                text = comment.text,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = SimpleDateFormat("dd/MM/yy HH:mm", Locale.getDefault()).format(Date(comment.createdAt)),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

// ... (O resto do arquivo continua igual: CommentInputField, SectionDetail, InfoColumn) ...

@Composable
fun CommentInputField(value: String, onValueChange: (String) -> Unit, onSendClick: () -> Unit) {
    Surface(shadowElevation = 8.dp) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.weight(1f),
                placeholder = { Text("Adicionar um comentário...") },
                shape = RoundedCornerShape(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(onClick = onSendClick, enabled = value.isNotBlank()) {
                Icon(Icons.AutoMirrored.Filled.Send, "Enviar comentário", tint = if (value.isNotBlank()) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
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
            Text(value, fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}