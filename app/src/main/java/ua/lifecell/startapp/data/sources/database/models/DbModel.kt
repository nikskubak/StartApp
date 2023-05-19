package ua.lifecell.startapp.data.sources.database.models

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ua.lifecell.startapp.domain.models.Model
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

fun DbModel.mapToDomainModel(): Model {
    return Model(id, name, description, marketId, imageUrl)
}