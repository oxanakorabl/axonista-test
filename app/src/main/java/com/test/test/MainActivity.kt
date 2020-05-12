package com.test.test

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(), ResultHandler {
    private lateinit var text: TextView
    private val presenter: MainPresenter = MainPresenter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        text = findViewById(R.id.text)
        presenter.setJson(Util.getJsonDataFromAsset(this, "attendees.json"))
        presenter.start()
    }

    override fun showResult(jsonData: String?) {
        runOnUiThread {
            if (!jsonData.isNullOrEmpty()) {
                text.text = jsonData.prettyJson()
            } else {
                text.text = getString(R.string.error)
            }
        }
    }
}

interface ResultHandler {
    fun showResult(jsonData: String?)
}
