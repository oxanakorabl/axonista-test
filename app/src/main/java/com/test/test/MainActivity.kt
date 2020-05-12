package com.test.test

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.annotation.UiThread
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {
    private lateinit var text: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        text = findViewById(R.id.text)

        val job: Job = GlobalScope.launch(Dispatchers.IO) {
            main()
        }
    }

    private suspend fun main() = coroutineScope {
        launch {
            val jsonData = doWork()
            runOnUiThread {
                showResult(jsonData)
            }
        }
    }

    private fun showResult(jsonData: String?) {
        if (!jsonData.isNullOrEmpty()) {
            text.text = jsonData.prettyJson()
        } else {
            text.text = getString(R.string.error)
        }
    }

    private fun doWork(): String? {
        val attendees = getAttendees()

        val countries = collectCountries(attendees)
        val result = mutableListOf<Conference>()

        countries.map { country ->
            val mapDateAndEmails = HashMap<String, MutableSet<String>>()
            attendees
                    .filter { it.country == country }
                    .map { collectDates(it, mapDateAndEmails) }

            val maxKey = findMax(mapDateAndEmails)

            if (maxKey.isNotEmpty()) {
                result.add(Conference(country, maxKey, mapDateAndEmails[maxKey]?.toList()))
            }
        }

        val gson = Gson()
        val jsonData = gson.toJson(result)
        Log.e("Result", jsonData)

        return jsonData
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


    private fun getAttendees(): List<Attendee> {
        val json: String? = Util.getJsonDataFromAsset(this, "attendees.json")
        val gson = Gson()
        val listPersonType = object : TypeToken<List<Attendee>>() {}.type
        return gson.fromJson(json, listPersonType)
    }


}
