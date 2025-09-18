package com.example.cachorro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
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
                                    navController.navigate("cadastro_pet_perdido")
                                },
                                onNavigateToPetEncontrado = {
                                    navController.navigate("cadastro_pet_encontrado")
                                },
                                // Adicionamos a navegação para a tela de detalhes
                                onNavigateToPetDetail = { petId ->
                                    navController.navigate("pet_detalhes/$petId")
                                }
                            )
                        }

                        // Rota para a tela de detalhes
                        composable(
                            route = "pet_detalhes/{petId}",
                            arguments = listOf(navArgument("petId") { type = NavType.IntType })
                        ) { backStackEntry ->
                            val petId = backStackEntry.arguments?.getInt("petId")
                            val pet = PetRepository.getPetById(petId ?: -1)

                            if (pet != null) {
                                PetDetailScreen(
                                    pet = pet,
                                    onNavigateBack = {
                                        navController.popBackStack()
                                    }
                                )
                            } else {
                                // Opcional: Mostrar uma tela de erro se o pet não for encontrado
                            }
                        }

                        composable("cadastro_pet_perdido") {
                            PetPerdidoScreen(
                                onNavigateBack = {
                                    navController.popBackStack()
                                }
                            )
                        }
                        composable("cadastro_pet_encontrado") {
                            PetEncontradoScreen(
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