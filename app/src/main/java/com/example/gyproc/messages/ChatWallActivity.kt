package com.example.gyproc.messages

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.gyproc.R
import com.example.gyproc.mainscreen.MainScreenActivity
import com.example.gyproc.mainscreen.MainScreenActivity.Companion.currentUser
import com.example.gyproc.models.ChatWall
import com.example.gyproc.models.User
import com.example.gyproc.models.UserData
import com.example.gyproc.views.ChatFromItem
import com.example.gyproc.views.ChatToItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_chat_log.*
import kotlinx.android.synthetic.main.activity_chat_log.edittext_chat
import kotlinx.android.synthetic.main.activity_chat_log.recyclerview_chat
import kotlinx.android.synthetic.main.activity_chat_log.send_button_chat
import kotlinx.android.synthetic.main.activity_chat_wall.*


class ChatWallActivity : AppCompatActivity() {

    companion object {
        val TAG = "ChatWall"
    }

    val adapter = GroupAdapter<GroupieViewHolder>()
//    val currentUser = MainScreenActivity.currentUser
//    var toUser: User? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_wall)

        recyclerview_chat_wall.adapter = adapter

        supportActionBar?.title = "Chattvägg"

        fetchCurrentUser()
        listenForMessage()

        send_button_chat_wall.setOnClickListener {
            Log.d(TAG, "Försöker skicka meddelande")
            performSendMessage()
        }

    }



    private fun fetchCurrentUser() {
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {
                currentUser = p0.getValue(User::class.java)
                Log.d(TAG,"Current user ${MainScreenActivity.currentUser?.username}")
            }
            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }

    lateinit var user : User
    lateinit var users : UserData

    private fun listenForMessage() {
        users = UserData()
        val fromId = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/user-wall-messages")

        ref.addChildEventListener(object : ChildEventListener {

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val chatWall = p0.getValue((ChatWall::class.java))

                if (chatWall != null ) {
                    Log.d(TAG, chatWall.text)

                    if (chatWall.fromId == FirebaseAuth.getInstance().uid) {

                        user = currentUser!!
                        Log.d(TAG, "$currentUser")
                        adapter.add(ChatFromItem(chatWall.text, user))


                    } else {
                        for (person in users.contacts) {
                            if(person.uid == chatWall.fromId) {
                                user = person
                                adapter.add(ChatToItem(chatWall.text, user))

                            }
                        }
//                        user = UserData().contacts
                    }



//                    adapter.add(ChatFromItem(chatWall.text, user))
                    Log.d(TAG, "försöker lägga till i adapter")

                }
                recyclerview_chat_wall.scrollToPosition(adapter.itemCount -1)
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

        val text = edittext_chat_wall.text.toString()
        val fromId = FirebaseAuth.getInstance().uid ?: return
//        val user = intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)
//        val toId = user.uid

        val reference = FirebaseDatabase.getInstance()
            .getReference("/user-wall-messages").push()

//        val toReference = FirebaseDatabase.getInstance()
//            .getReference("/user-wall-messages/$fromId").push()

        val chatWall = ChatWall(reference.key!!, text, fromId,
            System.currentTimeMillis() / 1000)

        reference.setValue(chatWall)
            .addOnSuccessListener {
                Log.d(TAG, "Saved our chat message: ${reference.key}")
                edittext_chat_wall.text.clear()
                recyclerview_chat_wall.scrollToPosition(adapter.itemCount -1)
            }


   }
}