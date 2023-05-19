package ua.lifecell.startapp.data.sources.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import ua.lifecell.startapp.data.sources.database.models.DbModel
import kotlinx.coroutines.flow.Flow

@Dao
interface DbModelDao {

    @Query("SELECT * from DbModel")
    fun getAll(): MutableList<DbModel>?

    @Query("SELECT * from DbModel where id = :id LIMIT 1")
    fun get(id : String): Flow<DbModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdate(entity: DbModel) : Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(entities: List<DbModel>?) : List<Long>

    @Query("DELETE from DbModel")
    fun deleteAll()
}