package com.desligo.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.desligo.ui.screens.*

object Routes {
    const val ONBOARDING = "onboarding"
    const val HOME = "home"
    const val PROFILE_DETAIL = "profile_detail/{profileId}"
    const val TIMER = "timer"
    const val SETTINGS = "settings"
    const val PREMIUM = "premium"
    const val MANUFACTURER_GUIDE = "manufacturer_guide"
    const val APP_SELECTION = "app_selection/{profileId}"
    const val PIN_SETUP = "pin_setup"
    const val PIN_VERIFY = "pin_verify"

    fun profileDetail(id: Long) = "profile_detail/$id"
    fun appSelection(profileId: Long) = "app_selection/$profileId"
}

@Composable
fun DesligoNavGraph() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.ONBOARDING) {
        composable(Routes.ONBOARDING) {
            OnboardingScreen(
                onComplete = {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.ONBOARDING) { inclusive = true }
                    }
                }
            )
        }

        composable(Routes.HOME) {
            HomeScreen(
                onProfileClick = { profileId ->
                    navController.navigate(Routes.profileDetail(profileId))
                },
                onTimerClick = { navController.navigate(Routes.TIMER) },
                onSettingsClick = { navController.navigate(Routes.SETTINGS) },
                onPremiumClick = { navController.navigate(Routes.PREMIUM) }
            )
        }

        composable(Routes.PROFILE_DETAIL) { backStackEntry ->
            val profileId = backStackEntry.arguments?.getString("profileId")?.toLongOrNull() ?: 0
            ProfileDetailScreen(
                profileId = profileId,
                onBack = { navController.popBackStack() },
                onManageApps = { navController.navigate(Routes.appSelection(profileId)) }
            )
        }

        composable(Routes.TIMER) {
            TimerScreen(onBack = { navController.popBackStack() })
        }

        composable(Routes.SETTINGS) {
            SettingsScreen(
                onBack = { navController.popBackStack() },
                onPremiumClick = { navController.navigate(Routes.PREMIUM) },
                onManufacturerGuide = { navController.navigate(Routes.MANUFACTURER_GUIDE) },
                onPinSetup = { navController.navigate(Routes.PIN_SETUP) }
            )
        }

        composable(Routes.PREMIUM) {
            PremiumScreen(onBack = { navController.popBackStack() })
        }

        composable(Routes.MANUFACTURER_GUIDE) {
            ManufacturerGuideScreen(onBack = { navController.popBackStack() })
        }

        composable(Routes.APP_SELECTION) { backStackEntry ->
            val profileId = backStackEntry.arguments?.getString("profileId")?.toLongOrNull() ?: 0
            AppSelectionScreen(
                profileId = profileId,
                onBack = { navController.popBackStack() }
            )
        }

        composable(Routes.PIN_SETUP) {
            PinSetupScreen(onBack = { navController.popBackStack() })
        }
    }
}
