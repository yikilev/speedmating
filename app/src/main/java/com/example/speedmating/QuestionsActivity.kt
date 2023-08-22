package com.example.speedmating

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.os.PersistableBundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.graphics.Color
import androidx.activity.ComponentActivity
import android.os.CountDownTimer
import android.widget.Button
import android.app.ProgressDialog
import android.widget.EditText
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import kotlin.concurrent.timer

class QuestionsActivity : ComponentActivity(){

    val databaseReference: DatabaseReference = FirebaseDatabase.getInstance()
        .getReferenceFromUrl("https://speedmating-85fb4-default-rtdb.firebaseio.com/")
    private lateinit var timerTextView: TextView

    private var questionCounter = 1

    private var mainConnectionId = ""
    private var mainPlayerId = ""
    private var mainMateId = ""

    private var timer: CountDownTimer? = null

    private val answers: MutableMap<String, Any> = mutableMapOf()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        val conId = intent.getStringExtra("conId").toString()
        val curPlayerId = intent.getStringExtra("curPlayerId").toString()
        val matePlayerId = intent.getStringExtra("matePlayerId").toString()

        mainConnectionId = conId
        mainPlayerId = curPlayerId
        mainMateId = matePlayerId
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_questions)


        var title = findViewById<TextView>(R.id.title)
        val spannableString = SpannableString("SpeedMating")
        spannableString.setSpan(ForegroundColorSpan(Color.parseColor("#FF565E")), 5, 6, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE)
        title.text = spannableString

        var bt_send = findViewById<Button>(R.id.bt_send_answer)


        timerTextView = findViewById(R.id.timerTextView)

        var timerTextView = findViewById<TextView>(R.id.timerTextView)
        var timeCounter = 0
        val targetTimeInMillis: Long = 100000

        restartTimer(targetTimeInMillis)

        updateQuestion()


        bt_send.setOnClickListener{
            var questionNumber = findViewById<TextView>(R.id.question_number)
            if(questionCounter < 3) {
                var answer = findViewById<EditText>(R.id.answer)
                answers.put(questionCounter.toString(), answer.text.toString())
                questionCounter++
                updateQuestion()
                restartTimer(targetTimeInMillis)



            }
            else if(questionCounter == 3){
                var answer = findViewById<EditText>(R.id.answer)
                answers.put(questionCounter.toString(), answer.text.toString())
                val questionsRef = databaseReference.child("connections").child(mainConnectionId).child("answers").child(mainPlayerId)
                questionsRef.setValue(answers)

            }


        }

    }

    private fun updateTimerText(millisUntilFinished: Long) {
        val minutes = millisUntilFinished / 1000 / 60
        val seconds = (millisUntilFinished / 1000) % 60

        val formattedTime = String.format("%02d:%02d", minutes, seconds)
        timerTextView.text = formattedTime
    }

    private fun updateQuestion(){
        var questionNumber = findViewById<TextView>(R.id.question_number)

        var path = "/connections/$mainConnectionId/questions/$questionCounter"

        val reference = databaseReference.child(path)

        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Обработка полученного значения
                val questionValue = dataSnapshot.value
                var questionView = findViewById<TextView>(R.id.question)
                questionView.text = questionValue.toString()
                questionNumber.setText("Вопрос " + questionCounter.toString())
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Обработка ошибки
                println("Ошибка: ${databaseError.message}")
            }
        })

//        databaseReference.child(mainConnectionId).child("questions").addListenerForSingleValueEvent(object :
//            ValueEventListener {
//            override fun onDataChange(dataSnapshot: DataSnapshot) {
//                if (dataSnapshot.exists()) {
//                    val questionValue = dataSnapshot.child("1").toString()
//                    var questionView = findViewById<TextView>(R.id.question)
//                    questionView.text = questionValue
//                    questionNumber.setText("Вопрос " + questionCounter.toString())
//                } else {
//                    // Данные по указанному пути не найдены
//                }
//            }
//
//            override fun onCancelled(databaseError: DatabaseError) {
//                // Обработка ошибок, если они возникли
//            }
//
//        })

    }
    private fun restartTimer(targetTimeInMillis: Long) {
        timer?.cancel()
        timer = object : CountDownTimer(targetTimeInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                updateTimerText(millisUntilFinished)
            }

            override fun onFinish() {
                if (questionCounter < 3) {
                    var answer = findViewById<EditText>(R.id.answer)
                    answers.put(questionCounter.toString(), answer.text.toString())
                    questionCounter++
                    updateTimerText(0)
                    restartTimer(targetTimeInMillis)
                    updateQuestion()
                }
                else if(questionCounter == 3){

                    var answer = findViewById<EditText>(R.id.answer)
                    answers.put(questionCounter.toString(), answer.text.toString())
                    val questionsRef = databaseReference.child("connections").child(mainConnectionId).child("answers").child(mainPlayerId)
                    questionsRef.setValue(answers)
                }
            }
        }

        timer?.start()
    }
}
