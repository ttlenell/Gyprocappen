package com.example.gyproc.Activites.All.blager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.gyproc.Activites.All.avvikelser.AvvikelserActivity
import com.example.gyproc.Activites.All.logbook.LogbookActivity
import com.example.gyproc.Activites.All.logbook.LogbookActivity.Companion.TAG
import com.example.gyproc.Activites.All.mainscreen.MainScreenActivity
import com.example.gyproc.Activites.All.messages.ChatWallActivity
import com.example.gyproc.Activites.All.messages.MessagesActivity
import com.example.gyproc.Activites.All.registerlogin.RegisterActivity
import com.example.gyproc.Activites.All.schedule.ScheduleActivity
import com.example.gyproc.Activites.All.vatutskott.VatutskottActivity
import com.example.gyproc.R
import com.example.gyproc.models.BLager
import com.example.gyproc.models.LogBook
import com.example.gyproc.models.User
import com.example.gyproc.models.UserData
import com.example.gyproc.views.BLagerItems
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

class BLagerActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    companion object {
        val TAG = "B-Lager entries"
    }

    val adapter = GroupAdapter<GroupieViewHolder>()

    lateinit var toolbar: Toolbar
    lateinit var drawerLayout: DrawerLayout
    lateinit var navView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_b_lager)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

            // sätter in vertikala linjer mellan varje inlägg

            recyclerview_data_posts.addItemDecoration(
            DividerItemDecoration(
                this, DividerItemDecoration
                    .VERTICAL
            )
        )

        fetchCurrentUser()
        listenForBLagerEntries()
        recyclerview_data_posts.adapter = adapter


        fab_add_post.setOnClickListener {

            val intent = Intent(
                this,
                BLagerAddActivity::class.java)
            startActivity(intent)
        }

        // koden för actionbar och hamburgarmeny

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

    private fun fetchCurrentUser() {
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {
                MainScreenActivity.currentUser = p0.getValue(User::class.java)
                Picasso.get().load(MainScreenActivity.currentUser?.profileImageUrl).into(nav_userphoto)
                navView.nav_textview_username.text = MainScreenActivity.currentUser?.username
            }
            override fun onCancelled(p0: DatabaseError) {

            }
        })
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

    lateinit var user: User
//    lateinit var users : UserData



    fun listenForBLagerEntries() {
//        users = UserData()

        Log.d("HEJ", UserData.contacts.toString())
//        val fromId = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/b_lager-entries/")



        ref.orderByChild("timestamp").addChildEventListener(object : ChildEventListener {
            //            ref.orderByChild(timeCreated)
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {


                val bLager = p0.getValue(BLager::class.java)
                if (bLager != null) {
                    Log.d(TAG, bLager.text)

                    val team = bLager.team
                    val timeCreated = bLager.dateToFireBase
                    val product = bLager.product
                    val amount = bLager.amount

                    if (bLager.fromId == FirebaseAuth.getInstance().uid) {

                        val currentUser = MainScreenActivity.currentUser ?: return
                        user = currentUser


                        Log.d(TAG, "Current user ${MainScreenActivity.currentUser?.username}")

                        adapter.add(BLagerItems(bLager.text, user, team,product, amount, timeCreated))
                        Log.d(TAG, "lägg till från inloggad user")
                    } else {
                        for (person in UserData.contacts) {
                            Log.d("HEJ1", person.username)
                            if(person.uid == bLager.fromId) {
                                user = person
                                Log.d("HEJ2", user.username)

                                adapter.add(
                                    BLagerItems(bLager.text,
                                    user,team,timeCreated,product,amount)
                                )
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
