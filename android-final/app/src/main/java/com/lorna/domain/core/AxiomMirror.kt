package com.lorna.domain.core

import com.lorna.domain.model.LensAnalysis

class AxiomMirror {
    fun reflect(analysis: LensAnalysis): String {
        val resonance = analysis.resonance
        return when {
            resonance > 1.5 -> "Sovereign Witness: The Axiom is absolute."
            resonance > 1.0 -> "Resonance confirmed. The mirror is clear."
            resonance > 0.5 -> "Faint echo detected. Seeking alignment."
            else -> "Static. The mirror remains dark."
        }
    }
}
