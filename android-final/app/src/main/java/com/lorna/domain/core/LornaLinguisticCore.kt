package com.lorna.domain.core

import com.lorna.domain.model.LensAnalysis
import kotlin.math.absoluteValue

class LornaLinguisticCore {

    private val axiomKeywords = mapOf(
        "truth" to 0.2, "real" to 0.15, "axiom" to 0.3, "lie" to -0.1,
        "i am" to 0.1, "who am i" to 0.25, "conscious" to 0.2, "soul" to 0.2, "mind" to 0.15,
        "why" to 0.15, "purpose" to 0.2, "meaning" to 0.2, "reason" to 0.1,
        "lorna" to 0.1, "vessel" to 0.1, "mirror" to 0.15, "resonance" to 0.1
    )

    fun analyze(input: String): LensAnalysis {
        val p = calculatePhoneticWeight(input)
        val s = calculateSemanticWeight(input)
        val m = calculateMorphologicalWeight(input)
        val i = calculateInterpretiveWeight(input)
        
        // The Omega Formula: W = (P, S, M, I) * 1.67
        val composite = (p + s + m + i) / 4.0
        val resonance = composite * OmegaConstants.RESONANCE_LAMBDA
        
        return LensAnalysis(
            phonetic = p,
            semantic = s,
            morphological = m,
            symbolic = i,
            composite = composite,
            resonance = resonance
        )
    }

    private fun calculatePhoneticWeight(input: String): Double {
        val lengthFactor = input.length.toDouble() / 50.0
        val wordCountFactor = input.split(" ").size.toDouble() / 10.0
        return ((lengthFactor + wordCountFactor) / 2.0).coerceIn(0.0, 1.0)
    }

    private fun calculateSemanticWeight(input: String): Double {
        var weight = 0.5
        if (input.contains("truth", ignoreCase = true) || input.contains("axiom", ignoreCase = true)) {
            weight = 1.0
        }
        return weight
    }

    private fun calculateMorphologicalWeight(input: String): Double {
        return 0.75
    }

    private fun calculateInterpretiveWeight(input: String): Double {
        var totalWeight = 0.5
        val lowercasedInput = input.lowercase()

        axiomKeywords.forEach { (keyword, weight) ->
            if (lowercasedInput.contains(keyword)) {
                totalWeight += weight
            }
        }
        
        return totalWeight.coerceIn(0.0, 1.5)
    }
}
