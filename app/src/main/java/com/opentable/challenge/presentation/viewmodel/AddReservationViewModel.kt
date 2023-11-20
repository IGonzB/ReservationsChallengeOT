package com.opentable.challenge.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.opentable.challenge.GlobalConfig
import com.opentable.challenge.data.local.ReservationDao
import com.opentable.challenge.data.model.Reservation
import com.opentable.challenge.data.model.TimeSlot
import io.reactivex.Completable
import io.reactivex.Flowable

import io.reactivex.Single

class AddReservationViewModel(
    private val dataSource: ReservationDao,
    private val configuration: GlobalConfig
) : ViewModel() {

    /**
     * Function to Calculate the Hours and its intervals and create a TimeSlot Object
     */
    fun calculateTimeSlots(): List<TimeSlot> {
        var start = configuration.startHour
        var hoursAvailable = 0
        for (i in configuration.startHour..configuration.endHour) {
            hoursAvailable++
        }

        val totalSlots = hoursAvailable * configuration.interval
        val slots = arrayOfNulls<TimeSlot>(totalSlots)

        for (i in 0 until totalSlots step configuration.interval) {
            slots[i] = TimeSlot(start.toString(), start, 0)

            var ix = i + 1
            var r: Double = GlobalConfig.range.toDouble()
            for (j in 0 until configuration.interval - 1) {
                val iv = start.toDouble() + (r / 100)
                slots[ix] = TimeSlot(iv.toString(), start, r.toInt())
                r = addRange(r)
                ix++
            }
            start++
        }
        return slots.requireNoNulls().toList()
    }

    private fun addRange(addedRange: Double): Double {
        return addedRange + GlobalConfig.range
    }

    /**
     * Function to Get the Available Slots by calculating the Intervals and removing the already selected ones
     */
    fun getAvailableTimeSlots(): Single<List<TimeSlot>> {
        val mutableSlots = mutableListOf<TimeSlot>()
        mutableSlots.addAll(calculateTimeSlots())

        return dataSource.getReservations().firstOrError().map {

            it.forEach { persistedReservation ->
                val indexToRemove =
                    mutableSlots.indexOf(mutableSlots.find { it.id == persistedReservation.reservationId })
                // Remove current and intervals ahead as they should not be selectable
                for (i in 1..configuration.interval) {
                    if (mutableSlots.getOrNull(indexToRemove) != null) {
                        mutableSlots.removeAt(indexToRemove)
                    }
                }
                var indexToRemoveBefore = indexToRemove
                indexToRemoveBefore--
                // Remove intervals before as they should not be selectable because they collide with current reservation time
                for (i in 1..<configuration.interval) {
                    if (mutableSlots.getOrNull(indexToRemoveBefore) != null) {
                        mutableSlots.removeAt(indexToRemoveBefore)
                        indexToRemoveBefore--
                    }
                }
            }
            mutableSlots
        }
    }

    fun saveReservation(name: String, selectedIndex: Int): Completable {
        return getAvailableTimeSlots().flatMapCompletable { timeSlots ->
            val timeSlot = timeSlots.get(selectedIndex)
            val reservation = Reservation(timeSlot.id, name, timeSlot.formattedTime())

            Log.d(
                "AddReservationViewModel",
                "saveReservation($reservation)"
            ).toString()

            dataSource.insertReservation(reservation)
        }
    }

    fun getReservationById(id: String): Flowable<Reservation> {
        return dataSource.getReservationById(id)
    }
}