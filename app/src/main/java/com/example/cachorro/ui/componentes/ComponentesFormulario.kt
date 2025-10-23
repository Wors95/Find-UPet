

package com.example.cachorro.ui.componentes

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val DarkBlues = Color(0xFF2A3F6F)
val LightGrayForm = Color(0xFFF0F0F0)
val LightYellow = Color(0xFFFFF9C4)

@Composable
fun SectionHeader(title: String) {
    Column {
        Spacer(modifier = Modifier.height(24.dp))
        Text(text = title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun SingleChoiceSegment(
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
fun LargeTextField(value: String, onValueChange: (String) -> Unit, label: String) {
    OutlinedTextField(
        value = value, onValueChange = onValueChange,
        label = { Text(label) },
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp),
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
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .clickable { /* TODO: Abrir galeria */ },
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