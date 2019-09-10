package com.respire.startapp.beacon

import android.content.Context
import android.widget.Toast
import com.estimote.sdk.Beacon
import com.estimote.sdk.BeaconManager
import com.estimote.sdk.Region
import com.estimote.sdk.Utils
import java.util.*


class BeaconMonitor(var context: Context, var sendBeacon: (beacon: BeaconData) -> Unit) {

    var beaconManager: BeaconManager = BeaconManager(context)
    var uuid = UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D")
    var beaconsListForServer = mutableListOf<Beacon>()

    fun startMonitoring() {
        beaconManager.connect {
            beaconManager.startMonitoring(
                Region(
                    "monitored region",
                    uuid,
                    null, null
                )
            )
        }

        beaconManager.setMonitoringListener(object : BeaconManager.MonitoringListener {
            override
            fun onEnteredRegion(region: Region, beacons: List<Beacon>) {
                if (beacons.isNotEmpty()) {
                    Toast.makeText(
                        context,
                        "Detected beacon with major ${beacons[0].major}",
                        Toast.LENGTH_SHORT
                    ).show()
                    beacons.forEach { sendBeacon(buildBeaconData(it, true, Utils.computeAccuracy(it))) }
                }
            }

            override fun onExitedRegion(region: Region) {
                // could add an "exit" notification too if you want (-:
            }
        })
    }

    fun startRanging() {
        beaconManager.connect {
            beaconManager.startRanging(
                Region(
                    "monitored region",
                    uuid,
                    null, null
                )
            )
        }

        beaconManager.setRangingListener(BeaconManager.RangingListener() { region, list ->
            if (list.isNotEmpty()) {
                calculateBeacon(list)
            }
        })
    }

    private val rangeDistance = 200

    private fun calculateBeacon(beacons: MutableList<Beacon>) {
        if(beacons.isNullOrEmpty()) {
            for (i in 0 until beacons.size) {
                if (beaconsListForServer.isEmpty()) {
                    val proximity = Utils.computeAccuracy(beacons[i])
                    if (proximity < rangeDistance) {
                        beaconsListForServer.add(beacons[i])
                        sendBeacon(buildBeaconData(beacons[i], true, proximity))
                    }
                } else {
                    val proximity = Utils.computeAccuracy(beacons[i])
                    var existBeacon = getExistBeacon(beacons[i])
                    if (existBeacon != null) {
                        val existProximity = Utils.computeAccuracy(existBeacon)
                        if (proximity < rangeDistance) {
                            if (existProximity > rangeDistance) {
                                sendBeacon(buildBeaconData(beacons[i], true, proximity))
                                beaconsListForServer.remove(existBeacon)
                                beaconsListForServer.add(beacons[i])
                            } else {
                                //nothing
                            }
                        } else {
                            if (existProximity > rangeDistance) {
                                //nothing
                            } else {
                                sendBeacon(buildBeaconData(beacons[i], false, proximity))
                                beaconsListForServer.remove(existBeacon)
                                beaconsListForServer.add(beacons[i])
                            }
                        }
                    } else {
                        if (proximity < rangeDistance) {
                            beaconsListForServer.add(beacons[i])
                            sendBeacon(buildBeaconData(beacons[i], true, proximity))
                        }
                    }
                }
            }
        }else{
            if (beaconsListForServer.isEmpty()) {
                //nothing
            }else{
                for (i in 0 until beaconsListForServer.size){
                    sendBeacon(buildBeaconData(beaconsListForServer[i], false, -1.0))
                    beaconsListForServer.remove(beaconsListForServer[i])
                }
            }
        }
    }

    private fun getExistBeacon(beacon: Beacon): Beacon? {
        for (i in 0 until beaconsListForServer.size) {
            if (beaconsListForServer[i].equals(beacon)) {
                return beaconsListForServer[i]
            }
        }
        return null
    }

    private fun buildBeaconData(beacon: Beacon, inRange: Boolean, proximity: Double): BeaconData {
        return BeaconData(
            beacon.proximityUUID.toString(),
            beacon.major.toString(),
            beacon.minor.toString(),
            beacon.rssi.toString(),
            proximity.toString(),
            inRange
        )
    }
}