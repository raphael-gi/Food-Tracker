package com.kenji.food.tracker.ui.component

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation3.runtime.NavKey
import com.kenji.food.tracker.R
import com.kenji.food.tracker.ui.Route

@Composable
fun BottomBar(currentRoute: NavKey?, onRoute: (Route) -> Unit) {
    NavigationBar {
        NavigationBarItem(
            selected = currentRoute == Route.CountHistoryList,
            onClick = { onRoute(Route.CountHistoryList) },
            icon = {
                Icon(
                    painter = painterResource(R.drawable.history),
                    contentDescription = stringResource(R.string.history)
                )
            }
        )

        NavigationBarItem(
            selected = currentRoute == Route.Count,
            onClick = { onRoute(Route.Count) },
            icon = {
                Icon(
                    painter = painterResource(R.drawable.add),
                    contentDescription = stringResource(R.string.add)
                )
            }
        )

        NavigationBarItem(
            selected = currentRoute == Route.Home,
            onClick = { onRoute(Route.Home) },
            icon = {
                Icon(
                    painter = painterResource(R.drawable.home),
                    contentDescription = stringResource(R.string.home)
                )
            }
        )

        NavigationBarItem(
            selected = currentRoute == Route.Profile,
            onClick = { onRoute(Route.Profile) },
            icon = {
                Icon(
                    painter = painterResource(R.drawable.profile),
                    contentDescription = stringResource(R.string.profile)
                )
            }
        )
    }
}