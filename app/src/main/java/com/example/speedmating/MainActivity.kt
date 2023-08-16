package com.example.speedmating

import android.app.ActivityOptions
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.speedmating.ui.theme.SpeedMatingTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)
        var title = findViewById<TextView>(R.id.title)
        val spannableString = SpannableString("SpeedMating")
        spannableString.setSpan(ForegroundColorSpan(Color.parseColor("#FF565E")), 5, 6, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
        title.text = spannableString
        val bt_start = findViewById<Button>(R.id.bt_start)
        bt_start.setOnClickListener {
            startActivity(
                Intent(this@MainActivity, QuestionsActivity::class.java),
                ActivityOptions.makeSceneTransitionAnimation(this).toBundle())
        }

    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
            text = "Hello $name!",
            modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SpeedMatingTheme {
        Greeting("Android")
    }
}