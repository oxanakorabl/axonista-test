package com.test.test

import java.time.LocalDate

fun collectCountries(attendees: List<Attendee>): HashSet<String> {
    val countries = HashSet<String>()
    attendees.map {
        countries.add(it.country)
    }
    return countries
}

fun validateAttendeeDate(attendee: Attendee, mapDateEmails: HashMap<String, MutableSet<String>>) {
    val daysList = prepareAttendeeDates(attendee)

    if (daysList.size > 1) {

        for (i: Int in daysList.size - 1 downTo 1) {
            val dayBefore = LocalDate.parse(daysList[i]).minusDays(1)
            if (LocalDate.parse(daysList[i - 1]) == dayBefore) {
                addKeyAndEmail(mapDateEmails, daysList[i - 1], attendee.email)
            }
        }
    }
}

fun prepareAttendeeDates(attendee: Attendee): List<String> {
    val datesSet = HashSet<String>()
    datesSet.addAll(attendee.availableDates)

    return datesSet
        .toSortedSet()
        .toList()
}

fun addKeyAndEmail(
    mapDateEmails: HashMap<String, MutableSet<String>>,
    date: String,
    email: String
) {
    if (mapDateEmails[date] != null) {
        mapDateEmails[date]?.add(email)
    } else {
        val emails = mutableSetOf<String>()
        emails.add(email)
        mapDateEmails[date] = emails
    }
}

fun findMax(map: HashMap<String, MutableSet<String>>): String {
    var maxSize = 0
    var maxKey = ""
    map
        .toSortedMap()
        .map {
            if (maxSize < it.value.size) {
                maxSize = it.value.size
                maxKey = it.key
            }
        }
    return maxKey
}
