package com.example.gyproc.Activites.All.mainscreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.gyproc.Activites.All.avvikelser.AvvikelserActivity
import com.example.gyproc.R
import com.example.gyproc.Activites.All.blager.BLagerActivity
import com.example.gyproc.Activites.All.logbook.LogbookActivity
import com.example.gyproc.Activites.All.messages.ChatWallActivity
import com.example.gyproc.Activites.All.messages.MessagesActivity
import com.example.gyproc.models.User
import com.example.gyproc.Activites.All.registerlogin.RegisterActivity
import com.example.gyproc.Activites.All.schedule.ScheduleActivity
import com.example.gyproc.Activites.All.vatutskott.VatutskottActivity
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.nav_header.*
import kotlinx.android.synthetic.main.nav_header.view.*


class MainScreenActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    companion object {
        var currentUser: User? = null
        val TAG = "LatestMessages"
    }

    lateinit var toolbar: Toolbar
    lateinit var drawerLayout: DrawerLayout
    lateinit var navView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_screen)

        verifyUserIsLoggedIn()
        fetchCurrentUser()

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

        imageview_main_logbook.setOnClickListener {
            val intent = Intent(this, LogbookActivity::class.java)
            startActivity(intent)
        }

        imageview_main_private_chat.setOnClickListener {

            val intent = Intent(this, MessagesActivity::class.java)
            startActivity(intent)
        }

        imageview_main_chatwall.setOnClickListener {

            val intent = Intent(this, ChatWallActivity::class.java)
            startActivity(intent)
        }

        imageview_main_blager.setOnClickListener {
            val intent = Intent(this, BLagerActivity::class.java)
            startActivity(intent)
        }

        imageview_main_schedule.setOnClickListener {
            val intent = Intent(this, ScheduleActivity::class.java)
            startActivity(intent)
        }

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
                val intent = Intent(this,LogbookActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_vatutskott -> {
                val intent = Intent(this,VatutskottActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_schedule -> {
                val intent = Intent(this,ScheduleActivity::class.java)
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


    private fun verifyUserIsLoggedIn() {
        val uid = FirebaseAuth.getInstance().uid
        if (uid == null) {
            val intent = Intent(this, RegisterActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

    private fun fetchCurrentUser() {
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {
                currentUser = p0.getValue(User::class.java)
                Log.d(TAG,"Current user ${MainScreenActivity.currentUser?.username}")
                Picasso.get().load(currentUser?.profileImageUrl).into(nav_userphoto)
                navView.nav_textview_username.text = currentUser?.username
            }
            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }
}
