package com.test.test


import org.hamcrest.CoreMatchers
import org.junit.Assert.*
import org.junit.Test


class BestDateUtilKtTest {
    val attendees: List<Attendee> = mutableListOf(
        Attendee(
            "aaa",
            "bbb",
            "aaa1@bbb.ru",
            "senegal",
            mutableListOf("2010-02-01", "2010-02-02", "2010-02-03")
        ),
        Attendee(
            "aaa",
            "bbb",
            "aaa2@bbb.ru",
            "senegal",
            mutableListOf("2010-02-04", "2010-02-02", "2010-02-03")
        ),
        Attendee(
            "aaa",
            "bbb",
            "aaa3@bbb.ru",
            "senegal",
            mutableListOf("2010-02-05", "2010-02-04", "2010-02-03")
        ),
        Attendee(
            "aaa",
            "bbb",
            "aaa4@bbb.ru",
            "zimbabwe",
            mutableListOf("2010-02-01", "2010-02-02", "2010-02-03")
        ),
        Attendee(
            "aaa",
            "bbb",
            "aaa5@bbb.ru",
            "zimbabwe",
            mutableListOf("2010-02-04", "2010-02-02", "2010-02-03")
        )
    )


    @Test
    fun collectCountries() {
        val expected = listOf<String>("zimbabwe", "senegal")
        val actual = collectCountries(attendees)
        assertArrayEquals(actual.toArray(), expected.toTypedArray())
    }

    @Test
    fun findBestDate() {
        val expected = Conference("zimbabwe", "2010-02-02", listOf("aaa4@bbb.ru","aaa5@bbb.ru"))
        val actual = findBestDate("zimbabwe", attendees)
        assertEquals( expected, actual)
    }

    @Test
    fun findMax() {
        val map = HashMap<String, MutableSet<String>>()
        var set = mutableSetOf("aaa4@bbb.ru")
        map["2010-02-01"] = set

        set = mutableSetOf("aaa4@bbb.ru", "aaa5@bbb.ru")
        map["2010-02-02"] = set
        map["2010-02-03"] = set

        set = mutableSetOf("aaa5@bbb.ru")
        map["2010-02-04"] = set

        val expected = "2010-02-02"
        val actual = findMax(map)

        assertEquals( expected, actual)
    }

    @Test
    fun collectDates() {
    }
}