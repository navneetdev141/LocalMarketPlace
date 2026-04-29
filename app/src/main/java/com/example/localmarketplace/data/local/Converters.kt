package com.example.localmarketplace.data.local

import android.R.attr.data
import androidx.room.TypeConverter

class Converters {

    @TypeConverter
    fun fromList(list: List<String>): String{
        return list.joinToString("|")
    }

    @TypeConverter
    fun toString(data: String): List<String>{
        return if(data.isEmpty()) emptyList()  else data.split("|")
    }
}