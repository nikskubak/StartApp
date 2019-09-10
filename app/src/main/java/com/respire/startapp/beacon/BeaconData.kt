package com.respire.startapp.beacon

import com.google.gson.annotations.Expose

data class BeaconData(
    @Expose var uuid: String?,
    @Expose var major: String?,
    @Expose var minor: String?,
    @Expose var rssi: String?,
    @Expose var proximity: String?,
    @Expose var inRange: Boolean
)