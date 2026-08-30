package com.kenji.food.tracker.widget

import android.content.Context
import androidx.glance.GlanceId
import androidx.glance.GlanceTheme
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.material3.ColorProviders
import com.kenji.food.tracker.ui.theme.DarkColorScheme
import com.kenji.food.tracker.ui.theme.LightColorScheme
import com.kenji.food.tracker.widget.repository.WidgetRepository
import com.kenji.food.tracker.widget.ui.AppWidget

class AppWidget : GlanceAppWidget() {
    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val repository = WidgetRepository.get(context)

        provideContent {
            GlanceTheme(colors = ColorProviders(light = LightColorScheme, dark = DarkColorScheme)) {
                AppWidget(repository)
            }
        }
    }
}