package com.example.gyproc.Activites.All.messages

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.gyproc.Activites.All.avvikelser.AvvikelserActivity
import com.example.gyproc.Activites.All.blager.BLagerActivity
import com.example.gyproc.Activites.All.logbook.LogbookActivity
import com.example.gyproc.Activites.All.mainscreen.MainScreenActivity
import com.example.gyproc.R
import com.example.gyproc.Activites.All.messages.NewMessageActivity.Companion.USER_KEY
import com.example.gyproc.models.ChatMessage
import com.example.gyproc.models.User
import com.example.gyproc.Activites.All.registerlogin.RegisterActivity
import com.example.gyproc.Activites.All.schedule.ScheduleActivity
import com.example.gyproc.Activites.All.vatutskott.VatutskottActivity
import com.example.gyproc.views.LatestMessageRow
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_messages.*
import kotlinx.android.synthetic.main.content_messages.*
import kotlinx.android.synthetic.main.nav_header.*
import kotlinx.android.synthetic.main.nav_header.view.*

class MessagesActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var toolbar: Toolbar
    lateinit var drawerLayout: DrawerLayout
    lateinit var navView: NavigationView

    companion object {
        var currentUser: User? = null
        val TAG = "LatestMessages"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_messages)

        recycler_latest_messages.adapter = adapter

        recycler_latest_messages.addItemDecoration(DividerItemDecoration(this,
            DividerItemDecoration.VERTICAL))

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


        adapter.setOnItemClickListener { item, view ->
            val intent = Intent(this, ChatLogActivity::class.java)

           val row = item as LatestMessageRow

            intent.putExtra(USER_KEY,row.chatPartnerUser )

            startActivity(intent)
        }

        listenforLatestMessages()
        fetchCurrentUser()

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



    val latestMessageMap = HashMap<String, ChatMessage>()

    private fun refreshRecyclerViewMessages() {
        adapter.clear()
        latestMessageMap.values.forEach {
            adapter.add(LatestMessageRow(it))
        }
    }

    private fun listenforLatestMessages() {
        val fromId = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/latest-messages/$fromId")
        ref.addChildEventListener(object: ChildEventListener {

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val chatMessage = p0.getValue(ChatMessage::class.java) ?: return
                latestMessageMap[p0.key!!] = chatMessage
                refreshRecyclerViewMessages()
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                val chatMessage = p0.getValue(ChatMessage::class.java) ?: return
                latestMessageMap[p0.key!!] = chatMessage
                refreshRecyclerViewMessages()

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {

            }
            override fun onChildRemoved(p0: DataSnapshot) {
            }
            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }
    val adapter = GroupAdapter<GroupieViewHolder>()


    private fun fetchCurrentUser() {
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {
                currentUser = p0.getValue(User::class.java)
                Log.d("LatestMessages","Current user ${currentUser?.username}")
                Picasso.get().load(MainScreenActivity.currentUser?.profileImageUrl).into(nav_userphoto)
                navView.nav_textview_username.text = MainScreenActivity.currentUser?.username
            }
            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_new_message -> {
                val intent = Intent(this, NewMessageActivity::class.java)
                startActivity(intent)
                
            }

        }
    return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu_chat, menu)
        return super.onCreateOptionsMenu(menu)
    }
    }


