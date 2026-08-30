package com.kenji.food.tracker.widget

import androidx.glance.appwidget.GlanceAppWidgetReceiver
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WidgetReceiver : GlanceAppWidgetReceiver() {
    override val glanceAppWidget = AppWidget()
}