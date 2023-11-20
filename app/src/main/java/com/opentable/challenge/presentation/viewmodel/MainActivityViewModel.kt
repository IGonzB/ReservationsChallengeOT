package com.opentable.challenge.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.opentable.challenge.data.local.ReservationDao
import com.opentable.challenge.data.model.Reservation
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

class MainActivityViewModel(private val dataSource: ReservationDao) : ViewModel() {

    fun deleteReservations(): Completable {
        return dataSource.deleteAllReservations()
    }
}

