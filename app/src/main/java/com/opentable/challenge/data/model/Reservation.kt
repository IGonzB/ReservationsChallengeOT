package com.opentable.challenge.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reservation_table")
data class Reservation(
    @ColumnInfo(name = "reservation_id")
    @PrimaryKey(autoGenerate = false)
    val reservationId: String,
    @ColumnInfo(name = "reservation_name")
    val reservationName: String,
    @ColumnInfo(name = "reservation_time")
    val reservationTime: String
)
