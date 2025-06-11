package com.optiroute.com.domain.models

import com.optiroute.com.utils.LocationUtils

fun Location.distanceTo(other: Location): Double {
    return LocationUtils.calculateDistance(
        this.latitude, this.longitude,
        other.latitude, other.longitude
    )
}

fun Location.bearingTo(other: Location): Double {
    return LocationUtils.calculateBearing(
        this.latitude, this.longitude,
        other.latitude, other.longitude
    )
}

fun Location.isValid(): Boolean {
    return LocationUtils.isLocationValid(this)
}