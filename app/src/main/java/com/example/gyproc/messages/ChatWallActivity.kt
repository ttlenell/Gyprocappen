package com.example.gyproc.messages

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.gyproc.R
import com.example.gyproc.mainscreen.MainScreenActivity
import com.example.gyproc.messages.MessagesActivity.Companion.currentUser
import com.example.gyproc.models.ChatMessage
import com.example.gyproc.models.ChatWall
import com.example.gyproc.models.User
import com.example.gyproc.views.ChatFromItem
import com.example.gyproc.views.ChatToItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_chat_log.*


class ChatWallActivity : AppCompatActivity() {

    companion object {
        val TAG = "Chatlog"
    }

    val adapter = GroupAdapter<GroupieViewHolder>()

    var toUser: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)

        recyclerview_chat.adapter = adapter

        supportActionBar?.title = "Chattvägg"


        listenForMessage()

        send_button_chat.setOnClickListener {
            Log.d(TAG, "Försöker skicka meddelande")
            performSendMessage()
        }

    }

    private fun listenForMessage() {
        val fromId = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/user-wall-messages/$fromId")

        ref.addChildEventListener(object : ChildEventListener {

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val chatWall = p0.getValue((ChatWall::class.java))

                if (chatWall != null) {
                    Log.d(TAG, chatWall.text)

                    if (chatWall.fromId == FirebaseAuth.getInstance().uid) {
                        val currentUser = MainScreenActivity.currentUser ?: return
                        adapter.add(ChatFromItem(chatWall.text, currentUser))
                    }
                }
//                    else {
//                        adapter.add(ChatToItem(chatWall.text, toUser!!))

                recyclerview_chat.scrollToPosition(adapter.itemCount -1)
            }

            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildRemoved(p0: DataSnapshot) {

            }
        })
    }




    private fun performSendMessage() {

        val text = edittext_chat.text.toString()
        val fromId = FirebaseAuth.getInstance().uid
//        val user = intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)
//        val toId = user.uid

        if (fromId == null) return

        val reference = FirebaseDatabase.getInstance()
            .getReference("/user-wall-messages/$fromId").push()
//
//        val toReference = FirebaseDatabase.getInstance()
//            .getReference("/user-wall-messages/$toId/$fromId").push()



        val chatMessage = ChatWall(reference.key!!, text, fromId,
            System.currentTimeMillis() / 1000)

        reference.setValue(chatMessage)
            .addOnSuccessListener {
                Log.d(TAG, "Saved our chat message: ${reference.key}")
                edittext_chat.text.clear()
                recyclerview_chat.scrollToPosition(adapter.itemCount -1)
            }
//        toReference.setValue(chatMessage)
//
//        val latestMessageRef = FirebaseDatabase.getInstance()
//            .getReference("/latest-messages/$fromId/$toId")
//        latestMessageRef.setValue(chatMessage)
//
//        val latestMessageToRef = FirebaseDatabase.getInstance()
//            .getReference("/latest-messages/$toId/$fromId")
//        latestMessageToRef.setValue(chatMessage)

    }
}
