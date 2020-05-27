package com.example.gyproc.views

import com.example.gyproc.R
import com.example.gyproc.models.User
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item

import kotlinx.android.synthetic.main.logbook_data_view.view.*
import java.util.*

class LogBookItems(val text: String, val user: User, private val team : String, private val shiftView :
String, val
dateToFirebase : String) :
    Item<GroupieViewHolder>() {
//
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.textView_logbook_username.text = user.username

        viewHolder.itemView.textView_logbook_text.text = text

    viewHolder.itemView.textView_logbook_team.text = team

        viewHolder.itemView.textView_logbook_time.text = dateToFirebase


        viewHolder.itemView.textView_logbook_shift.text = shiftView

        val uri = user.profileImageUrl
        val targetImageView = viewHolder.itemView.imageview_logbook_user
        Picasso.get().load(uri).into(targetImageView)


    }

    override fun getLayout(): Int {

        return R.layout.logbook_data_view

    }
}