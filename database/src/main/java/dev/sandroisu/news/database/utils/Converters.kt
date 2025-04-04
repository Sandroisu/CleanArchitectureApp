package dev.sandroisu.news.database.utils

import androidx.room.TypeConverter
import java.text.DateFormat
import java.text.ParseException
import java.util.Date

internal class Converters {
    @TypeConverter
    fun fromTimestamp(value: String?): Date? {
        return try {
            value?.let {
                DateFormat.getDateTimeInstance().parse(it)
            }
        } catch (throwable: ParseException) {
            null
        }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): String? {
        return date?.time?.let {
            DateFormat.getDateInstance().format(it)
        }
    }
}
