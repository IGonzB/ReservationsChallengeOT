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

package com.opentable.challenge.data.local

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import com.opentable.challenge.data.model.Reservation

/**
 * The Room database that contains the Users table
 */
@Database(entities = [Reservation::class], version = 1)
abstract class ReservationsDatabase : RoomDatabase() {

    abstract fun reservationDAO(): ReservationDao

    companion object {

        @Volatile private var INSTANCE: ReservationsDatabase? = null

        fun getInstance(context: Context): ReservationsDatabase =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
                }

        private fun buildDatabase(context: Context) =
                Room.databaseBuilder(context.applicationContext,
                        ReservationsDatabase::class.java, "Reservations.db")
                        .build()
    }
}
