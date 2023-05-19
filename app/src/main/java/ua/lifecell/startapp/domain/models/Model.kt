package ua.lifecell.startapp.domain.models

data class Model(
    var id: String,
    var name: String? = null,
    var description: String? = null,
    var marketId: String? = null,
    var imageUrl: String? = null
)
