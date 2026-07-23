package com.lorna.domain.core

import com.lorna.domain.data.HeatMapPoint
import kotlin.math.cos
import kotlin.math.sin

class HeatMapGenerator {
    fun generateHeatMap(thermal: Double, pressure: Double, gate: Double): List<HeatMapPoint> {
        val data = mutableListOf<HeatMapPoint>()
        for (i in 0 until 15) {
            for (j in 0 until 15) {
                val val_ = sin(i * thermal) * cos(j * pressure) * gate
                data.add(HeatMapPoint(i, j, val_))
            }
        }
        return data
    }
}
