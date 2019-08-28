package com.respire.startapp.database

import android.os.Parcelable
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.util.*


@Entity(tableName = "Entity")
@Parcelize
data class Entity(@PrimaryKey @Expose var id: String) : BaseObservable(), Parcelable {

    constructor() : this("")

    @Expose
    @get:Bindable
    var name: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.name)
        }

    @Expose
    @get:Bindable
    var description: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.description)
        }

    @Expose
    @SerializedName("market_id")
    @get:Bindable
    var marketId: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.marketId)
        }

    @Expose
    @TypeConverters(DataTypeConverter::class)
    @get:Bindable
    var date: Date? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.date)
        }

    @Expose
    @get:Bindable
    var imageUrl: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.imageUrl)
        }

    @Expose
    @TypeConverters(DataTypeConverter::class)
    @get:Bindable
    var subEntities: MutableList<SubEntity>? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.subEntities)
        }

    @Expose
    @get:Bindable
    var type: Int? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.type)
        }

    @Expose
    @get:Bindable
    var dateS: String? = null
        set(value) {
            field = value
            notifyPropertyChanged(BR.dateS)
        }

    enum class Status {
        NEW, PROGRESS, FINISHED
    }
}