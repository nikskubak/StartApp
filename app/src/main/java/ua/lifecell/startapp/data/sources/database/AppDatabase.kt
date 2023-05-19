package ua.lifecell.startapp.data.sources.database

import androidx.room.Database
import androidx.room.RoomDatabase
import ua.lifecell.startapp.data.sources.database.models.DbModel

@Database(entities = [DbModel::class], version = 1, exportSchema = true)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getDbModelDao () : DbModelDao
}