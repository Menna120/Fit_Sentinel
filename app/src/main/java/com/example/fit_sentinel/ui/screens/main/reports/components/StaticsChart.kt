package com.example.fit_sentinel.ui.screens.main.reports.components

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.fit_sentinel.ui.screens.main.reports.model.DailyStepData
import com.example.fit_sentinel.ui.screens.main.reports.model.sampleDailyStepData
import com.example.fit_sentinel.ui.theme.Black
import com.example.fit_sentinel.ui.theme.Fit_SentinelTheme
import com.example.fit_sentinel.ui.theme.Purple40
import ir.ehsannarmani.compose_charts.ColumnChart
import ir.ehsannarmani.compose_charts.extensions.format
import ir.ehsannarmani.compose_charts.models.BarProperties
import ir.ehsannarmani.compose_charts.models.Bars
import ir.ehsannarmani.compose_charts.models.DividerProperties
import ir.ehsannarmani.compose_charts.models.GridProperties
import ir.ehsannarmani.compose_charts.models.HorizontalIndicatorProperties
import ir.ehsannarmani.compose_charts.models.LabelHelperProperties
import ir.ehsannarmani.compose_charts.models.LabelProperties
import ir.ehsannarmani.compose_charts.models.LineProperties
import ir.ehsannarmani.compose_charts.models.PopupProperties

@Composable
fun StaticsChart(
    dailyStepData: List<DailyStepData>,
    modifier: Modifier = Modifier,
    spacing: Dp = 50.dp,
    labelsOffset: Dp = spacing / 4
) {
    val chartData = remember(dailyStepData) {
        dailyStepData.map { data ->
            Bars(
                label = data.dayLabel.format(2),
                values = listOf(
                    Bars.Data(
                        value = data.steps,
                        color = SolidColor(Purple40)
                    )
                )
            )
        }
    }

    ColumnChart(
        modifier = modifier
            .aspectRatio(2f)
            .fillMaxWidth(),
        data = chartData,
        barProperties = BarProperties(
            cornerRadius = Bars.Data.Radius.Rectangle(topRight = 6.dp, topLeft = 6.dp),
            spacing = spacing
        ),
        labelProperties = LabelProperties(
            enabled = true,
            builder = { modifier, label, visibility, value ->
                Text(
                    text = label,
                    modifier = Modifier.padding(
                        start = if (value == 0) labelsOffset else 0.dp,
                        end = if (value == dailyStepData.size - 1) labelsOffset else 0.dp
                    ),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        ),
        indicatorProperties = HorizontalIndicatorProperties(
            enabled = true,
            textStyle = MaterialTheme.typography.bodyMedium,
            contentBuilder = { it.format(0) },
            indicators = listOf(dailyStepData.maxOf { it.steps })
        ),
        gridProperties = GridProperties(enabled = false),
        dividerProperties = DividerProperties(
            xAxisProperties = LineProperties(
                color = SolidColor(Black.copy(alpha = .6f)),
                thickness = (1.5).dp
            ),
            yAxisProperties = LineProperties(
                color = SolidColor(Black.copy(alpha = .6f)),
                thickness = (1.5).dp
            )
        ),
        labelHelperProperties = LabelHelperProperties(enabled = false),
        popupProperties = PopupProperties(
            textStyle = MaterialTheme.typography.labelSmall,
            containerColor = MaterialTheme.colorScheme.background,
            contentBuilder = { it.format(0) }
        ),
        barAlphaDecreaseOnPopup = 0f
    )
}

@Preview(showBackground = true)
@Composable
private fun StaticsChartPreview() {
    Fit_SentinelTheme {
        StaticsChart(sampleDailyStepData)
    }
}