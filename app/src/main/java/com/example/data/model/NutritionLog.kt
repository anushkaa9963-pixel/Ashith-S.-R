package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "nutrition_logs")
data class NutritionLog(
    @PrimaryKey
    val dateKey: String, // format "yyyy-MM-dd"
    val calciumMg: Int = 0,     // Daily Target: ~1200 mg
    val vitaminDIu: Int = 0,    // Daily Target: ~800 IU
    val proteinG: Int = 0,       // Daily Target: ~80 g
    val waterMl: Int = 0        // Daily Target: ~2500 ml
) {
    companion object {
        const val TARGET_CALCIUM_MG = 1200
        const val TARGET_VITAMIN_D_IU = 800
        const val TARGET_PROTEIN_G = 85
        const val TARGET_WATER_ML = 2500
    }
}
