

package com.example.cachorro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
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
                val navController = rememberNavController()
                val petViewModel: PetViewModel = viewModel()

                NavHost(navController = navController, startDestination = "principal") {
                    composable("principal") {
                        EncontreSeuPetScreen(
                            petViewModel = petViewModel,
                            onNavigateToPetForm = {
                                navController.navigate("pet_form")
                            },
                            onNavigateToPetDetail = { petId ->
                                navController.navigate("pet_detalhes/$petId")
                            }
                        )
                    }

                    composable(
                        route = "pet_detalhes/{petId}",
                        arguments = listOf(navArgument("petId") { type = NavType.IntType })
                    ) { backStackEntry ->
                        val petId = backStackEntry.arguments?.getInt("petId") ?: 0

                        val pets by petViewModel.allPets.collectAsState()
                        val pet = pets.find { it.id == petId }

                        if (pet != null) {
                            PetDetailScreen(
                                petViewModel = petViewModel,
                                pet = pet,
                                onNavigateBack = { navController.popBackStack() },
                                onNavigateToEdit = { id ->
                                    navController.navigate("pet_form/$id")
                                }
                            )
                        }
                    }

                    composable("pet_form") {
                        PetPerdidoScreen(
                            petViewModel = petViewModel,
                            onNavigateBack = { navController.popBackStack() }
                        )
                    }

                    composable(
                        route = "pet_form/{petId}",
                        arguments = listOf(navArgument("petId") { type = NavType.IntType })
                    ) { backStackEntry ->
                        val petId = backStackEntry.arguments?.getInt("petId")
                        PetPerdidoScreen(
                            petViewModel = petViewModel,
                            onNavigateBack = { navController.popBackStack() },
                            petId = petId
                        )
                    }
                }
            }
        }
    }
}