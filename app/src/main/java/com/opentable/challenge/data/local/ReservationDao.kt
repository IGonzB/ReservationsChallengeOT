package com.opentable.challenge.data.local

import androidx.room.*
import com.opentable.challenge.data.model.Reservation
import io.reactivex.Completable
import io.reactivex.Flowable

@Dao
interface ReservationDao {

    @Query("SELECT * FROM reservation_table")
    fun getReservations(): Flowable<List<Reservation>>

    @Query("SELECT * FROM reservation_table WHERE reservation_id = :id")
    fun getReservationById(id: String): Flowable<Reservation>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertReservation(reservation: Reservation): Completable

    @Delete
    fun delete(reservation: Reservation): Completable

    @Query("DELETE FROM reservation_table")
    fun deleteAllReservations(): Completable
}