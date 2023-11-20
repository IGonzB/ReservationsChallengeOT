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

package com.opentable.challenge.presentation.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.opentable.challenge.GlobalConfig
import com.opentable.challenge.data.local.ReservationDao
import com.opentable.challenge.presentation.viewmodel.AddReservationViewModel
import com.opentable.challenge.presentation.viewmodel.MainActivityViewModel
import com.opentable.challenge.presentation.viewmodel.ReservationsViewModel

/**
 * Factory for ViewModels
 */
class ViewModelFactory(private val dataSource: ReservationDao) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddReservationViewModel::class.java)) {
            return AddReservationViewModel(
                dataSource,
                GlobalConfig(
                    GlobalConfig.startHour,
                    GlobalConfig.endHour,
                    GlobalConfig.interval
                )
            ) as T
        }
        if (modelClass.isAssignableFrom(ReservationsViewModel::class.java)) {
            return ReservationsViewModel(dataSource) as T
        }
        if (modelClass.isAssignableFrom(MainActivityViewModel::class.java)) {
            return MainActivityViewModel(dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
