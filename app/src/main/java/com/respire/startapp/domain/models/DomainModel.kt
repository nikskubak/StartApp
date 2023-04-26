package com.respire.startapp.domain.models

data class DomainModel(
    var id: String,
    var name: String? = null,
    var description: String? = null,
    var marketId: String? = null,
    var imageUrl: String? = null
)
