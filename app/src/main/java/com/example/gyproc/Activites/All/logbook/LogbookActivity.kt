package com.example.gyproc.Activites.All.logbook

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.gyproc.R
import com.example.gyproc.Activites.All.blager.BLagerActivity
import com.example.gyproc.Activites.All.mainscreen.MainScreenActivity
import com.example.gyproc.Activites.All.mainscreen.MainScreenActivity.Companion.currentUser
import com.example.gyproc.Activites.All.messages.ChatWallActivity
import com.example.gyproc.Activites.All.messages.MessagesActivity
import com.example.gyproc.models.LogBook
import com.example.gyproc.models.User
import com.example.gyproc.models.UserData
import com.example.gyproc.Activites.All.registerlogin.RegisterActivity
import com.example.gyproc.views.LogBookItems
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.content_logbook.*
import kotlinx.android.synthetic.main.nav_header.*
import kotlinx.android.synthetic.main.nav_header.view.*

class LogbookActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    companion object {
        val TAG = "Logbook entries"

    }

    lateinit var toolbar: Toolbar
    lateinit var drawerLayout: DrawerLayout
    lateinit var navView: NavigationView

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
        fetchCurrentUser()
        listenForLogbookEntries()
//        supportActionBar?.title = "Loggbok"

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

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {


            R.id.nav_avvikelser -> {
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
                val intent = Intent(this,LogbookActivity::class.java)
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


    override fun onBackPressed() {
        Log.d(TAG, "onBackPressed Called")
        finish()
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
                Picasso.get().load(currentUser?.profileImageUrl).into(nav_userphoto)
                navView.nav_textview_username.text = currentUser?.username
            }
            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }

    lateinit var user: User
//    lateinit var users : UserData



    fun listenForLogbookEntries() {
//        users = UserData()

        Log.d("HEJ", UserData.contacts.toString())
//        val fromId = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/logbook-entries/")



        ref.orderByChild("timestamp").addChildEventListener(object : ChildEventListener {
//            ref.orderByChild(timeCreated)
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {


                val logBook = p0.getValue(LogBook::class.java)
                if (logBook != null) {
                    Log.d(TAG, logBook.text)

                    val shift = logBook.shift
                    val team = logBook.team
                    val timeCreated = logBook.dateToFirebase

                    if (logBook.fromId == FirebaseAuth.getInstance().uid) {

                        val currentUser = currentUser ?: return
                        user = currentUser


                        Log.d(TAG, "Current user ${MainScreenActivity.currentUser?.username}")

                        adapter.add(LogBookItems(logBook.text, user, shift, team, timeCreated))
                        Log.d(TAG, "lägg till från inloggad user")
                    } else {
                        for (person in UserData.contacts) {
                            Log.d("HEJ1", person.username)
                            if(person.uid == logBook.fromId) {
                                user = person
                                Log.d("HEJ2", user.username)

                                adapter.add(LogBookItems(logBook.text,
                                    user, shift,team,timeCreated))
                                Log.d(TAG, "Lägg till från andra users")

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



