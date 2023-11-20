package com.opentable.challenge.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

@Entity(tableName = "timeslot_table")
data class TimeSlot(
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = false)
    val id: String,
    @ColumnInfo(name = "hour")
    val hour: Int,
    @ColumnInfo(name = "minute")
    val minute: Int,
) {
    private val calendar: Calendar = Calendar.getInstance()

    companion object {
        const val yyyy_MM_dd_HH_mm = "yyyy-MM-dd HH:mm a"
        const val HH_mm = "HH:mm a"
    }

    init {
        calendar.time = Date()
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
    }

    fun formattedDateTime(): String {
        val formatter = SimpleDateFormat(yyyy_MM_dd_HH_mm)
        val current = formatter.format(calendar.time)
        return current
    }

    fun formattedTime(): String {
        val formatter = SimpleDateFormat(HH_mm)
        val current = formatter.format(calendar.time)
        return current
    }
}
