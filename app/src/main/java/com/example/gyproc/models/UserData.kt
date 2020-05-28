package com.example.gyproc.models

import android.util.Log
import com.example.gyproc.Activites.All.mainscreen.MainScreenActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

object UserData {
    val contacts: MutableList<User> = mutableListOf()
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

