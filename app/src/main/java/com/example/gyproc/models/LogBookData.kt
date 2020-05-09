package com.example.gyproc.models

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LogBookData(val entries: MutableList<LogBook> = mutableListOf()) {
    init {

        val ref = FirebaseDatabase.getInstance().getReference("/logbook-entries")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {


                p0.children.forEach {
                    Log.d("Main", it.toString())
                    val entry = it.getValue(LogBook::class.java)

                    if (entry != null) {
                        entries.add(entry)
                    }

                }
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }
}

