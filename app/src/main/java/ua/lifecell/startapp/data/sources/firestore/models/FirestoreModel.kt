package ua.lifecell.startapp.data.sources.firestore.models

import ua.lifecell.startapp.data.sources.database.models.DbModel
import ua.lifecell.startapp.domain.models.Model

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

fun FirestoreModel.mapToDomainModel(): Model {
    return Model(id, name, description, marketId, imageUrl)
}
