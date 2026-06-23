package com.kenji.food.tracker.ui.component

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.kenji.food.tracker.R
import com.kenji.food.tracker.ui.Route

@Composable
fun BottomBar(currentRoute: Route?, onRoute: (Route) -> Unit) {
    NavigationBar {
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