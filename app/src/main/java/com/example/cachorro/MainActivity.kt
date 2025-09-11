package com.example.cachorro // Verifique se o pacote está correto

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cachorro.ui.theme.CachorroTheme // Substitua pelo nome do seu tema

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CachorroTheme { // Substitua pelo nome do seu tema
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    // 1. Cria o controlador de navegação
                    val navController = rememberNavController()

                    // 2. Cria o host que vai exibir as telas
                    NavHost(
                        navController = navController,
                        startDestination = "principal.kt" // Define a rota da primeira tela a ser exibida
                    ) {
                        // 3. Define a primeira rota: "principal"
                        composable("principal") {
                            EncontreSeuPetScreen(
                                // Ação para quando clicar em "Perdi meu Pet!"
                                onNavigateToPetPerdido = {
                                    navController.navigate("cadastro_pet_perdido.kt")
                                },
                                // Ação para quando clicar em "Vi seu Pet!"
                                onNavigateToPetEncontrado = {
                                    // TODO: Criar a tela e a rota para "pet encontrado"
                                    // navController.navigate("cadastro_pet_encontrado")
                                }
                            )
                        }

                        // 4. Define a segunda rota: "cadastro_pet_perdido"
                        composable("cadastro_pet_perdido") {
                            PetPerdidoScreen(
                                // Ação para quando clicar no botão de voltar na TopAppBar
                                onNavigateBack = {
                                    navController.popBackStack()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}