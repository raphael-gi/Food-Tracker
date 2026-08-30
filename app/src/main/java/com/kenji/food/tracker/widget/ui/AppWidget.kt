package com.kenji.food.tracker.widget.ui

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceModifier
import androidx.glance.GlanceTheme
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.LocalContext
import androidx.glance.appwidget.LinearProgressIndicator
import androidx.glance.appwidget.appWidgetBackground
import androidx.glance.background
import androidx.glance.layout.Alignment
import androidx.glance.layout.Box
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.Spacer
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.fillMaxWidth
import androidx.glance.layout.padding
import androidx.glance.layout.width
import androidx.glance.text.Text
import com.kenji.food.tracker.R
import com.kenji.food.tracker.widget.repository.WidgetRepository
import com.kenji.food.tracker.widget.repository.WidgetState
import com.kenji.food.tracker.widget.repository.WidgetUIState

@Composable
fun AppWidget(repository: WidgetRepository) {
    val state by repository.state.collectAsState(WidgetUIState.Loading)

    Box(
        modifier = GlanceModifier.fillMaxSize().background(GlanceTheme.colors.background),
        contentAlignment = Alignment.Center
    ) {
        when (val state = state) {
            WidgetUIState.Loading -> Text("Loading...")
            WidgetUIState.NoTarget -> Text(text = stringResource(R.string.completeSetupMessage))
            is WidgetState -> AppWidgetContent(
                targetCalories = state.target.calories,
                currentCalories = state.caloriesToday,
                targetProteins = state.target.protein,
                currentProteins = state.proteinsToday
            )
        }
    }
}

@Composable
private fun AppWidgetContent(
    targetCalories: Int,
    currentCalories: Int,
    targetProteins: Int?,
    currentProteins: Double
) {
    Column(modifier = GlanceModifier.padding(10.dp)) {
        ProgressBar(
            progress = currentCalories.toFloat() / targetCalories,
            icon = R.drawable.calories,
            iconDescription = R.string.calories,
        )

        if (targetProteins != null) {
            Spacer(modifier = GlanceModifier.defaultWeight())

            ProgressBar(
                progress = currentProteins.toFloat() / targetProteins,
                icon = R.drawable.protein,
                iconDescription = R.string.protein
            )
        }
    }
}

@Composable
private fun ProgressBar(progress: Float, @DrawableRes icon: Int, @StringRes iconDescription: Int) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Image(
            provider = ImageProvider(icon),
            contentDescription = stringResource(iconDescription)
        )

        Spacer(modifier = GlanceModifier.width(10.dp))

        LinearProgressIndicator(
            modifier = GlanceModifier.fillMaxWidth(),
            backgroundColor = GlanceTheme.colors.widgetBackground,
            color = GlanceTheme.colors.secondary,
            progress = progress
        )
    }
}

@Composable
private fun stringResource(@StringRes id: Int): String {
    return LocalContext.current.getString(id)
}

