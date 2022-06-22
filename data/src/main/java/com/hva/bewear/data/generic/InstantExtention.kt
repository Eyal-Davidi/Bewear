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

fun Instant.isBeforeCurrentDate(offset: ZoneOffset): Boolean {
    val date = atOffset(offset).toLocalDate()
    return date.isBefore(LocalDate.now())
}

fun Instant.isBeforeCurrentHour(offset: ZoneOffset): Boolean {
    val dateTime = atOffset(offset).toLocalDateTime()
    return isBeforeCurrentDate(offset) || dateTime.hour < LocalDateTime.now().hour
}