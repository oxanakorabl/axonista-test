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
            val mapDateEmails = HashMap<String, MutableSet<String>>()

            attendees
                .filter { it.country == country }
                .map { attendee ->
                    validateAttendeeDate(attendee, mapDateEmails)
                }

            val maxKey = findMax(mapDateEmails)
            result.add(Conference(country, maxKey, mapDateEmails[maxKey]?.toList()))
        }

        val jsonData = Gson().toJson(result)
        Log.e("Result", jsonData)

        return jsonData
    }


}




