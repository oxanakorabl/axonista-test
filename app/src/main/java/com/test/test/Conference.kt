package com.test.test

import com.google.gson.annotations.SerializedName

data class Conference(
    @SerializedName("country")
    val country: String,
    @SerializedName("startingDate")
    val startingDate: String,
    @SerializedName("emails")
    val emails: List<String>?
)