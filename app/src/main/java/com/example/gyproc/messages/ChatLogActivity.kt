package com.example.gyproc.messages

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.gyproc.R

class ChatLogActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)

        supportActionBar?.title = "Chat Log"
    }
}
