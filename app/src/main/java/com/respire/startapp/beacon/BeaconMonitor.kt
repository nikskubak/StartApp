package com.respire.startapp.beacon

import android.content.Context
import android.util.Log
import com.estimote.sdk.Beacon
import com.estimote.sdk.BeaconManager
import com.estimote.sdk.Region
import com.estimote.sdk.Utils
import java.util.*


class BeaconMonitor(var context: Context, var sendBeacon: (beacon: BeaconData) -> Unit) {

    var beaconManager: BeaconManager = BeaconManager(context)
    var uuid: UUID? = UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D")
    var beaconsListForServer = mutableListOf<Beacon>()
    var monitoredBeacons = mutableListOf<Beacon>()
    private val rangeDistance = 2
    val region = Region(
        "monitored region",
        uuid,
        null, null
    )

    fun startMonitoring() {
        beaconManager.connect {
            beaconManager.setBackgroundScanPeriod(1000, 2000)
            beaconManager.setForegroundScanPeriod(1000, 2000)
            beaconManager.startMonitoring(region)
            beaconManager.startRanging(region)
        }

        beaconManager.setMonitoringListener(object : BeaconManager.MonitoringListener {
            override
            fun onEnteredRegion(region: Region, beacons: List<Beacon>) {
                monitoredBeacons.addAll(beacons)
            }

            override fun onExitedRegion(region: Region) {
                if (monitoredBeacons.isNotEmpty()) {
                    for (i in 0 until monitoredBeacons.size) {
                        sendBeacon(buildBeaconData(monitoredBeacons[i], false, -1.0))
                        Log.e(
                            "calculateBeacon",
                            "onExitedRegion ${monitoredBeacons[i].major}:${monitoredBeacons[i].minor}"
                        )
                    }
                    monitoredBeacons.clear()
                }
            }
        })

        beaconManager.setRangingListener(BeaconManager.RangingListener() { region, list ->
            if (list.isNotEmpty()) {
                list.forEach {
                    Log.e(
                        "Detected beacon",
                        "$it and proximity = ${Utils.computeAccuracy(it)}"
                    )
                }
                calculateBeacon(list)
            }
        })
    }

    private fun calculateBeacon(beacons: MutableList<Beacon>) {
        if (beacons.isNotEmpty()) {
            for (i in 0 until beacons.size) {
                if (beaconsListForServer.isEmpty()) {
                    val proximity = Utils.computeAccuracy(beacons[i])
                    if (proximity < rangeDistance) {
                        beaconsListForServer.add(beacons[i])
                        sendBeacon(buildBeaconData(beacons[i], true, proximity))
                        Log.e("calculateBeacon", "1 beacon ${beacons[i].major}:${beacons[i].minor}")
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
                                Log.e(
                                    "calculateBeacon",
                                    "2 beacon ${beacons[i].major}:${beacons[i].minor}"
                                )
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
                                Log.e(
                                    "calculateBeacon",
                                    "3 beacon ${beacons[i].major}:${beacons[i].minor}"
                                )
                            }
                        }
                    } else {
                        if (proximity < rangeDistance) {
                            beaconsListForServer.add(beacons[i])
                            sendBeacon(buildBeaconData(beacons[i], true, proximity))
                            Log.e(
                                "calculateBeacon",
                                "4 beacon ${beacons[i].major}:${beacons[i].minor}"
                            )
                        }
                    }
                }
            }
        } else {
            if (beaconsListForServer.isEmpty()) {
                //nothing
            } else {
                for (i in 0 until beaconsListForServer.size) {
                    sendBeacon(buildBeaconData(beaconsListForServer[i], false, -1.0))
                    beaconsListForServer.remove(beaconsListForServer[i])
                    Log.e("calculateBeacon", "5 beacon ${beacons[i].major}:${beacons[i].minor}")
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

    fun stopMonitoring() {
        beaconManager.stopMonitoring(region)
        beaconManager.stopRanging(region)
    }
}