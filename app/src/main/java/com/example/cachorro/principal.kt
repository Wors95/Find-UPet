package com.example.cachorro

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


val DarkBlue = Color(0xFF2A3F6F)
    val TealGreen = Color(0xFF00A99D)
    val LightGray = Color(0xFFF0F0F0)

    @OptIn(ExperimentalMaterial3Api::class)


    @Composable
    fun EncontreSeuPetScreen() {
        var textoBusca by remember { mutableStateOf("") }
        var filtroSelecionado by remember { mutableStateOf("Todos") }

        Scaffold(
            topBar = {
                TopAppBar(
                    title = {

                        Image(
                            painter = painterResource(id = R.drawable.download),
                            contentDescription = "Logo Encontre Seu Pet",
                            modifier = Modifier.height(32.dp)
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Voltar"
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = { }) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Perfil"
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.White,
                        titleContentColor = MaterialTheme.colorScheme.onSurface,
                        actionIconContentColor = MaterialTheme.colorScheme.onSurface
                    )
                )
            },
            containerColor = Color.White
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp)
                    .fillMaxSize()
            ) {
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


                AcoesPrincipais()

                Spacer(modifier = Modifier.height(32.dp))


                Text(
                    text = "Últimas publicações",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )


            }
        }
    }

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
            TextButton(onClick = { }) {
                Text(text = "Busca avançada →", color = MaterialTheme.colorScheme.primary)
            }
        }
    }

    @Composable
    private fun FiltroPets(filtroSelecionado: String, onFiltroChange: (String) -> Unit) {
        val filtros = listOf("Todos", "Cachorro", "Gato")
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
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
    private fun AcoesPrincipais() {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                onClick = {  },
                modifier = Modifier
                    .weight(1f)
                    .height(80.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = DarkBlue)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "Perdi meu Pet!", fontSize = 16.sp)
                }
            }
            Button(
                onClick = { },
                modifier = Modifier
                    .weight(1f)
                    .height(80.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = TealGreen)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {

                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "Vi seu Pet!", fontSize = 16.sp)
                }
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun EncontreSeuPetScreenPreview() {

        EncontreSeuPetScreen()
    }