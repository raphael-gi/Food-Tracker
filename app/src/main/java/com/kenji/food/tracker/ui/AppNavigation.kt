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
import androidx.navigation3.runtime.result.LocalResultEventBus
import androidx.navigation3.runtime.result.rememberResultEventBusNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.kenji.food.tracker.ui.component.BottomBar
import com.kenji.food.tracker.ui.screen.HomeScreen
import com.kenji.food.tracker.ui.screen.count.CountHistoryListScreen
import com.kenji.food.tracker.ui.screen.count.CountScreen
import com.kenji.food.tracker.ui.screen.food.FoodDetailScreen
import com.kenji.food.tracker.ui.screen.food.FoodListScreen
import com.kenji.food.tracker.ui.screen.food.UpsertFoodScreen
import com.kenji.food.tracker.ui.screen.onboarding.OnboardingScreen
import com.kenji.food.tracker.ui.screen.profile.FoodTargetScreen
import com.kenji.food.tracker.ui.screen.profile.ProfileScreen
import com.kenji.food.tracker.ui.screen.recipe.RecipeDetailScreen
import com.kenji.food.tracker.ui.screen.recipe.RecipeListScreen
import com.kenji.food.tracker.ui.screen.recipe.UpsertRecipeScreen
import com.kenji.food.tracker.ui.screen.scan.ScannerScreen
import com.kenji.food.tracker.ui.viewmodel.food.add.UpsertFoodViewModel
import com.kenji.food.tracker.ui.viewmodel.food.detail.FoodDetailViewModel
import com.kenji.food.tracker.ui.viewmodel.food.target.FoodTargetViewModel
import com.kenji.food.tracker.ui.viewmodel.recipe.add.UpsertRecipeViewModel
import com.kenji.food.tracker.ui.viewmodel.recipe.detail.RecipeDetailViewModel

@Composable
fun AppNavigation() {
    val backStack = rememberNavBackStack(Route.Onboarding)

    Scaffold(
        bottomBar = {
            val currentRoute = backStack.lastOrNull()
            if (currentRoute != Route.Onboarding && currentRoute != Route.Scanner) {
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
                rememberViewModelStoreNavEntryDecorator(),
                rememberResultEventBusNavEntryDecorator()
            ),
            entryProvider = entryProvider {
                entry<Route.Onboarding> {
                    OnboardingScreen {
                        backStack.clear()
                        backStack.add(Route.Home)
                    }
                }

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

                entry<Route.FoodDetail> { key ->
                    FoodDetailScreen(
                        viewModel = hiltViewModel<FoodDetailViewModel, FoodDetailViewModel.Factory>(
                            creationCallback = { factory -> factory.create(key) }
                        ),
                        onPressEdit = { backStack.add(Route.UpsertFood(key.id)) },
                        onBackPressed = { backStack.removeLastOrNull() }
                    )
                }

                entry<Route.UpsertFood> { key ->
                    UpsertFoodScreen(
                        viewModel = hiltViewModel<UpsertFoodViewModel, UpsertFoodViewModel.Factory>(
                            creationCallback = { factory ->
                                factory.create(id = key.id)
                            }
                        ),
                        onLaunchScanner = { backStack.add(Route.Scanner) },
                        onNavBack = { backStack.removeLastOrNull() }
                    )
                }

                entry<Route.RecipeList> {
                    RecipeListScreen(
                        onNavigate = { backStack.add(it) },
                        onBackPressed = { backStack.removeLastOrNull() }
                    )
                }

                entry<Route.RecipeDetail> { key ->
                    RecipeDetailScreen(
                        viewModel = hiltViewModel<RecipeDetailViewModel, RecipeDetailViewModel.Factory>(
                            creationCallback = { factory -> factory.create(key.id) }
                        ),
                        onPressEdit = { backStack.add(Route.UpsertRecipe(key.id)) },
                        onBackPressed = { backStack.removeLastOrNull() }
                    )
                }

                entry<Route.UpsertRecipe> { key ->
                    UpsertRecipeScreen(
                        viewModel = hiltViewModel<UpsertRecipeViewModel, UpsertRecipeViewModel.Factory>(
                            creationCallback = { factory -> factory.create(key.id) }
                        ),
                        onNavBack = {
                            backStack.removeLastOrNull()
                        }
                    )
                }

                entry<Route.Count> {
                    CountScreen(
                        onLaunchCamera = {
                            backStack.add(Route.Scanner)
                        },
                        onFinish = {
                            backStack.clear()
                            backStack.add(Route.Home)
                        }
                    )
                }

                entry<Route.CountHistoryList> {
                    CountHistoryListScreen()
                }

                entry<Route.Profile> {
                    ProfileScreen {
                        backStack.add(it)
                    }
                }

                entry<Route.FoodTarget> { key ->
                    FoodTargetScreen(
                        viewModel = hiltViewModel<FoodTargetViewModel, FoodTargetViewModel.Factory>(
                            creationCallback = { factory -> factory.create(key) }
                        ),
                        onBackPressed = {
                            backStack.removeLastOrNull()
                        }
                    )
                }

                entry<Route.Scanner> {
                    val resultBus = LocalResultEventBus.current

                    ScannerScreen { result ->
                        resultBus.sendResult(result)
                        backStack.removeLastOrNull()
                    }
                }
            }
        )
    }
}
