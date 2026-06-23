package com.kenji.food.tracker.ui.screen.onboarding

import androidx.navigation3.runtime.EntryProviderScope
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import com.kenji.food.tracker.ui.Route

fun EntryProviderScope<NavKey>.onboardingBuilder(backStack: NavBackStack<NavKey>) {
    entry<OnboardingRoute.Loading> {
        OnboardingLoadingScreen(
            continueOnboarding = {
                backStack.clear()
                backStack.add(OnboardingRoute.Start)
            },
            skipOnboarding = {
                backStack.clear()
                backStack.add(Route.Home)
            }
        )
    }

    entry<OnboardingRoute.Start> {
        OnboardingStartScreen(
            onStart = {
                backStack.add(OnboardingRoute.Input)
            },
        )
    }
    entry<OnboardingRoute.Input> {
        OnboardingInputScreen {
            backStack.clear()
            backStack.add(Route.Home)
        }
    }
}

