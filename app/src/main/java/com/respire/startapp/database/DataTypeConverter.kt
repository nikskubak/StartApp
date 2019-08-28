package com.respire.startapp.database

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

class DataTypeConverter {

    companion object {
        val gson: Gson = Gson()

        @TypeConverter
        @JvmStatic
        fun toJsonSubEntities(subEntities: List<SubEntity>?): String? {
            return gson.toJson(subEntities)
        }

        @TypeConverter
        @JvmStatic
        fun fromJsonSubEntities(json: String?): List<SubEntity>? {
            val listType = object : TypeToken<List<SubEntity>>() {}.type
            return gson.fromJson(json, listType)
        }

        @TypeConverter
        @JvmStatic
        fun toDate(dateLong: Long?): Date? {
            return if (dateLong == null) null else Date(dateLong)
        }

        @TypeConverter
        @JvmStatic
        fun fromDate(date: Date?): Long? {
            return date?.time
        }

        @TypeConverter
        @JvmStatic
        fun statusToTnt(value: Entity.Status) = value.ordinal

        @TypeConverter
        @JvmStatic
        fun intToStatus(value: Int) = Entity.Status.values()[value]
    }
}