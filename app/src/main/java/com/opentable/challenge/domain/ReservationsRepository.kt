package com.opentable.challenge.domain

import com.opentable.challenge.data.model.Reservation
import io.reactivex.Completable
import io.reactivex.Flowable

interface ReservationsRepository {
    fun getReservationById (id: String) : Flowable<Reservation>
    fun getReservations() : Flowable<List<Reservation>>
    fun deleteReservations() : Completable
    fun insertReservation(reservation: Reservation) : Completable
}