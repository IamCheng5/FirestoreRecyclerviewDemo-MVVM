package com.iamcheng5.firestorerecyclerviewdemo_mvvm

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText

class NewPostActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_post)
        val etTitle = findViewById<EditText>(R.id.newPostAct_et_title)
        val etDescription = findViewById<EditText>(R.id.newPostAct_et_description)
        val etContent = findViewById<EditText>(R.id.newPostAct_et_content)
        findViewById<Button>(R.id.newWordAct_btn_create).setOnClickListener {
            val replyIntent = Intent()
            val title = etTitle.text.toString().trim()
            val description = etDescription.text.toString().trim()
            val content = etContent.text.toString().trim()
            if (TextUtils.isEmpty(title) || TextUtils.isEmpty(description) ||TextUtils.isEmpty(content)) {
                setResult(RESULT_CANCELED, replyIntent)
            } else {
                replyIntent.putExtra(EXTRA_REPLY,Post("",title,description,content))
                setResult(RESULT_OK, replyIntent)
            }
            finish()

        }
    }

    companion object {
        const val EXTRA_REPLY: String = "com.iamcheng5.firestorerecyclerviewdemo_mvvm.REPLY"
    }
}