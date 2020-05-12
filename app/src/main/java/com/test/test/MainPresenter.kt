package com.test.test

import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class MainPresenter(private val resultHandler: ResultHandler) {
    private lateinit var attendees: List<Attendee>

    fun setJson(json: String?) {
        val gson = Gson()
        val listPersonType = object : TypeToken<List<Attendee>>() {}.type
        attendees = gson.fromJson(json, listPersonType)
    }

    fun start() {
        GlobalScope.launch(Dispatchers.IO) {
            doWork()
        }
    }

    private suspend fun doWork() = coroutineScope {
        launch {
            val jsonData = findBestDates()
            resultHandler.showResult(jsonData)
        }
    }

    private fun findBestDates(): String? {
        val countries = collectCountries(attendees)
        val result = mutableListOf<Conference>()

        countries.map { country ->

            val conference = findBestDate(country)
            if (conference != null) {
                result.add(conference)
            }
        }

        val jsonData = Gson().toJson(result)
        Log.e("Result", jsonData)

        return jsonData
    }

    private fun findBestDate(country: String): Conference? {
        val mapDateAndEmails = HashMap<String, MutableSet<String>>()
        attendees
            .filter { it.country == country }
            .map { collectDates(it, mapDateAndEmails) }

        val maxKey = findMax(mapDateAndEmails)

        return Conference(country, maxKey, mapDateAndEmails[maxKey]?.toList())
    }

    private fun findMax(map: HashMap<String, MutableSet<String>>): String {
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

        // Log.i("findMax", "maxKey $maxKey maxSize $maxSize")

        return maxKey
    }

    private fun collectDates(attendee: Attendee, map: HashMap<String, MutableSet<String>>) {
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
//        map.map { entry ->
//            entry.value.map {
//                Log.i("collectDates", "${entry.key} $it")
//            }
//        }
    }

    private fun collectCountries(attendees: List<Attendee>): HashSet<String> {
        val countries = HashSet<String>()
        attendees.map {
            countries.add(it.country)
        }
//        Log.i("collectCountries", countries.size.toString())
//        countries.map {
//            Log.i("collectCountries", it)
//        }
        return countries
    }

}