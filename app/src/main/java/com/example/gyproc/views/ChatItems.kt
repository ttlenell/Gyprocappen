package com.example.gyproc.views

import com.example.gyproc.R
import com.example.gyproc.models.User
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.chat_from_row.view.*
import kotlinx.android.synthetic.main.chat_to_row.view.*

class ChatFromItem(val text: String, val user: User, private val timestamp: String): Item<GroupieViewHolder>
    () {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.textView_chat_to_row.text = text
        viewHolder.itemView.textview_chat_to_row_timeview.text = timestamp

        val uri = user.profileImageUrl
        val targetImageView = viewHolder.itemView.imageview_chat_to_row
        Picasso.get().load(uri).into(targetImageView)

    }

    override fun getLayout(): Int {

        return R.layout.chat_to_row

    }
}

class ChatToItem(val text: String, val user: User, private val timestamp: String) : Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

        viewHolder.itemView.textView_chat_from_row.text = text
        viewHolder.itemView.textview_chat_from_row_timeview.text = timestamp

        val uri = user.profileImageUrl
        val targetImageView = viewHolder.itemView.imageview_chat_from_row
        Picasso.get().load(uri).into(targetImageView)

    }

    override fun getLayout(): Int {

        return R.layout.chat_from_row

    }
}


