package com.kenji.food.tracker.ui.component.chart

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kenji.food.tracker.R
import com.kenji.food.tracker.entity.FoodPerDay
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.compose.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.compose.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.compose.cartesian.data.CartesianLayerRangeProvider
import com.patrykandpatrick.vico.compose.cartesian.data.ColumnCartesianLayerModel
import com.patrykandpatrick.vico.compose.cartesian.data.columnModel
import com.patrykandpatrick.vico.compose.cartesian.decoration.HorizontalLine
import com.patrykandpatrick.vico.compose.cartesian.layer.ColumnCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.common.Fill
import com.patrykandpatrick.vico.compose.common.component.LineComponent
import com.patrykandpatrick.vico.compose.common.component.rememberLineComponent
import com.patrykandpatrick.vico.compose.common.data.ExtraStore

@Composable
fun WeekOverviewChart(
    foodPerDay: List<FoodPerDay>,
    getKey: (FoodPerDay) -> Int,
    @StringRes title: Int,
    currentTarget: Int,
    valuePadding: Int
) {
    val modelProducer = remember { CartesianChartModelProducer() }

    LaunchedEffect(foodPerDay) {
        modelProducer.runTransaction {
            columnModel { series(foodPerDay.map(getKey)) }
        }
    }

    val labels = foodPerDay.map { perDay ->
        val dayRes = getShortDayOfWeek(perDay.day)
        if (dayRes != null) stringResource(dayRes)
        else ""
    }

    val columnShape = rememberLineComponent(
        fill = Fill(MaterialTheme.colorScheme.tertiary),
        thickness = 8.dp,
        shape = RoundedCornerShape(topStart = 2.dp, topEnd = 2.dp),
    )

    Column(modifier = Modifier.padding(10.dp)) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(title),
            textAlign = TextAlign.Center
        )

        CartesianChartHost(
            modifier = Modifier.fillMaxSize(),
            chart = rememberCartesianChart(
                rememberColumnCartesianLayer(
                    columnProvider = remember {
                        object : ColumnCartesianLayer.ColumnProvider {
                            override fun getColumn(
                                entry: ColumnCartesianLayerModel.Entry,
                                extraStore: ExtraStore
                            ) = columnShape

                            override fun getWidestSeriesColumn(
                                seriesKey: Any,
                                seriesIndex: Int,
                                extraStore: ExtraStore
                            ): LineComponent {
                                return columnShape
                            }
                        }
                    },
                    rangeProvider = remember {
                        CartesianLayerRangeProvider.fixed(
                            minY = 0.0,
                            maxY = maxOf(
                                currentTarget.toDouble(),
                                *foodPerDay.map { getKey(it).toDouble() }.toTypedArray()
                            ) + valuePadding
                        )
                    }
                ),
                startAxis = VerticalAxis.rememberStart(),
                bottomAxis = HorizontalAxis.rememberBottom(
                    valueFormatter = { _, i, _ ->
                        labels[i.toInt()]
                    }
                ),
                decorations = listOf(
                    HorizontalLine(
                        y = { currentTarget.toDouble() },
                        line = rememberLineComponent(
                            fill = Fill(MaterialTheme.colorScheme.secondary),
                            thickness = 2.dp
                        )
                    )
                ),
            ),
            modelProducer = modelProducer,
        )
    }
}

@StringRes
private fun getShortDayOfWeek(day: Int): Int? {
    return when (day) {
        0 -> R.string.mondayShort
        1 -> R.string.tuesdayShort
        2 -> R.string.wednesdayShort
        3 -> R.string.thursdayShort
        4 -> R.string.fridayShort
        5 -> R.string.saturdayShort
        6 -> R.string.sundayShort
        else -> null
    }
}
