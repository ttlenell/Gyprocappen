package com.example.gyproc.models

import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat.startActivity
import com.example.gyproc.mainscreen.MainScreenActivity
import com.example.gyproc.messages.ChatLogActivity
import com.example.gyproc.messages.MessagesActivity
import com.example.gyproc.messages.NewMessageActivity
import com.example.gyproc.messages.UserItem
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_new_message.*

class UserData(val contacts: MutableList<User> = mutableListOf()) {
    init {

        val ref = FirebaseDatabase.getInstance().getReference("/users")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {


                p0.children.forEach {
                    Log.d("Main", it.toString())
                    val user = it.getValue(User::class.java)

                    if (user != null) {
                        if (user.username != MainScreenActivity.currentUser?.username) {
                            contacts.add(user)
                        }
                    }

                }
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }
}

