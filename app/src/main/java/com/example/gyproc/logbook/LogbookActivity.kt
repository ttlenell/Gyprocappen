package com.example.gyproc.logbook

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.gyproc.R
import com.example.gyproc.mainscreen.MainScreenActivity
import com.example.gyproc.mainscreen.MainScreenActivity.Companion.currentUser
import com.example.gyproc.models.LogBook
import com.example.gyproc.models.User
import com.example.gyproc.models.UserData
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
//        val USER_KEY = "USER_KEY"
//
//
//        const val POSITION_KEY = "POSITION_KEY"
    }

    val adapter = GroupAdapter<GroupieViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logbook)

        recyclerview_data_posts.adapter = adapter

        recyclerview_data_posts.addItemDecoration(
            DividerItemDecoration(
                this, DividerItemDecoration
                    .VERTICAL
            )
        )
onResume()
        supportActionBar?.title = "Loggbok"

        fetchCurrentUser()
//        fetchClickedLog()
        listenForLogbookEntries()


        fab_add_post.setOnClickListener {

            val intent = Intent(
                this,
                LogbookAddActivity::class.java)
            startActivity(intent)
        }

    }

    override fun onResume() {
        super.onResume()
        adapter.notifyDataSetChanged()
    }


    override fun onBackPressed() {
        Log.d(TAG, "onBackPressed Called")
        val setIntent = Intent(this, MainScreenActivity::class.java)
//        setIntent.addCategory(Intent.CATEGORY_HOME)
//        setIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(setIntent)
        return
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

    lateinit var user: User
    lateinit var users : UserData



    fun listenForLogbookEntries() {

        users = UserData()

        val fromId = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/logbook-entries/")

        ref.addChildEventListener(object : ChildEventListener {

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val logBook = p0.getValue(LogBook::class.java)
                if (logBook != null) {
                    Log.d(TAG, logBook.text)
                    val shift = logBook.shift
                    val timeCreated = logBook.dateToFirebase


                    if (logBook.fromId == FirebaseAuth.getInstance().uid) {

                        val currentUser = currentUser ?: return
                        user = currentUser


                        Log.d(TAG, "Current user ${MainScreenActivity.currentUser?.username}")
                        adapter.add(LogBookItems(logBook.text, user, shift, timeCreated))
                        Log.d(TAG, "försöker lägga till i adapter")
                    } else {
                        for (person in users.contacts) {
                            if(person.uid == logBook.fromId) {
                                user = person



                                adapter.add(LogBookItems(logBook.text,
                                    user, shift,timeCreated))

                            }
                        }
                    }
                }
//                recyclerview_data_posts.scrollToPosition(adapter.itemCount -1)

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



