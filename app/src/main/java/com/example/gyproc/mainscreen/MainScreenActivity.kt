package com.example.gyproc.mainscreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.gyproc.R
import com.example.gyproc.messages.ChatLogActivity
import com.example.gyproc.messages.ChatWallActivity
import com.example.gyproc.messages.MessagesActivity
import com.example.gyproc.models.User
import com.example.gyproc.registerlogin.RegisterActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main_screen.*

class MainScreenActivity : AppCompatActivity() {

    companion object {
        var currentUser: User? = null
        val TAG = "LatestMessages"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_screen)

        verifyUserIsLoggedIn()

        imageview_main_private_chat.setOnClickListener {

            val intent = Intent(this, MessagesActivity::class.java)
            startActivity(intent)
        }

        imageview_main_chatwall.setOnClickListener {

            val intent = Intent(this,ChatWallActivity::class.java)
            startActivity(intent)
        }
    }

    private fun verifyUserIsLoggedIn() {
        val uid = FirebaseAuth.getInstance().uid
        if (uid == null) {
            val intent = Intent(this, RegisterActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }
}
