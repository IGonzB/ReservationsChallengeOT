/*
 * Copyright (C) 2020 The Android Open Source Project
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
package com.opentable.challenge.presentation.fragment

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.opentablechallenge.presentation.MainActivity
import com.opentable.challenge.R
import com.opentable.challenge.data.di.Injection
import com.opentable.challenge.databinding.FragmentAddReservationBinding
import com.opentable.challenge.presentation.util.ViewModelFactory
import com.opentable.challenge.presentation.viewmodel.AddReservationViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers


class AddReservationFragment : Fragment(R.layout.fragment_add_reservation),
    AdapterView.OnItemSelectedListener {

    private lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: AddReservationViewModel by viewModels { viewModelFactory }

    private val disposable = CompositeDisposable()

    private var availableTimes: List<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModelFactory = Injection.provideViewModelFactory(requireContext())
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentAddReservationBinding.bind(view)

        disposable.add(
            viewModel.getAvailableTimeSlots()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map {
                    it.map {
                        it.formattedTime()
                    }
                }
                .subscribe({

                    val arrayAdapter =
                        ArrayAdapter(
                            requireContext(),
                            android.R.layout.simple_spinner_item,
                            it
                        )
                    arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                    binding.apply {
                        addReservationSpinner.apply {
                            adapter = arrayAdapter
                            setSelection(0, false)
                            onItemSelectedListener = this@AddReservationFragment
                            prompt = getString(R.string.add_reservation_spinner_prompt)
                            gravity = Gravity.CENTER
                        }
                    }
                    availableTimes = it
                }, { error -> Log.e(TAG, "Error on fetch TimeSlots", error) })
        )

        binding.apply {
            addReservationSaveButton.setOnClickListener {

                if (TextUtils.isEmpty(addReservationEt.text)) {
                    addReservationTil.error = getString(R.string.add_reservation_name_error)
                    return@setOnClickListener
                } else {
                    addReservationTil.error = null
                }

                if (TextUtils.isEmpty(availableTimes.getOrNull(addReservationSpinner.selectedItemPosition))) {
                    showToast(message = getString(R.string.add_reservation_nothing_selected))
                    return@setOnClickListener
                }

                disposable.add(
                    viewModel.saveReservation(
                        addReservationEt.text.toString(),
                        addReservationSpinner.selectedItemPosition
                    ).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe({
                            Log.d(
                                TAG,
                                "saveReservation#Success()"
                            ).toString()
                            findNavController().popBackStack()
                        },
                            { error -> Log.e(TAG, "Unable to update username", error) })
                )
            }
        }
    }

    override fun onStop() {
        super.onStop()
        disposable.clear()
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        // TODO do something with selection
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        showToast(message = getString(R.string.add_reservation_nothing_selected))
    }

    private fun showToast(
        context: Context = requireContext(),
        message: String,
        duration: Int = Toast.LENGTH_LONG
    ) {
        Toast.makeText(context, message, duration).show()
    }

    companion object {
        private val TAG = AddReservationFragment::class.java.simpleName
    }
}
