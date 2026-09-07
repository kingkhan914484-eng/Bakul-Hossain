package com.example.util

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.Uri
import androidx.core.content.ContextCompat

data class DeviceLocation(
    val latitude: Double,
    val longitude: Double,
    val accuracyMeters: Float = 0f,
    val provider: String = "GPS",
    val timestamp: Long = System.currentTimeMillis()
)

object LocationHelper {

    fun hasLocationPermission(context: Context): Boolean {
        val fine = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        val coarse = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        return fine || coarse
    }

    @SuppressLint("MissingPermission")
    fun getCurrentLocation(context: Context): DeviceLocation? {
        if (!hasLocationPermission(context)) return null
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as? LocationManager
            ?: return null

        val gpsLoc = try {
            locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        } catch (e: Exception) {
            null
        }

        val netLoc = try {
            locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
        } catch (e: Exception) {
            null
        }

        val bestLocation: Location? = when {
            gpsLoc != null && netLoc != null -> {
                if (gpsLoc.time > netLoc.time) gpsLoc else netLoc
            }
            gpsLoc != null -> gpsLoc
            else -> netLoc
        }

        return bestLocation?.let {
            DeviceLocation(
                latitude = it.latitude,
                longitude = it.longitude,
                accuracyMeters = it.accuracy,
                provider = it.provider ?: "Device"
            )
        }
    }

    /**
     * Calculates distance between two points in kilometers using Haversine formula
     */
    fun calculateDistanceKm(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val r = 6371.0 // Earth radius in kilometers
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                Math.sin(dLon / 2) * Math.sin(dLon / 2)
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
        return r * c
    }

    /**
     * Opens map navigation safely without crashing.
     * Uses geo: URI scheme, falling back to Google Maps web URL if no map app is installed.
     */
    fun openNavigation(
        context: Context,
        latitude: Double?,
        longitude: Double?,
        label: String,
        address: String
    ) {
        try {
            if (latitude != null && longitude != null) {
                val geoUri = Uri.parse("geo:$latitude,$longitude?q=$latitude,$longitude(${Uri.encode(label)})")
                val mapIntent = Intent(Intent.ACTION_VIEW, geoUri)
                mapIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                if (mapIntent.resolveActivity(context.packageManager) != null) {
                    context.startActivity(mapIntent)
                    return
                }
                val webUri = Uri.parse("https://www.google.com/maps/search/?api=1&query=$latitude,$longitude")
                val webIntent = Intent(Intent.ACTION_VIEW, webUri).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                context.startActivity(webIntent)
            } else if (address.isNotBlank()) {
                val geoUri = Uri.parse("geo:0,0?q=${Uri.encode("$label, $address")}")
                val mapIntent = Intent(Intent.ACTION_VIEW, geoUri).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                if (mapIntent.resolveActivity(context.packageManager) != null) {
                    context.startActivity(mapIntent)
                    return
                }
                val webUri = Uri.parse("https://www.google.com/maps/search/?api=1&query=${Uri.encode("$label, $address")}")
                val webIntent = Intent(Intent.ACTION_VIEW, webUri).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                context.startActivity(webIntent)
            }
        } catch (e: Exception) {
            // Intent handling error prevention
        }
    }
}
