package com.test.test

import android.util.Log

fun collectCountries(attendees: List<Attendee>): HashSet<String> {
    val countries = HashSet<String>()
    attendees.map {
        countries.add(it.country)
    }
        Log.i("collectCountries", countries.size.toString())
        countries.map {
            Log.i("collectCountries", it)
        }
    return countries
}


fun findBestDate(country: String, attendees: List<Attendee> ): Conference? {
    val mapDateAndEmails = HashMap<String, MutableSet<String>>()
    attendees
        .filter { it.country == country }
        .map { collectDates(it, mapDateAndEmails) }

    val maxKey = findMax(mapDateAndEmails)

    return Conference(country, maxKey, mapDateAndEmails[maxKey]?.toList())
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

     Log.i("findMax", "maxKey $maxKey maxSize $maxSize")

    return maxKey
}

fun collectDates(attendee: Attendee, map: HashMap<String, MutableSet<String>>) {
    attendee.availableDates.map {
        map[it]?.add(attendee.email)
        if (map[it] != null) {
            map[it]?.add(attendee.email)
        } else {
            val set = mutableSetOf<String>()
            set.add(attendee.email)
            map.put(it, set)
        }
    }
        map.map { entry ->
            entry.value.map {
                Log.i("collectDates", " ${attendee.country} ${entry.key} $it")
            }
        }
}