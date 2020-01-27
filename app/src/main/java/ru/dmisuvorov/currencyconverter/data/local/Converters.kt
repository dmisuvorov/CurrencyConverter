package ru.dmisuvorov.currencyconverter.data.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    @TypeConverter
    fun mapToString(map: Map<String, Double>): String = Gson().toJson(map)

    @TypeConverter
    fun stringToMap(string: String): Map<String, Double> =
        Gson().fromJson(string, object : TypeToken<Map<String, Double>>() {}.type)
}