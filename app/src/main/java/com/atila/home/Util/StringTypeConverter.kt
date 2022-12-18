package com.atila.home.Util

import androidx.room.TypeConverter
import java.util.ArrayList

class StringTypeConverter {
    @TypeConverter
    fun fromString(value: String): ArrayList<String> {
        val list = value.split(",").toMutableList()
        return ArrayList(list)
    }

    @TypeConverter
    fun fromArrayList(list: ArrayList<String>): String {
        return list.joinToString(",")
    }
}