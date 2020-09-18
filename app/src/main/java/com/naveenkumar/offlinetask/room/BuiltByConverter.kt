package com.naveenkumar.offlinetask.room

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.naveenkumar.offlinetask.api.model.BuiltBy
import java.lang.reflect.Type


class BuiltByConverter {
    @TypeConverter
    fun stringToBuiltBy(json: String?): List<BuiltBy?>? {
        val gson = Gson()
        val type: Type =
            object : TypeToken<List<BuiltBy?>?>() {}.type
        return gson.fromJson(json, type)
    }

    @TypeConverter
    fun builtByToString(list: List<BuiltBy?>?): String? {
        val gson = Gson()
        val type: Type =
            object : TypeToken<List<BuiltBy?>?>() {}.type
        return gson.toJson(list, type)
    }

}