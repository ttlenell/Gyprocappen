package com.example.gyproc.logbook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.gyproc.R
import com.example.gyproc.mainscreen.MainScreenActivity
import com.example.gyproc.mainscreen.MainScreenActivity.Companion.currentUser
import com.example.gyproc.models.LogBook
import com.example.gyproc.models.LogBookData
import com.example.gyproc.models.User
import com.example.gyproc.views.LogBookItems
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_logbook.*

class LogbookActivity : AppCompatActivity() {

    companion object {
        val TAG = "Logbook entries"

        //      var currentUser: User? = null
        val USER_KEY = "USER_KEY"
    }

    val adapter = GroupAdapter<GroupieViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logbook)

        recyclerview_logbook.adapter = adapter
        recyclerview_logbook.addItemDecoration(
            DividerItemDecoration(
                this, DividerItemDecoration
                    .VERTICAL
            )
        )

        supportActionBar?.title = "Loggbok"

        fetchCurrentUser()
        listenForLogbookEntries()

        fab_logbook_add.setOnClickListener {

            val intent = Intent(
                this,
                LogbookAddActivity::class.java)
//            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

    private fun fetchCurrentUser() {
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {
                currentUser = p0.getValue(User::class.java)
//                Log.d(TAG,"Current user ${MainScreenActivity.currentUser?.username}")
            }
            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }

    lateinit var entries: LogBookData
    lateinit var user: User

    fun listenForLogbookEntries() {

        entries = LogBookData()

//        val fromId = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/logbook-entries")


        ref.addChildEventListener(object : ChildEventListener {

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val logBook = p0.getValue(LogBook::class.java)

                if (logBook != null) {
                    Log.d(TAG, logBook.text)

                    val currentUser = currentUser ?: return
                    user = currentUser

                    Log.d(TAG,"Current user ${MainScreenActivity.currentUser?.username}")
                    adapter.add(LogBookItems(logBook.text, user))
                    Log.d(TAG, "försöker lägga till i adapter")


//                    for (entry in entries.entries) {
//                        if (entry.id == logBook.fromId) {
////                            user = user
//                            Log.d(TAG, entry.id + entry.fromId + entry.shift)
//
//
//                            if (logBook.fromId == FirebaseAuth.getInstance().uid) {
//
//                                adapter.add(LogBookItems(logBook.text, user))
//                                Log.d(TAG, "försöker lägga till i adapter")
//                            }
//                        }
//                    }
                        }
                    }




            override fun onCancelled(p0: DatabaseError) {
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
            }

            override fun onChildRemoved(p0: DataSnapshot) {
            }

        })

    }
}

