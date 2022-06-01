package com.hva.bewear.data.generic

import java.time.*

private const val ZONE_PREFIX = "UTC"

fun Instant.toDate(): LocalDate {
    return atZone(ZoneId.systemDefault()).toLocalDate()
}

fun Instant.toDateTime(timezoneOffset: Int = 0): LocalDateTime {
    return atZone(ZoneId.ofOffset(ZONE_PREFIX, ZoneOffset.ofTotalSeconds(timezoneOffset)))
        .toLocalDateTime()
}

fun Instant.isBeforeCurrentDate(): Boolean {
    val date = LocalDate.ofEpochDay(epochSecond)
    return date.isBefore(LocalDate.now())
}

fun Instant.isBeforeCurrentHour(offset: ZoneOffset): Boolean {
    val dateTime = LocalDateTime.ofInstant(this, offset)
    val now = LocalDateTime.now(ZoneId.ofOffset(ZONE_PREFIX, offset))
    return isBeforeCurrentDate() || dateTime.hour < now.hour
}