package com.respire.startapp.data.sources.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.respire.startapp.data.sources.database.models.DbModel

@Dao
interface DbModelDao {

    @Query("SELECT * from DbModel")
    fun getAll(): MutableList<DbModel>?

    @Query("SELECT * from DbModel where id = :id LIMIT 1")
    fun get(id : String): DbModel?

    @Insert(onConflict = REPLACE)
    fun insertOrUpdate(entity: DbModel) : Long

    @Insert(onConflict = REPLACE)
    fun insertAll(entities: List<DbModel>?) : List<Long>

    @Query("DELETE from DbModel")
    fun deleteAll()
}