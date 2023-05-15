package com.respire.startapp.data.sources.network.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.respire.startapp.data.sources.database.models.DbModel
import com.respire.startapp.domain.models.Model

data class ApiModel(@Expose var record: List<ApiSubModel>)

data class ApiSubModel(
    @Expose
    var id: String,
    @Expose
    var name: String? = null,
    @Expose
    var description: String? = null,
    @Expose
    @SerializedName("market_id")
    var marketId: String? = null,
    @Expose
    @SerializedName("imageUrl")
    var imageUrl: String? = null
)

fun ApiSubModel.mapToDbModel(): DbModel {
    return DbModel(id, name, description, marketId, imageUrl)
}

fun ApiSubModel.mapToDomainModel(): Model {
    return Model(id, name, description, marketId, imageUrl)
}