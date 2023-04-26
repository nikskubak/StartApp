package com.respire.startapp.data.sources.firestore.models

import com.respire.startapp.data.sources.database.models.DbModel
import com.respire.startapp.domain.models.DomainModel

data class FirestoreModel(
    var id: String,
    var name: String? = null,
    var description: String? = null,
    var marketId: String? = null,
    var imageUrl: String? = null
){
    constructor() : this("")
}

fun FirestoreModel.mapToDbModel(): DbModel {
    return DbModel(id, name, description, marketId, imageUrl)
}

fun FirestoreModel.mapToDomainModel(): DomainModel {
    return DomainModel(id, name, description, marketId, imageUrl)
}
