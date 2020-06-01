package com.example.gyproc.views

import com.example.gyproc.R
import com.example.gyproc.models.User
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.blager_data_view.view.*

class BLagerItems(val text : String, val user : User, private val team : String, val product :
String, val amount : String, val dateToFirebase : String) : Item<GroupieViewHolder>()  {


        //
        override fun bind(viewHolder: GroupieViewHolder, position: Int) {
            viewHolder.itemView.textView_blager_username.text = user.username

            viewHolder.itemView.textView_blager_text.text = text

            viewHolder.itemView.textView_blager_team.text = team

            viewHolder.itemView.textView_blager_time.text = dateToFirebase

            viewHolder.itemView.textView_blager_amount.text = amount


            viewHolder.itemView.textView_blager_product.text = product

            val uri = user.profileImageUrl
            val targetImageView = viewHolder.itemView.imageview_blager_user
            Picasso.get().load(uri).into(targetImageView)
}
    override fun getLayout(): Int {

        return R.layout.blager_data_view

    }
}