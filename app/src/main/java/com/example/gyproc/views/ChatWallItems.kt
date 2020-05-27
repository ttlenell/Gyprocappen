package com.example.gyproc.views

import com.example.gyproc.R
import com.example.gyproc.models.User
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.chat_from_row.view.*
import kotlinx.android.synthetic.main.chatwall_from_others_row.view.*
import kotlinx.android.synthetic.main.chatwall_from_row.view.*
import java.text.SimpleDateFormat

class ChatWallFrom(val text: String, val user: User, private val timestamp: String): Item<GroupieViewHolder>() {


    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.timeView_chatwall_from_row.text = timestamp
        viewHolder.itemView.textview_chatwall_from_row.text = text
        viewHolder.itemView.username_textview_chatwall_from_row.text = user.username

        val uri = user.profileImageUrl
        val targetImageView = viewHolder.itemView.imageview_chatwall_from_row
        Picasso.get().load(uri).into(targetImageView)

    }

    override fun getLayout(): Int {

        return R.layout.chatwall_from_row

    }
}

class ChatWallFromOthers(val text: String, val user: User, private val timestamp : String) :
    Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

        viewHolder.itemView.textView_chatwall_from_others_row.text = text
        viewHolder.itemView.timeView_chatwall_from_others_row.text = timestamp
        viewHolder.itemView.username_textview_chatwall_from_others_row.text = user.username

        val uri = user.profileImageUrl
        val targetImageView = viewHolder.itemView.imageview_chatwall_from_others_row
        Picasso.get().load(uri).into(targetImageView)

    }

    override fun getLayout(): Int {

        return R.layout.chatwall_from_others_row

    }
}