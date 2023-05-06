package com.respire.startapp.data.sources.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.respire.startapp.data.sources.database.models.DbModel

@Database(entities = [DbModel::class], version = 1, exportSchema = true)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getDbModelDao () : DbModelDao
}