package com.respire.startapp.repositories

import androidx.lifecycle.LiveData
import com.respire.startapp.base.Result
import com.respire.startapp.database.Entity

interface EntityRepository {
    fun getEntities(isConnected : Boolean) : LiveData<Result<MutableList<Entity>>>
}