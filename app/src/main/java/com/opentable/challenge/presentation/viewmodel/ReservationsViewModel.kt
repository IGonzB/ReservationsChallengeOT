package com.opentable.challenge.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.opentable.challenge.data.local.ReservationDao
import com.opentable.challenge.data.model.Reservation
import io.reactivex.Flowable
import io.reactivex.Single

class ReservationsViewModel(private val dataSource: ReservationDao) : ViewModel() {

    fun getReservations(): Flowable<List<Reservation>> {
        return dataSource.getReservations()
    }
}

