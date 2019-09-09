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
                    beacons.forEach { sendBeacon(buildBeaconData(it)) }
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
//                Toast.makeText(
//                    context,
//                    "Detected beacon with major ${list[0].major}",
//                    Toast.LENGTH_SHORT
//                ).show()
                list.forEach {
                    sendBeacon(buildBeaconData(it))
                }
            }
        })
    }

    private fun buildBeaconData(beacon: Beacon): BeaconData {
        return BeaconData(
            beacon.proximityUUID.toString(),
            beacon.major.toString(),
            beacon.minor.toString(),
            beacon.rssi.toString(),
            Utils.computeAccuracy(beacon).toString()
        )
    }
}