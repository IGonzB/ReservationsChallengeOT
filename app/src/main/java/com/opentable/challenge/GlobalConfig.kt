package com.opentable.challenge

/**
 * Class that holds the Global Values for Reservation
 */
class GlobalConfig(
    val startHour : Int,
    val endHour : Int,
    val interval : Int
) {
    companion object {
        const val startHour = 17 // Accepting reservations from
        const val endHour = 23   // Accepting reservations Until
        const val interval = 4   // 60/4 = Every 15 Min - 60/10 = Every 6 Min and so on
        const val range = 60 / interval
    }
}