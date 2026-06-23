package com.kenji.food.tracker.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.kenji.food.tracker.ui.component.BottomBar
import com.kenji.food.tracker.ui.screen.HomeScreen
import com.kenji.food.tracker.ui.screen.count.CountScreen
import com.kenji.food.tracker.ui.screen.food.AddFoodScreen
import com.kenji.food.tracker.ui.screen.food.FoodListScreen
import com.kenji.food.tracker.ui.screen.onboarding.OnboardingRoute
import com.kenji.food.tracker.ui.screen.onboarding.onboardingBuilder
import com.kenji.food.tracker.ui.screen.profile.FoodTargetScreen
import com.kenji.food.tracker.ui.screen.profile.ProfileScreen
import com.kenji.food.tracker.ui.screen.recipe.AddRecipeScreen
import com.kenji.food.tracker.ui.screen.recipe.RecipeListScreen
import com.kenji.food.tracker.ui.viewmodel.food.target.FoodTargetViewModel

@Composable
fun AppNavigation() {
    val backStack = rememberNavBackStack(OnboardingRoute.Loading)

    Scaffold(
        bottomBar = {
            val currentRoute = backStack.lastOrNull()
            if (currentRoute is Route) {
                BottomBar(currentRoute) { route ->
                    backStack.clear()
                    backStack.add(route)
                }
            }
        },
    ) { innerPadding ->
        NavDisplay(
            modifier = Modifier.padding(innerPadding),
            backStack = backStack,
            onBack = { backStack.removeLastOrNull() },
            entryDecorators = listOf(
                rememberSaveableStateHolderNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator()
            ),
            entryProvider = entryProvider {
                onboardingBuilder(backStack)

                entry<Route.Home> {
                    HomeScreen { route ->
                        backStack.add(route)
                    }
                }

                entry<Route.FoodList> {
                    FoodListScreen(
                        onNavigate = { backStack.add(it) },
                        onBackPressed = { backStack.removeLastOrNull() }
                    )
                }

                entry<Route.AddFood> {
                    AddFoodScreen {
                        backStack.removeLastOrNull()
                    }
                }

                entry<Route.RecipeList> {
                    RecipeListScreen(
                        onNavigate = { backStack.add(it) },
                        onBackPressed = { backStack.removeLastOrNull() }
                    )
                }

                entry<Route.AddRecipe> {
                    AddRecipeScreen {
                        backStack.removeLastOrNull()
                    }
                }

                entry<Route.Count> {
                    CountScreen()
                }

                entry<Route.Profile> {
                    ProfileScreen {
                        backStack.add(it)
                    }
                }

                entry<Route.FoodTarget> { key ->
                    FoodTargetScreen(
                        viewModel = hiltViewModel<FoodTargetViewModel, FoodTargetViewModel.Factory>(
                            creationCallback = { factory ->
                                factory.create(key)
                            }
                        ),
                        onBackPressed = {
                            backStack.removeLastOrNull()
                        }
                    )
                }
            }
        )
    }
}
