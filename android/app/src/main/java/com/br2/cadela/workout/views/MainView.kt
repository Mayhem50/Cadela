package com.br2.cadela.workout.views

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.br2.cadela.ui.theme.Red500
import com.br2.cadela.workout.WorkoutModule
import com.br2.cadela.workout.views.run.WorkoutRunView
import com.google.accompanist.insets.navigationBarsPadding

@Composable
fun WorkoutMainView(mainNavController: NavController?) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(Red500)
    ) {
        val navController = rememberNavController()
        var hideNavBar by remember {
            mutableStateOf(false)
        }

        Scaffold(
            bottomBar = {
                BottomNavBar(
                    navController = navController,
                    routes = listOf(
                        RouteItem("workout_home", Icons.Filled.Home),
                        RouteItem("nav_settings", Icons.Filled.Settings),
                        RouteItem("workout_stats", Icons.Filled.Timeline)
                    ),
                    shouldHide = hideNavBar,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            },
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .navigationBarsPadding()
        ) {
            NavHost(navController = navController, startDestination = "workout_home") {
                composable("workout_home") {WorkoutHomeView(WorkoutModule.workoutVm, navController) }
                composable("nav_settings") { WorkoutHomeView(WorkoutModule.workoutVm, navController) }
                composable("workout_stats") { WorkoutHomeView(WorkoutModule.workoutVm, navController) }
                composable("workout_run") { WorkoutRunView(WorkoutModule.workoutVm, navController) }
            }
        }
    }
}


@OptIn(ExperimentalAnimationApi::class)
@Composable
private fun BottomNavBar(
    navController: NavHostController,
    routes: List<RouteItem>,
    selectedContentColor: Color = Color.White,
    unselectedContentColor: Color = Color.LightGray,
    shouldHide: Boolean = false,
    modifier: Modifier = Modifier
) {
    var hide by remember { mutableStateOf(false) }
    var currentRoute by remember { mutableStateOf("workout_home") }
    navController.addOnDestinationChangedListener { _, destination, _ ->
        currentRoute = destination.route!!
        hide = shouldHide || !routes.map { it.path }.contains(destination.route!!)
    }

    AnimatedVisibility(
        visible = !hide,
        enter = slideInVertically({ it }),
        exit = slideOutVertically({ it })
    ) {
        BottomNavigation(modifier = modifier) {
            routes.map {
                BottomNavigationItem(
                    selected = currentRoute == it.path,
                    onClick = {
                        navController.navigate(it.path)
                    },
                    selectedContentColor = selectedContentColor,
                    unselectedContentColor = unselectedContentColor,
                    icon = {
                        Icon(it.icon, null)
                    })
            }
        }
    }
}

data class RouteItem(val path: String, val icon: ImageVector)
