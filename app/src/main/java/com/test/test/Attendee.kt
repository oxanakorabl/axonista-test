package com.test.test

data class Attendee(
    val firstName: String,
    val lastName: String,
    val email: String,
    val country: String,
    val availableDates: List<String>
)