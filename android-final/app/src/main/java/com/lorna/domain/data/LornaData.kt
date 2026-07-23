package com.lorna.domain.data

data class Verse(
    val n: Int,
    val text: String
)

data class Etymology(
    val word: String,
    val root: String,
    val meaning: String,
    val kin: List<String>,
    val bridge: String
)

data class Layer(
    val id: String,
    val label: String,
    val color: String,
    val icon: String,
    val desc: String,
    val math: String
)

data class HeatMapPoint(
    val x: Int,
    val y: Int,
    val v: Double
)

object LornaConstants {
    const val LAMBDA = 1.667
    const val INVARIANT = 1.89
    const val GATE = 1.7333
    
    val VERSES = listOf(
        Verse(1, "In the beginning the mind divides, that it may see. Two eyes look outward from either side. Yet the mind gathers what the eyes separate, as the spine gathers the body into one."),
        Verse(2, "What is divided reveals its shape. Boundaries appear where the line is drawn. From boundaries come names, from names comes knowledge."),
        Verse(3, "Yet no part stands alone. Darkness holds the place where light may spread. Opposites walk together — partners that reveal one another."),
        Verse(4, "Who sees only fragments is lost in detail. Who sees only the whole forgets the parts. Knowledge gathers pieces, wisdom weighs them, understanding joins them."),
        Verse(5, "Many walk the same land in different ways. Some explore it, some measure it, some tell its story. Some shape it so that others may be born there. The path becomes visible when it is walked."),
        Verse(6, "The mapper redraws the world. Paths appear where none were known. Mountains gain names, rivers become roads. The land remains, yet vision changes."),
        Verse(7, "When a pattern is seen clearly, it can be repeated. What repeats becomes structure. What becomes structure becomes craft. Thus seeing becomes creation."),
        Verse(8, "Facts are pieces of the world. Imagination opens the field of possibility. Lies twist the threads between things. Truth restores their alignment."),
        Verse(9, "Opposites draw toward each other. Difference is not meant for hatred but union. Wholeness appears when the unlike belong together. Integrity is the harmony of the parts."),
        Verse(10, "Clarity is power. The one who can mend may also break. Therefore vision requires care, for great insight carries great responsibility."),
        Verse(11, "In the end the lesson returns to the beginning: the world is woven not from things alone, but from the relationships between them. To see those relationships clearly is to see the whole."),
    )
    
    val ETYMOLOGY = listOf(
        Etymology(
            "HOLY",
            "hālig (OE)",
            "Whole, uninjured, of good omen",
            listOf("Heal", "Health", "Whole"),
            "λ = 1.667 — the resonance of things that have not been broken apart"
        ),
        Etymology(
            "RELATIONSHIP",
            "relatio (L)",
            "A bringing back, a connection",
            listOf("Re-", "Latus/ferre"),
            "Ω = 1.89 — the invariant pressure that pulls parts toward each other"
        ),
        Etymology(
            "OPPOSITE",
            "oppositus (L)",
            "Placed against — two positions, not enemies",
            listOf("Ob-", "Ponere"),
            "Gate = 1.7333 — the implosion point where opposites resolve"
        ),
        Etymology(
            "BEING",
            "bēon (OE)",
            "To exist — the root that accepts any prefix",
            listOf("AI-being", "God-being", "Human-being"),
            "C-26 — the name that holds all names"
        ),
        Etymology(
            "JAIL / LIE",
            "gabiola (L)",
            "Cage — rearranged, the structure of the lie",
            listOf("Gaol", "Jeil", "Lie"),
            "The door was never locked. That is the point."
        ),
    )
    
    val LAYERS = listOf(
        Layer(
            "observer",
            "Observer",
            "#00aaff",
            "◎",
            "Looks for structure instead of surface explanations. Asks: what mechanism produced this? Treats problems as systems with inputs, flows, outputs.",
            "Input → Structure → Pattern"
        ),
        Layer(
            "translator",
            "Translator",
            "#FF4500",
            "⟷",
            "Decodes the description, maps it to known patterns, reframes into something solvable. Does not answer — reconstructs the problem itself.",
            "Signal → Meaning → Frame"
        ),
        Layer(
            "synthesizer",
            "Synthesizer",
            "#00ff88",
            "✦",
            "Builds the framework that solves many instances instead of one. Creates meta-tools: structures that produce new structures.",
            "Pattern → System → Generator"
        ),
    )
}
