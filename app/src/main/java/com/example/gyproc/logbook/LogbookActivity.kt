package com.example.gyproc.logbook

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.gyproc.R
import com.example.gyproc.mainscreen.MainScreenActivity
import com.example.gyproc.models.LogBookEntry
import com.example.gyproc.views.LogBookItems
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_logbook.*

class LogbookActivity : AppCompatActivity() {

  companion object {
      val TAG = "Logbook entries"
  }

    val adapter = GroupAdapter<GroupieViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logbook)


        recyclerview_logbook.adapter = adapter
        recyclerview_logbook.addItemDecoration(DividerItemDecoration(this,DividerItemDecoration
            .VERTICAL))

        setupDummyLog()
        listenForLogbookEntries()

        fab_logbook_add.setOnClickListener {

            val intent = Intent(this,
                LogbookAddActivity::class.java)
            startActivity(intent)
        }

    }
    val latestLogbookEntries = HashMap<String, LogBookEntry>()

    private fun setupDummyLog() {
        val adapter = GroupAdapter<GroupieViewHolder>()
        adapter.clear()
        val currentUser = MainScreenActivity.currentUser ?: return
        latestLogbookEntries.values.forEach {
            adapter.add(LogBookItems("hej", currentUser))
            adapter.add(LogBookItems("hej2", currentUser))
        }

    }

    fun listenForLogbookEntries() {
        val fromId = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().
        getReference("/logbook-entries/$fromId")


        ref.addChildEventListener(object : ChildEventListener {

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val logBookEntry = p0.getValue(LogBookEntry::class.java)

                if(logBookEntry != null) {
                    Log.d(TAG,logBookEntry?.title)

                    if(logBookEntry.fromId == FirebaseAuth.getInstance().uid) {

                        val currentUser = MainScreenActivity.currentUser ?: return
                        Log.d(TAG, "$currentUser")

                        adapter.add(LogBookItems(logBookEntry.title, currentUser))
                        Log.d(TAG, "försöker lägga till i adapter")
                    }
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
