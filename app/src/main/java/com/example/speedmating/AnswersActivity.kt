package com.example.speedmating

import android.graphics.Color
import android.os.Bundle
import android.os.PersistableBundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.widget.TextView
import androidx.activity.ComponentActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AnswersActivity: ComponentActivity() {
    val databaseReference: DatabaseReference = FirebaseDatabase.getInstance()
        .getReferenceFromUrl("https://speedmating-85fb4-default-rtdb.firebaseio.com/")
    private lateinit var timerTextView: TextView


    private var mainConnectionId = ""
    private var mainPlayerId = ""
    private var mainMateId = ""

//    private var answers = mutableListOf<String>()
//    private var questions = mutableListOf<String>()

//    private val answers: MutableList<String> = mutableListOf()
//
//    private val questions: MutableList<String> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        val conId = intent.getStringExtra("conId").toString()
        val curPlayerId = intent.getStringExtra("curPlayerId").toString()
        val matePlayerId = intent.getStringExtra("matePlayerId").toString()


        mainConnectionId = conId
        mainPlayerId = curPlayerId
        mainMateId = matePlayerId
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_answers)

        var qu1 = findViewById<TextView>(R.id.question1)
        var qu2 = findViewById<TextView>(R.id.question2)
        var qu3 = findViewById<TextView>(R.id.question3)

        var a1 = findViewById<TextView>(R.id.ans1)
        var a2 = findViewById<TextView>(R.id.ans2)
        var a3 = findViewById<TextView>(R.id.ans3)


        var path = "/connections/$mainConnectionId/answers/$matePlayerId"

        var reference = databaseReference.child(path)

        title()

        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Обработка полученного значения
                for(answer in dataSnapshot.children){
                    var value = answer.value.toString()
//                    answers.add(value)

                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Обработка ошибки
                println("Ошибка: ${databaseError.message}")
            }
        })

        path = "/connections/$mainConnectionId/questions/"
        reference = databaseReference.child(path)

        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Обработка полученного значения
                for(answer in dataSnapshot.children){
                    var value = answer.value.toString()
//                    questions.add(value)

                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Обработка ошибки
                println("Ошибка: ${databaseError.message}")
            }
        })

//        qu1.setText(questions[0].toString())
//        qu2.setText(questions[1].toString())
//        qu3.setText(questions[2].toString())
//
//        a1.setText(answers[0].toString())
//        a2.setText(answers[1].toString())
//        a3.setText(answers[2].toString())



    }

    private fun title() {
        var title = findViewById<TextView>(R.id.title)
        val spannableString = SpannableString("SpeedMating")
        spannableString.setSpan(
            ForegroundColorSpan(Color.parseColor("#FF565E")),
            5,
            6,
            SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        title.text = spannableString

    }

}