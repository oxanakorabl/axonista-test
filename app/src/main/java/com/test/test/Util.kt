package com.test.test

import android.content.Context
import java.io.IOException

object Util {

    fun getJsonDataFromAsset(context: Context, fileName: String): String? {
        val jsonString: String
        try {
            jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            return null
        }
        return jsonString
    }


}

fun String.prettyJson(): String? {
    val sb = StringBuilder()
    var indent = 0
    var pre = 0.toChar()
    for (c in this.toCharArray()) {
        if (Character.isWhitespace(c)) continue
        if (c == ']' || c == '}') indent--
        if (pre == '[' || pre == '{' || pre == ',' || c == ']' || c == '}') {
            sb.append('\n')
            for (i in 0 until indent) sb.append("  ")
        }
        sb.append(c)
        if (c == '[' || c == '{') indent++
        pre = c
    }
    return sb.toString()
}