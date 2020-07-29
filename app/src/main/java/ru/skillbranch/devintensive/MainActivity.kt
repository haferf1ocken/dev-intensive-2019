package ru.skillbranch.devintensive

import android.app.Activity
import android.graphics.Color
import android.graphics.PorterDuff

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*
import ru.skillbranch.devintensive.extensions.*
import ru.skillbranch.devintensive.models.Bender
import java.util.*

class MainActivity : AppCompatActivity(), View.OnClickListener {

    private val TAG = "M_MainActivity"

    private lateinit var benderImage: ImageView
    private lateinit var textTxt: TextView
    private lateinit var messageEt: EditText
    private lateinit var sendButton: ImageView

    private lateinit var benderObj: Bender

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        benderImage = iv_bender
        textTxt = tv_text
        messageEt = et_message
        sendButton = iv_send

        val status = savedInstanceState?.getString("STATUS") ?: Bender.Status.NORMAL.name
        val question = savedInstanceState?.getString("QUESTION") ?: Bender.Question.NAME.name
        benderObj = Bender(Bender.Status.valueOf(status), Bender.Question.valueOf(question))

        Log.d(TAG, "onCreate $status $question")

        val (r, g, b) = benderObj.status.color
        benderImage.setColorFilter(Color.rgb(r, g, b), PorterDuff.Mode.MULTIPLY)

        textTxt.text = benderObj.askQuestion()

        messageEt.setOnEditorActionListener() { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE && isValid()) {
                setQuestion()
                this.hideKeyBoard()
                true
            } else {
                false
            }
        }

        messageEt.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val content = messageEt.text?.toString()
                if (!content.isNullOrEmpty()) {
                    messageEt.error = when (benderObj.question) {
                        Bender.Question.NAME ->
                            if (content[0].isUpperCase()) null
                            else "Имя должно начинаться с заглавной буквы"
                        Bender.Question.PROFESSION ->
                            if (content[0].isLowerCase()) null
                            else "Профессия должна начинаться со строчной буквы"
                        Bender.Question.MATERIAL ->
                            if (content.contains(Regex("[^0-9]"))) null
                            else "Материал не должен содержать цифр"
                        Bender.Question.BDAY ->
                            if (content.contains(Regex("[0-9]"))) null
                            else "Год моего рождения должен содержать только цифры"
                        Bender.Question.SERIAL ->
                            if (content.contains(Regex("[0-9]")) && content.length == 7) null
                            else "Серийный номер содержит только цифры, и их 7"
                        Bender.Question.IDLE -> null
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

            sendButton.setOnClickListener(this)
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(TAG, "onRestart")
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putString("STATUS", benderObj.status.name)
        outState.putString("QUESTION", benderObj.question.name)
        Log.d(TAG, "onSaveInstanceState ${benderObj.status.name} ${benderObj.question.name}")
    }

    override fun onClick(v: View?) {
        if (v?.id == R.id.iv_send && isValid()) {
            setQuestion()
        }
        this.hideKeyBoard()
    }

    private fun setQuestion() {
        val (phrase, color) =  benderObj.listenAnswer(messageEt.text.toString()
            .toLowerCase(Locale.ROOT))
        messageEt.setText("")
        val (r,g,b) = color
        benderImage.setColorFilter(Color.rgb(r,g,b), PorterDuff.Mode.MULTIPLY)
        textTxt.text = phrase
    }

    private fun isValid(): Boolean {
        return messageEt.error == null && messageEt.text.isNotEmpty()
    }

}