/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.opentable.challenge

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.opentable.challenge.data.local.ReservationsDatabase
import com.opentable.challenge.data.model.Reservation
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Test the implementation of [ReservationsDao]
 */
@RunWith(AndroidJUnit4::class)
class ReservationsDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: ReservationsDatabase

    @Before
    fun initDb() {
        // using an in-memory database because the information stored here disappears after test
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            ReservationsDatabase::class.java
        )
            // allowing main thread queries, just for testing
            .allowMainThreadQueries()
            .build()
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun getUsersWhenNoUserInserted() {
        database.reservationDAO().getReservationById("15.45")
            .test()
            .assertNoValues()
    }

    @Test
    fun insertAndGetUser() {
        // When inserting a new user in the data source
        database.reservationDAO().insertReservation(RESERVATION).blockingAwait()

        // When subscribing to the emissions of the user
        database.reservationDAO().getReservationById(RESERVATION.reservationId)
            .test()
            // assertValue asserts that there was only one emission of the user
            .assertValue { it.reservationId == RESERVATION.reservationId && it.reservationName == RESERVATION.reservationName }
    }

    @Test
    fun updateAndGetUser() {
        // Given that we have a user in the data source
        database.reservationDAO().insertReservation(RESERVATION).blockingAwait()

        // When we are updating the name of the user
        val updatedUser = Reservation(RESERVATION.reservationId, "Irvin G New", RESERVATION.reservationTime)
        database.reservationDAO().insertReservation(updatedUser).blockingAwait()

        // When subscribing to the emissions of the user
        database.reservationDAO().getReservationById(RESERVATION.reservationId)
            .test()
            // assertValue asserts that there was only one emission of the user
            .assertValue { it.reservationId == RESERVATION.reservationId && it.reservationName == "Irvin G New" }
    }

    @Test
    fun deleteAndGetUser() {
        // Given that we have a user in the data source
        database.reservationDAO().insertReservation(RESERVATION).blockingAwait()

        //When we are deleting all users
        database.reservationDAO().deleteAllReservations()
        // When subscribing to the emissions of the user
        database.reservationDAO().getReservationById(RESERVATION.reservationId)
            .test()
            // check that there's no user emitted
            .assertNoValues()
    }

    companion object {
        private val RESERVATION = Reservation("15.45", "Irvin Gonzalez B.", "15:45")
    }
}
