package com.respire.startapp.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query

@Dao
interface EntityDao {

    @Query("SELECT * from Entity")
    fun getAll(): MutableList<Entity>?

    @Query("SELECT * from Entity where id = :id LIMIT 1")
    fun get(id : String): Entity?

    @Insert(onConflict = REPLACE)
    fun insertOrUpdate(entity: Entity) : Long

    @Insert(onConflict = REPLACE)
    fun insertAll(entities: List<Entity>?) : List<Long>

    @Query("DELETE from Entity")
    fun deleteAll()
}