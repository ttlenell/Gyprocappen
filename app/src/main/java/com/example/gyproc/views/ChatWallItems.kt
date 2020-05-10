package com.example.gyproc.views

import com.example.gyproc.R
import com.example.gyproc.models.User
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.chat_to_row.view.textview_logbook_username
import kotlinx.android.synthetic.main.chatwall_from_others_row.view.*
import kotlinx.android.synthetic.main.chatwall_from_row.view.*
import java.text.SimpleDateFormat

class ChatWallFrom(val text: String, val user: User): Item<GroupieViewHolder>() {

    val dateFormat = SimpleDateFormat
        .getDateTimeInstance(SimpleDateFormat.SHORT, SimpleDateFormat.SHORT)

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.textview_logbook_username.text = text
        viewHolder.itemView.username_textview_chatwall_from_row.text = user.username
//        viewHolder.itemView.timeView_chatwall_from_row.text = dateFormat.format(time)

        val uri = user.profileImageUrl
        val targetImageView = viewHolder.itemView.imageview_chatwall_from_row
        Picasso.get().load(uri).into(targetImageView)

    }

    override fun getLayout(): Int {

        return R.layout.chatwall_from_row

    }
}

class ChatWallFromOthers(val text: String, val user: User) : Item<GroupieViewHolder>() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

        val dateFormat = SimpleDateFormat
            .getDateTimeInstance(SimpleDateFormat.SHORT, SimpleDateFormat.SHORT)

//        viewHolder.itemView.timeView_chatwall_from_others_row.text = dateFormat.format(time)

        viewHolder.itemView.textview_logbook_username.text = text

        viewHolder.itemView.username_textview_chatwall_from_others_row.text = user.username

        val uri = user.profileImageUrl
        val targetImageView = viewHolder.itemView.imageview_chatwall_from_others_row
        Picasso.get().load(uri).into(targetImageView)

    }

    override fun getLayout(): Int {

        return R.layout.chatwall_from_others_row

    }
}