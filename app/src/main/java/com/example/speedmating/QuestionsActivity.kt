package com.example.speedmating

import android.os.Bundle
import android.os.PersistableBundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.graphics.Color

class QuestionsActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.activity_questions)
//        var title = findViewById<TextView>(R.id.title)
//        val spannableString = SpannableString("SpeedMating")
//        spannableString.setSpan(ForegroundColorSpan(Color.parseColor("#FF565E")), 5, 6, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
//        title.text = spannableString

    }

}