package com.example.data.model

data class Badge(
    val id: String,
    val title: String,
    val description: String,
    val iconName: String,
    val isUnlocked: Boolean = false,
    val progress: Float = 0f, // 0.0 to 1.0
    val progressLabel: String = ""
)

data class MotivationalQuote(
    val quote: String,
    val author: String,
    val category: String
)

object FitnessQuotes {
    val QUOTES = listOf(
        MotivationalQuote(
            "Every millimeter of spinal extension starts with daily decompression and unwavering posture discipline.",
            "Dr. Marcus Vance, Orthopedic Specialist",
            "Spine Science"
        ),
        MotivationalQuote(
            "Stand tall, breathe deep, and let gravity lose its battle against your spine.",
            "Posture & Kinetic Alignment",
            "Mindset"
        ),
        MotivationalQuote(
            "Growth hormone peaks when your body enters deep slow-wave sleep. Guard your 10 PM to 2 AM rest window.",
            "Chronobiology & Endocrinology Research",
            "HGH Optimization"
        ),
        MotivationalQuote(
            "Good posture adds immediate stature, confidence, and restores compressed disc height naturally.",
            "Biomechanics Institute",
            "Posture"
        ),
        MotivationalQuote(
            "Consistency beats intensity. Ten minutes of daily spinal decompression preserves vertical stature for a lifetime.",
            "Kinetic Chain Journal",
            "Consistency"
        ),
        MotivationalQuote(
            "Feed the bone matrix with calcium, vitamin D, and pure hydration to stimulate cellular density.",
            "Nutrition for Growth Plates",
            "Nutrition"
        )
    )

    fun getRandomQuote(): MotivationalQuote {
        val dayOfYear = java.util.Calendar.getInstance().get(java.util.Calendar.DAY_OF_YEAR)
        return QUOTES[dayOfYear % QUOTES.size]
    }
}
