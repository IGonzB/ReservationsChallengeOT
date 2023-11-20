package com.opentable.challenge.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.opentable.challenge.data.model.Reservation
import com.opentable.challenge.databinding.ReservationItemViewBinding

class ReservationsAdapter(private val listener: OnItemClickListener) :
    ListAdapter<Reservation, ReservationsAdapter.ReservationViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReservationViewHolder {
        val binding =
            ReservationItemViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReservationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReservationViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    inner class ReservationViewHolder(private val binding: ReservationItemViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.apply {
                root.setOnClickListener {
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION) {
                        val article = getItem(position)
                        listener.onItemClick(article)
                    }
                }
            }
        }

        fun bind(reservation: Reservation) {
            binding.apply {
                reservationsItemName.text = reservation.reservationName
                reservationsItemTime.text = reservation.reservationTime
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(reservation: Reservation)
    }


    class DiffCallback : DiffUtil.ItemCallback<Reservation>() {
        override fun areItemsTheSame(oldItem: Reservation, newItem: Reservation): Boolean {
            return oldItem.reservationName == newItem.reservationName && oldItem.reservationTime == newItem.reservationTime
        }

        override fun areContentsTheSame(oldItem: Reservation, newItem: Reservation): Boolean {
            return oldItem == newItem
        }
    }
}