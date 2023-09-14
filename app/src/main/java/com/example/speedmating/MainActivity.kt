package com.example.speedmating

import android.app.Activity
import android.app.ActivityOptions
import android.app.AlertDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.widget.Button
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.internal.composableLambdaInstance
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.speedmating.ui.theme.SpeedMatingTheme
import com.google.android.material.card.MaterialCardView
import com.google.firebase.FirebaseApp
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.example.speedmating.R
import java.util.UUID

class MainActivity : ComponentActivity() {

    private var lang_chosen = ""
    private var mainPlayerId = ""
    private var mateFound = false
    private var mainMateId = ""
    val databaseReference: DatabaseReference = FirebaseDatabase.getInstance()
        .getReferenceFromUrl("https://speedmating-85fb4-default-rtdb.firebaseio.com/")
    private var status = "searching"

    private var mainConnectionId = ""

    fun testCon(){

        FirebaseApp.initializeApp(this)

        mainPlayerId = UUID.randomUUID().toString()
        var status = "searching"

        val progressDialog = ProgressDialog(this@MainActivity)
        progressDialog.setTitle("Поиск собеседника")
        progressDialog.setMessage("Ищем свободного собеседника")
        progressDialog.show()

        databaseReference.child("connections").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(!mateFound) {
                    if (snapshot.hasChildren()) {
                        for (connections in snapshot.children) {
                            val conId = connections.key.toString()
                            var getMatesCount = connections.childrenCount.toInt()
                            if (status.equals("waiting")) {
                                if (getMatesCount == 2) {
                                    var curUser = false
                                    var mateId = ""
                                    for (players in connections.children){
                                        val getPlayerId = players.key.toString()
                                        if(getPlayerId.equals(mainPlayerId)){
                                            curUser = true
                                        }
                                        else{
                                            mateId = players.key.toString()

                                        }
                                    }
                                    if(curUser) {
                                        mateFound = true
                                        mainMateId = mateId
                                        mainConnectionId = conId

                                        addQuestions()

                                        if (progressDialog.isShowing()) {
                                            progressDialog.dismiss()
                                        }
                                        databaseReference.child("connections")
                                            .removeEventListener(this)


                                        val intent = Intent(this@MainActivity, QuestionsActivity::class.java)

                                        intent.putExtra("conId", mainConnectionId)
                                        intent.putExtra("curPlayerId", mainPlayerId)
                                        intent.putExtra("matePlayerId", mainMateId)
                                        startActivity(intent)
                                    }
                                }
                            }
                            else{
                                if(getMatesCount == 1){
                                    connections.child(mainPlayerId).child("language").ref.setValue(lang_chosen)
                                    for(players in connections.children){
                                        val getMateId = players.key.toString()
                                        mainConnectionId = conId
                                        mateFound = true
                                        mainMateId = getMateId

                                        addQuestions()

                                        if (progressDialog.isShowing()) {
                                            progressDialog.dismiss()
                                        }
                                        databaseReference.child("connections").removeEventListener(this)

                                        val intent = Intent(this@MainActivity, QuestionsActivity::class.java)

                                        intent.putExtra("conId", mainConnectionId)
                                        intent.putExtra("curPlayerId", mainPlayerId)
                                        intent.putExtra("matePlayerId", mainMateId)
                                        startActivity(intent)

                                        break
                                    }
                                }
                            }
                        }

                        if(!mateFound && !status.equals("waiting")){
                            val mainConId = UUID.randomUUID().toString()
                            databaseReference.child("connections").child(mainConId).child(mainPlayerId).apply {
                                child("language").setValue(lang_chosen)
                            }
                            status = "waiting"
                        }

                    } else {
                        val mainConId = UUID.randomUUID().toString()
                        databaseReference.child("connections").child(mainConId).child(mainPlayerId).apply {
                            child("language").setValue(lang_chosen)
                        }

                        status = "waiting"


                    }
                }


            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        title()

        val bt_start = findViewById<Button>(R.id.bt_start)
        val cn_lang = findViewById<MaterialCardView>(R.id.cn_lang)
        val gb_lang = findViewById<MaterialCardView>(R.id.gb_lang)
        val fr_lang = findViewById<MaterialCardView>(R.id.fr_lang)
        val ger_lang = findViewById<MaterialCardView>(R.id.ger_lang)
        val ru_lang = findViewById<MaterialCardView>(R.id.ru_lang)
        val sp_lang = findViewById<MaterialCardView>(R.id.sp_lang)


        cn_lang.setOnClickListener {
            selectCardView(cn_lang)
            deselectCardView(gb_lang)
            deselectCardView(fr_lang)
            deselectCardView(ger_lang)
            deselectCardView(ru_lang)
            deselectCardView(sp_lang)
            lang_chosen = "Chinese"


        }
        gb_lang.setOnClickListener {
            selectCardView(gb_lang)
            deselectCardView(cn_lang)
            deselectCardView(fr_lang)
            deselectCardView(ger_lang)
            deselectCardView(ru_lang)
            deselectCardView(sp_lang)
            lang_chosen = "English"

        }
        fr_lang.setOnClickListener {
            selectCardView(fr_lang)
            deselectCardView(gb_lang)
            deselectCardView(cn_lang)
            deselectCardView(ger_lang)
            deselectCardView(ru_lang)
            deselectCardView(sp_lang)
            lang_chosen = "French"

        }
        ger_lang.setOnClickListener {
            selectCardView(ger_lang)
            deselectCardView(gb_lang)
            deselectCardView(fr_lang)
            deselectCardView(cn_lang)
            deselectCardView(ru_lang)
            deselectCardView(sp_lang)
            lang_chosen = "German"

        }
        ru_lang.setOnClickListener {
            selectCardView(ru_lang)
            deselectCardView(gb_lang)
            deselectCardView(fr_lang)
            deselectCardView(ger_lang)
            deselectCardView(cn_lang)
            deselectCardView(sp_lang)
            lang_chosen = "Russian"

        }
        sp_lang.setOnClickListener {
            selectCardView(sp_lang)
            deselectCardView(gb_lang)
            deselectCardView(fr_lang)
            deselectCardView(ger_lang)
            deselectCardView(ru_lang)
            deselectCardView(cn_lang)
            lang_chosen = "Spanish"

        }

        bt_start.setOnClickListener {
            testCon()
        }


    }

    private fun selectCardView(selectedCardView: MaterialCardView) {
        selectedCardView.strokeWidth = resources.getDimensionPixelSize(R.dimen.stroke_width_selected)
        selectedCardView.strokeColor = getColor(R.color.select_green)
    }

    private fun deselectCardView(cardView: MaterialCardView) {
        cardView.setStrokeWidth(0)
    }


    override fun onBackPressed() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_exit_sm, null)
        val dialog = Dialog(this)
        dialog.setContentView(dialogView)
        val dialogButtonYes = dialogView.findViewById<CardView>(R.id.dialogButtonYes)
        val dialogButtonClose = dialogView.findViewById<CardView>(R.id.closeDialogButton)

        dialogButtonYes.setOnClickListener {
            // Закрываем диалоговое окно
            dialog.dismiss()
        }

        dialogButtonClose.setOnClickListener {
            // Закрываем приложение
            finish()
        }

        dialog.show()
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

    private fun addQuestions() {
//        databaseReference.child(mainConnectionId).child("questions").child("1")
//            .setValue(resources.getStringArray(R.array.sm_questions)[(0 until 20).random()])
//
//        databaseReference.child(mainConnectionId).child("questions").child("2")
//            .setValue(resources.getStringArray(R.array.sm_questions)[(0 until 20).random()])
//
//        databaseReference.child(mainConnectionId).child("questions").child("3")
//            .setValue(resources.getStringArray(R.array.sm_questions)[(0 until 20).random()])
            val questions = mapOf(
                "1" to resources.getStringArray(R.array.sm_questions)[(0 until 20).random()],
                "2" to resources.getStringArray(R.array.sm_questions)[(0 until 20).random()],
                "3" to resources.getStringArray(R.array.sm_questions)[(0 until 20).random()]
            )

            val questionsRef = databaseReference.child("connections").child(mainConnectionId).child("questions")
            questionsRef.setValue(questions)


    }

}
