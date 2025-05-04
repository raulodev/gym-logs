package com.raulodev.gymlogs.screens

import android.icu.util.Calendar
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.himanshoe.charty.bar.BarChart
import com.himanshoe.charty.bar.config.BarTooltip
import com.himanshoe.charty.bar.model.BarData
import com.himanshoe.charty.common.ChartColor
import com.himanshoe.charty.common.LabelConfig
import com.himanshoe.charty.pie.PieChart
import com.himanshoe.charty.pie.model.PieChartData
import com.raulodev.gymlogs.database.AppDatabase
import com.raulodev.gymlogs.enums.Routes
import com.raulodev.gymlogs.ui.theme.content4
import com.raulodev.gymlogs.ui.theme.danger
import com.raulodev.gymlogs.ui.theme.successLight
import com.raulodev.gymlogs.utils.Months
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun ChartScreen(
    db: AppDatabase? = null, navegationController: NavHostController? = null
) {

    val pieChartData = remember { mutableStateListOf<PieChartData>() }
    val barChartData = remember { mutableStateListOf<BarData>() }

    suspend fun countMembers() {
        try {
            val result = db?.userDao()?.countUsersByOwner()

            if (result != null) {

                val data = mutableListOf<PieChartData>()

                if (result.male > 0) {
                    data.add(
                        PieChartData(
                            ((result.male.toDouble() / result.total) * 100).toFloat(),
                            ChartColor.Solid(successLight),
                            label = if (result.male == 1) "${result.male} Hombre" else "${result.male} Hombres"
                        )
                    )
                }

                if (result.female > 0) {
                    data.add(
                        PieChartData(
                            ((result.female.toDouble() / result.total) * 100).toFloat(),
                            ChartColor.Solid(danger),
                            label = if (result.female == 1) "${result.female} Mujer" else "${result.female} Mujeres"
                        )
                    )
                }

                if (result.unknown > 0) {
                    data.add(
                        PieChartData(
                            ((result.unknown.toDouble() / result.total) * 100).toFloat(),
                            ChartColor.Solid(content4),
                            label = "${result.unknown} Sin definir"
                        )
                    )
                }
                pieChartData.addAll(data)
            }

        } catch (e: Exception) {
            Log.e("DevLogs", "Error: ${e.message}")
        }

    }

    suspend fun getPayments() {
        try {

            val rightNow = Calendar.getInstance()
            val year = rightNow.get(Calendar.YEAR)

            val result = db?.paymentDao()?.getByMonth(year = year)

            if (result != null) {

                val data = mutableListOf<BarData>()

                for (row in result) {
                    data.add(
                        BarData(
                            yValue = row.payments.toFloat(),
                            xValue = Months[row.month - 1],
                            barColor = ChartColor.Solid(successLight),
                        )
                    )
                }
                barChartData.addAll(data)
            }

        } catch (e: Exception) {
            Log.e("DevLogs", "Error: ${e.message}")
        }
    }


    LaunchedEffect(Unit) {
        try {
            withContext(Dispatchers.IO) {
                countMembers()
                getPayments()
            }
        } catch (e: Exception) {
            Log.e("DevLogs", "Error: ${e.message}")
        }
    }

    Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(20.dp)) {

        IconButton(onClick = {
            navegationController?.navigate(Routes.MembersScreen.name)
        }) {
            Icon(
                Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Charts",
                tint = successLight
            )
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(50.dp)
        ) {
            if (barChartData.isNotEmpty()) {
                item {
                    Column {
                        Text(
                            "Pagos por mes",
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth(),
                            fontWeight = FontWeight.Medium
                        )

                        BarChart(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(300.dp),
                            labelConfig = LabelConfig.default().copy(
                                showXLabel = true,
                                showYLabel = false,
                                xAxisCharCount = 3,
                                labelTextStyle = TextStyle(
                                    fontSize = 10.sp,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                            ),
                            barTooltip = BarTooltip.BarTop,
                            data = { barChartData }
                        )
                    }
                }
            }
            if (pieChartData.isNotEmpty()) {
                item {
                    Column {
                        Text(
                            "Total de usuarios",
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth(),
                            fontWeight = FontWeight.Medium
                        )

                        PieChart(
                            isDonutChart = false,
                            data = { pieChartData },
                            modifier = Modifier
                                .size(300.dp)
                                .padding(4.dp)
                        )
                    }
                }
            }
        }
    }
}