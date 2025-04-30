package com.example.landrenting.data

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.example.landrenting.models.ApprovalStatus
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

@ProvidedTypeConverter
class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromApprovalStatus(value: ApprovalStatus): String {
        return value.name
    }

    @TypeConverter
    fun toApprovalStatus(value: String): ApprovalStatus {
        return ApprovalStatus.valueOf(value)
    }

    @TypeConverter
    fun fromStringList(value: List<String>): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toStringList(value: String): List<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(value, listType)
    }
}