package com.example.gyproc.Activites.All.messages

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.gyproc.Activites.All.avvikelser.AvvikelserActivity
import com.example.gyproc.Activites.All.blager.BLagerActivity
import com.example.gyproc.Activites.All.logbook.LogbookActivity
import com.example.gyproc.R
import com.example.gyproc.Activites.All.mainscreen.MainScreenActivity
import com.example.gyproc.Activites.All.mainscreen.MainScreenActivity.Companion.currentUser
import com.example.gyproc.Activites.All.registerlogin.RegisterActivity
import com.example.gyproc.Activites.All.schedule.ScheduleActivity
import com.example.gyproc.Activites.All.vatutskott.VatutskottActivity
import com.example.gyproc.models.ChatWall
import com.example.gyproc.models.User
import com.example.gyproc.models.UserData
import com.example.gyproc.views.ChatWallFrom
import com.example.gyproc.views.ChatWallFromOthers
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_chat_wall.*
import kotlinx.android.synthetic.main.content_chatwall.*


class ChatWallActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var toolbar: Toolbar
    lateinit var drawerLayout: DrawerLayout
    lateinit var navView: NavigationView


    companion object {
        val TAG = "ChatWall"
    }

    val adapter = GroupAdapter<GroupieViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_wall)

        recyclerview_chat_wall.adapter = adapter

//        supportActionBar?.title = "Chattvägg"
        listenForMessage()
        fetchCurrentUser()

        send_button_chat_wall.setOnClickListener {
            Log.d(TAG, "Försöker skicka meddelande")
            performSendMessage()
        }

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, 0, 0
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navView.setNavigationItemSelectedListener(this)

    }

    override fun onBackPressed() {
        super.onBackPressed()
        Log.d(LogbookActivity.TAG, "onBackPressed Called")
//        val setIntent = Intent(this, MainScreenActivity::class.java)
//        startActivity(setIntent)
//        onStop()
        adapter.notifyDataSetChanged()
        finish()

        return
    }

    override fun onResume() {
        super.onResume()

        adapter.notifyDataSetChanged()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {

            R.id.nav_mainscreen -> {
                val intent = Intent(this, MainScreenActivity::class.java)
                startActivity(intent)
                finish()
            }

            R.id.nav_avvikelser -> {
                val intent = Intent(this, AvvikelserActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_messages_private -> {
                val intent = Intent(this, MessagesActivity::class.java)
                startActivity(intent)
            }

            R.id.nav_messages_wall -> {
                val intent = Intent(this, ChatWallActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_b_lager -> {
                val intent = Intent(this, BLagerActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_logbook -> {
                val intent = Intent(this, LogbookActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_vatutskott -> {
                val intent = Intent(this, VatutskottActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_schedule -> {
                val intent = Intent(this, ScheduleActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_logout -> {
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(this, RegisterActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }



    private fun fetchCurrentUser() {
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {
                currentUser = p0.getValue(User::class.java)
                Log.d(TAG,"Current user ${currentUser?.username}")
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
                        adapter.add(ChatWallFrom(chatWall.text, user))

                        Log.d(TAG, "försöker lägga till från inloggad")


                    } else {
                        for (person in users.contacts) {
                            if(person.uid == chatWall.fromId) {
                                user = person


                                adapter.add(ChatWallFromOthers(chatWall.text,user)
                                )
                                Log.d(TAG, "försöker lägga till från andra")

                            }
                        }

                    }


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

        val reference = FirebaseDatabase.getInstance()
            .getReference("/user-wall-messages").push()


        val chatWall = ChatWall(reference.key!!, text, fromId)

        reference.setValue(chatWall)
            .addOnSuccessListener {
                Log.d(TAG, "Saved our chat message: ${reference.key}")
                edittext_chat_wall.text.clear()
                recyclerview_chat_wall.scrollToPosition(adapter.itemCount -1)
            }


   }
}