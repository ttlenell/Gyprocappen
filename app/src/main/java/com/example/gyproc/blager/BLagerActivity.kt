package com.example.gyproc.blager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.gyproc.R
import kotlinx.android.synthetic.main.activity_logbook.*

class BLagerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logbook)


        fab_add_post.setOnClickListener {

            val intent = Intent(
                this,
                BLagerAddActivity::class.java)
            startActivity(intent)
        }
    }
}
