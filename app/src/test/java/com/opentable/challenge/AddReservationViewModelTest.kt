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
import com.opentable.challenge.data.local.ReservationDao
import com.opentable.challenge.data.model.Reservation
import com.opentable.challenge.presentation.viewmodel.AddReservationViewModel
import io.reactivex.Completable
import io.reactivex.Flowable
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations


/**
 * Unit test for [AddReservationViewModel]
 */
class AddReservationViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var dataSource: ReservationDao

    @Captor
    private lateinit var userArgumentCaptor: ArgumentCaptor<Reservation>

    private lateinit var viewModel: AddReservationViewModel

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)

        val configuration = GlobalConfig(START_TIME, END_TIME, INTERVAL)

        viewModel = AddReservationViewModel(dataSource, configuration)
    }

    @Test
    fun getCalculatedTimeSlots() {
        val calculatedTimeSlots = viewModel.calculateTimeSlots()

        Assert.assertTrue(calculatedTimeSlots.size == 28)
        Assert.assertTrue(calculatedTimeSlots.get(0).id == "17.0")
        Assert.assertTrue(calculatedTimeSlots.get(4).id == "18.0")
        Assert.assertTrue(calculatedTimeSlots.get(8).id == "19.0")
        Assert.assertTrue(calculatedTimeSlots.get(12).id == "20.0")
        Assert.assertTrue(calculatedTimeSlots.get(16).id == "21.0")
        Assert.assertTrue(calculatedTimeSlots.get(20).id == "22.0")
        Assert.assertTrue(calculatedTimeSlots.get(24).id == "23.0")
    }


    companion object {
        private val RESERVATION = Reservation("15.45", "Irvin Gonzalez B.", "15:45")
        const val START_TIME = 17
        const val END_TIME = 23
        const val INTERVAL = 4
    }
}
