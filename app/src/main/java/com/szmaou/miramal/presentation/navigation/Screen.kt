package com.szmaou.miramal.presentation.navigation

sealed class Screen(val route: String) {
    data object Login : Screen("login")
    data object Search : Screen("search")
    data object Detail : Screen("detail/{animeId}") {
        fun createRoute(animeId: Int) = "detail/$animeId"
    }
    data object Favorites : Screen("favorites")
}
