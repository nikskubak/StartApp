package com.respire.startapp.network

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.respire.startapp.beacon.BeaconData

data class BeaconResponse(@Expose @SerializedName("beacon") var beaconData: BeaconData)