

package com.example.cachorro

import androidx.room.TypeConverter

class Converters {
    @TypeConverter
    fun fromString(value: String): List<String> {
        // Evita criar uma lista com um item vazio se a string for vazia
        return if (value.isEmpty()) emptyList() else value.split(",").map { it.trim() }
    }

    @TypeConverter
    fun fromList(list: List<String>): String {
        return list.joinToString(",")
    }
}