package com.respire.startapp.data.sources.database.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.respire.startapp.domain.models.DomainModel
import kotlinx.android.parcel.Parcelize


@Entity(tableName = "DbModel")
@Parcelize
data class DbModel(
    @PrimaryKey
    var id: String,
    var name: String? = null,
    var description: String? = null,
    @ColumnInfo(name = "market_id")
    var marketId: String? = null,
    @ColumnInfo(name = "image_url")
    var imageUrl: String? = null
) : Parcelable{
    constructor() : this("")
}

fun DbModel.mapToDomainModel(): DomainModel {
    return DomainModel(id, name, description, marketId, imageUrl)
}