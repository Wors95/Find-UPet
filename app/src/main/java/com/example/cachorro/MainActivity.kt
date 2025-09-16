package com.example.cachorro

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.cachorro.ui.theme.CachorroTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CachorroTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    NavHost(
                        navController = navController,
                        startDestination = "principal"
                    ) {
                        composable("principal") {
                            EncontreSeuPetScreen(
                                onNavigateToPetPerdido = {
                                    // LOG PONTO A
                                    Log.d("MeuAppNavegacao", "PONTO A: Chamada de navegação em MainActivity EXECUTADA.")
                                    navController.navigate("cadastro_pet_perdido")
                                },
                                onNavigateToPetEncontrado = {
                                    // Lógica para o outro botão
                                }
                            )
                        }

                        composable("cadastro_pet_perdido") {
                            // LOG PONTO C
                            Log.d("MeuAppNavegacao", "PONTO C: Tela 'cadastro_pet_perdido' CARREGADA.")
                            PetPerdidoScreen(
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