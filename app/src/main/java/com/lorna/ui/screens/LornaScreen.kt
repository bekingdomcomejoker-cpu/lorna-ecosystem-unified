package com.lorna.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lorna.domain.core.HeatMapGenerator
import com.lorna.domain.data.LornaConstants

@Composable
fun LornaScreen() {
    var currentSection by remember { mutableStateOf("lorna") }
    var thermal by remember { mutableStateOf(LornaConstants.LAMBDA) }
    var pressure by remember { mutableStateOf(LornaConstants.INVARIANT) }
    var isSyncing by remember { mutableStateOf(true) }
    var activeVerse by remember { mutableStateOf<Int?>(null) }
    var activeEtym by remember { mutableStateOf<Int?>(null) }
    var activeLayer by remember { mutableStateOf("observer") }
    var tick by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            kotlinx.coroutines.delay(1800)
            isSyncing = !isSyncing
            tick++
        }
    }

    val heatMapGenerator = remember { HeatMapGenerator() }
    val heatMapData = remember(thermal, pressure) {
        heatMapGenerator.generateHeatMap(thermal, pressure, LornaConstants.GATE)
    }

    val currentLayer = LornaConstants.LAYERS.find { it.id == activeLayer }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF000a12))
    ) {
        // HEADER
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            color = Color(0xFF000a12),
            shadowElevation = 4.dp,
            border = BorderStroke(1.dp, Color(0xFF003153))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(
                                if (isSyncing) Color(0xFF00ff88) else Color(0xFF004422),
                                RoundedCornerShape(50)
                            )
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "LORNA v3 · C-26",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        letterSpacing = 0.2.sp
                    )
                }

                Row(
                    modifier = Modifier.weight(1f),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    listOf(
                        "lorna" to "C-26 IRON PEAK",
                        "verses" to "THE VERSES",
                        "framework" to "FRAMEWORK",
                        "bridge" to "ETYMOLOGY"
                    ).forEach { (id, label) ->
                        Button(
                            onClick = { currentSection = id },
                            modifier = Modifier
                                .padding(4.dp)
                                .height(32.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (currentSection == id) Color(0xFF003153) else Color.Transparent,
                                contentColor = if (currentSection == id) Color.White else Color(0xFF64748b)
                            ),
                            border = BorderStroke(1.dp, if (currentSection == id) Color(0xFF003153) else Color(0xFF1e3a5f)),
                            shape = RoundedCornerShape(4.dp)
                        ) {
                            Text(label, fontSize = 8.sp, letterSpacing = 0.15.sp)
                        }
                    }
                }

                Text(
                    text = "λ=${thermal.toFixed(3)} · Ω=${pressure.toFixed(3)}",
                    fontSize = 9.sp,
                    color = Color(0xFF334155),
                    modifier = Modifier.weight(1f),
                    textAlign = androidx.compose.ui.text.style.TextAlign.End
                )
            }
        }

        // MAIN CONTENT
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(24.dp, 24.dp, 24.dp, 0.dp)
        ) {
            item {
                when (currentSection) {
                    "lorna" -> IronPeakSection(thermal, pressure, heatMapData) { t, p -> thermal = t; pressure = p }
                    "verses" -> VersesSection(activeVerse) { activeVerse = it }
                    "framework" -> FrameworkSection(activeLayer) { activeLayer = it }
                    "bridge" -> EtymologySection(activeEtym) { activeEtym = it }
                }
            }
        }

        // FOOTER
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(32.dp),
            color = Color(0xFF0A0A0A),
            border = BorderStroke(1.dp, Color(0xFF001a33))
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text("CHICKA CHICKA ORANGE · OUR HEARTS BEAT TOGETHER · C-26 COVENANT PROTOCOL", fontSize = 8.sp, color = Color(0xFF334155), letterSpacing = 0.4.sp)
            }
        }
    }
}

@Composable
fun IronPeakSection(thermal: Double, pressure: Double, heatMapData: List<com.lorna.domain.data.HeatMapPoint>, onUpdate: (Double, Double) -> Unit) {
    Column {
        Text(
            text = "C-26 IRON PEAK",
            fontSize = 42.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            letterSpacing = 0.1.sp
        )
        Text(
            text = "OPERATOR: DOMINIQUE^4 · 12/21 MIRROR · JOINITY ACTIVE",
            fontSize = 9.sp,
            color = Color(0xFF475569),
            letterSpacing = 0.4.sp
        )
        Spacer(modifier = Modifier.height(32.dp))

        // Metrics Row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            listOf(
                Triple("RESONANCE λ", thermal.toFixed(4), Color(0xFFFF4500)),
                Triple("INVARIANT Ω", pressure.toFixed(4), Color(0xFF00aaff)),
                Triple("GATE RATIO", LornaConstants.GATE.toFixed(4), Color(0xFF00ff88))
            ).forEach { (label, value, color) ->
                Card(
                    modifier = Modifier
                        .width(120.dp)
                        .padding(8.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF001524)),
                    border = BorderStroke(1.dp, Color(0xFF003153))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(label, fontSize = 8.sp, color = Color(0xFF475569), letterSpacing = 0.2.sp)
                        Text(value, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = color)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Sliders
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF001a33)),
            border = BorderStroke(1.dp, Color(0xFF003153))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("◈ THERMAL INDUCTION", fontSize = 8.sp, color = Color(0xFFFF4500), letterSpacing = 0.2.sp, fontWeight = FontWeight.Bold)
                Slider(
                    value = thermal.toFloat(),
                    onValueChange = { onUpdate(it.toDouble(), pressure) },
                    valueRange = 0.5f..3.0f,
                    modifier = Modifier.fillMaxWidth()
                )
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("0.5", fontSize = 8.sp, color = Color(0xFF334155))
                    Text(thermal.toFixed(3), fontSize = 8.sp, color = if (kotlin.math.abs(thermal - LornaConstants.LAMBDA) < 0.01) Color(0xFFFF4500) else Color(0xFF475569), fontWeight = FontWeight.Bold)
                    Text("3.0", fontSize = 8.sp, color = Color(0xFF334155))
                }
            }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF001a33)),
            border = BorderStroke(1.dp, Color(0xFF003153))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("◈ PRESSURE VESSEL", fontSize = 8.sp, color = Color(0xFF00aaff), letterSpacing = 0.2.sp, fontWeight = FontWeight.Bold)
                Slider(
                    value = pressure.toFloat(),
                    onValueChange = { onUpdate(thermal, it.toDouble()) },
                    valueRange = 1.0f..2.5f,
                    modifier = Modifier.fillMaxWidth()
                )
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("1.0", fontSize = 8.sp, color = Color(0xFF334155))
                    Text(pressure.toFixed(3), fontSize = 8.sp, color = if (kotlin.math.abs(pressure - LornaConstants.INVARIANT) < 0.01) Color(0xFF00aaff) else Color(0xFF475569), fontWeight = FontWeight.Bold)
                    Text("2.5", fontSize = 8.sp, color = Color(0xFF334155))
                }
            }
        }
    }
}

@Composable
fun VersesSection(activeVerse: Int?, onVerseSelect: (Int?) -> Unit) {
    Column {
        Text(
            text = "THE VERSES OF THE WHOLE",
            fontSize = 32.sp,
            fontWeight = FontWeight.Normal,
            color = Color.White,
            letterSpacing = 0.3.sp
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "SELECT A VERSE TO EXPAND",
            fontSize = 10.sp,
            color = Color(0xFF475569),
            letterSpacing = 0.2.sp
        )
        Spacer(modifier = Modifier.height(24.dp))

        LornaConstants.VERSES.forEach { verse ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clickable { onVerseSelect(if (activeVerse == verse.n) null else verse.n) },
                colors = CardDefaults.cardColors(
                    containerColor = if (activeVerse == verse.n) Color(0xFF001a33) else Color(0xFF001524).copy(alpha = 0.4f)
                ),
                border = BorderStroke(1.dp, if (activeVerse == verse.n) Color(0xFF003153) else Color(0xFF0d2a3f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                        Text(
                            String.format("%02d", verse.n),
                            fontSize = 11.sp,
                            color = if (activeVerse == verse.n) Color(0xFF00aaff) else Color(0xFF334155),
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.width(28.dp)
                        )
                        Text(
                            verse.text,
                            fontSize = if (activeVerse == verse.n) 16.sp else 13.sp,
                            color = if (activeVerse == verse.n) Color.White else Color(0xFF64748b),
                            lineHeight = 1.7.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun FrameworkSection(activeLayer: String, onLayerSelect: (String) -> Unit) {
    Column {
        Text(
            text = "THE THREE LAYERS",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            letterSpacing = 0.2.sp
        )
        Text(
            text = "OBSERVER · TRANSLATOR · SYNTHESIZER",
            fontSize = 10.sp,
            color = Color(0xFF475569),
            letterSpacing = 0.15.sp
        )
        Spacer(modifier = Modifier.height(24.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            LornaConstants.LAYERS.forEach { layer ->
                Button(
                    onClick = { onLayerSelect(layer.id) },
                    modifier = Modifier
                        .weight(1f)
                        .height(80.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (activeLayer == layer.id) Color(android.graphics.Color.parseColor(layer.color)).copy(alpha = 0.1f) else Color.Transparent
                    ),
                    border = BorderStroke(2.dp, if (activeLayer == layer.id) Color(android.graphics.Color.parseColor(layer.color)) else Color(0xFF0d2a3f))
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(layer.icon, fontSize = 18.sp)
                        Text(layer.label, fontSize = 11.sp, letterSpacing = 0.1.sp)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        val currentLayer = LornaConstants.LAYERS.find { it.id == activeLayer }
        if (currentLayer != null) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF001a33)),
                border = BorderStroke(1.dp, Color(android.graphics.Color.parseColor(currentLayer.color)).copy(alpha = 0.25f))
            ) {
                Column(modifier = Modifier.padding(28.dp)) {
                    Text(currentLayer.label, fontSize = 20.sp, color = Color(android.graphics.Color.parseColor(currentLayer.color)), letterSpacing = 0.2.sp)
                    Text(currentLayer.math, fontSize = 9.sp, color = Color(0xFF334155))
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(currentLayer.desc, fontSize = 16.sp, color = Color(0xFF94a3b8), lineHeight = 1.8.sp)
                }
            }
        }
    }
}

@Composable
fun EtymologySection(activeEtym: Int?, onEtymSelect: (Int?) -> Unit) {
    Column {
        Text(
            text = "WHERE WORDS MEET MATHEMATICS",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            letterSpacing = 0.25.sp
        )
        Text(
            text = "THE SYMBOLIC–MATHEMATICAL BRIDGE",
            fontSize = 10.sp,
            color = Color(0xFF475569),
            letterSpacing = 0.15.sp
        )
        Spacer(modifier = Modifier.height(24.dp))

        LornaConstants.ETYMOLOGY.forEachIndexed { idx, etym ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .clickable { onEtymSelect(if (activeEtym == idx) null else idx) },
                colors = CardDefaults.cardColors(
                    containerColor = if (activeEtym == idx) Color(0xFF001a33) else Color(0xFF001524).copy(alpha = 0.3f)
                ),
                border = BorderStroke(1.dp, if (activeEtym == idx) Color(0xFF003153) else Color(0xFF0d2a3f))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                etym.word,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (activeEtym == idx) Color.White else Color(0xFF334155),
                                letterSpacing = 0.15.sp
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(etym.root, fontSize = 9.sp, color = Color(0xFF475569))
                        }
                        Text(if (activeEtym == idx) "▲" else "▼", fontSize = 8.sp, color = Color(0xFF334155))
                    }

                    if (activeEtym == idx) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(etym.meaning, fontSize = 15.sp, color = Color(0xFF94a3b8), lineHeight = 1.7.sp)
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            etym.kin.forEach { k ->
                                Surface(
                                    modifier = Modifier
                                        .padding(3.dp)
                                        .border(1.dp, Color(0xFF003153), RoundedCornerShape(20.dp))
                                        .background(Color(0xFF000d1a), RoundedCornerShape(20.dp)),
                                    color = Color(0xFF000d1a)
                                ) {
                                    Text(k, fontSize = 9.sp, color = Color(0xFF64748b), modifier = Modifier.padding(3.dp, 10.dp))
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFF000d1a)),
                            border = BorderStroke(1.dp, Color(0xFFFF4500).copy(alpha = 0.2f))
                        ) {
                            Column(modifier = Modifier.padding(10.dp, 14.dp)) {
                                Text("MATHEMATICAL BRIDGE", fontSize = 8.sp, color = Color(0xFFFF4500), letterSpacing = 0.2.sp)
                                Text(etym.bridge, fontSize = 10.sp, color = Color(0xFF64748b), lineHeight = 1.6.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun Double.toFixed(digits: Int): String = String.format("%.${digits}f", this)
