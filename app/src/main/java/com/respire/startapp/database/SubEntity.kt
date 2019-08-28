package com.respire.startapp.database

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import com.google.gson.annotations.Expose

data class SubEntity constructor(@Expose var id: String) : BaseObservable() {

    constructor() : this("")

    @Expose
    @get:Bindable
    var name: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.name)
        }
}