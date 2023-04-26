package com.respire.startapp.data.database

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.util.*


@Entity(tableName = "Entity")
@Parcelize
data class Entity(
    @PrimaryKey
    @Expose var id: String,
    @Expose
    var name: String? = null,
    @Expose
    var description: String? = null,
    @Expose
    @SerializedName("market_id")
    var marketId: String? = null,
    @Expose
    var imageUrl: String? = null
) : Parcelable{
    constructor() : this("")
}