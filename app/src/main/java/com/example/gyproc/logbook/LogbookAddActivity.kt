package com.example.gyproc.logbook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.gyproc.R
import com.example.gyproc.messages.ChatLogActivity
import com.example.gyproc.models.ChatMessage
import com.example.gyproc.models.LogBookEntry
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_chat_log.*
import kotlinx.android.synthetic.main.activity_logbook.*
import java.text.SimpleDateFormat
import java.util.*

class LogbookAddActivity : AppCompatActivity() {

    companion object {
        val TAG = "LogbookAdd"
        val USER_KEY = "USER_KEY"
    }

    val adapter = GroupAdapter<GroupieViewHolder>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logbook_add)

        recyclerview_logbook.adapter = adapter
    }

    fun addLogbookItem() {

        val fromId = FirebaseAuth.getInstance().uid
        val reference = FirebaseDatabase.getInstance()
            .getReference("/logbook-entries/$fromId").push()

//        val dateFormat: SimpleDateFormat = SimpleDateFormat("HH,MM dd/MM/yyyy", Locale.ENGLISH)
//        recyclerview_logbook.add(dateFormat.format(Date()))

        val logBookEntry = LogBookEntry(reference.key!!, "","")

        reference.setValue(logBookEntry)
            .addOnSuccessListener {
                Log.d(TAG, "Saved the entry to the logbook: ${reference.key}")
                edittext_chat.text.clear()
                recyclerview_logbook.scrollToPosition(adapter.itemCount - 1)


            }
    }
}

