package com.example.gyproc.Activites.All.messages

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.gyproc.R
import com.example.gyproc.Activites.All.messages.MessagesActivity.Companion.currentUser
import com.example.gyproc.models.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.activity_new_message.*
import kotlinx.android.synthetic.main.user_row_new_message.view.*

class NewMessageActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_message)

        supportActionBar?.title = "Select User"

        val adapter = GroupAdapter<GroupieViewHolder>()


        recyclerview_newmessage.adapter = adapter
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

        fetchUsers()
    }

    companion object {
        val USER_KEY = "USER_KEY"
    }

    // hämta alla tillgängliga users

    private fun fetchUsers() {
      val ref = FirebaseDatabase.getInstance().getReference("/users")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {

                val adapter = GroupAdapter<GroupieViewHolder>()


                p0.children.forEach {
                    Log.d("Main", it.toString())
                    val user = it.getValue(User::class.java)

                    if (user != null) {
                        if (user.username != currentUser?.username) {
                            adapter.add(UserItem(user))
                        }
                    }

                }

                adapter.setOnItemClickListener { item, view ->

                    val userItem = item as UserItem

                    val intent = Intent(view.context, ChatLogActivity::class.java)
                 //   intent.putExtra(USER_KEY, userItem.user.username)
                    intent.putExtra(USER_KEY, userItem.user )
                    startActivity(intent)

                    finish()
                }

                recyclerview_newmessage.adapter = adapter
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }
}

class UserItem (val user : User): Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

        viewHolder.itemView.username_textview_newmessage.text = user.username

        Picasso.get().load(user.profileImageUrl).into(viewHolder.itemView.imageview_newmessage_row)
    }



    override fun getLayout(): Int {
        return R.layout.user_row_new_message
    }
}

