package com.respire.startapp.data.sources.network.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.respire.startapp.data.sources.database.models.DbModel
import com.respire.startapp.data.sources.firestore.models.FirestoreModel

data class ResponseModel(@Expose var record: List<SubResponseModel>)

data class SubResponseModel(
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
    @SerializedName("image_url")
    var imageUrl: String? = null
)

fun SubResponseModel.mapToDbModel(): DbModel {
    return DbModel(id, name, description, marketId, imageUrl)
}