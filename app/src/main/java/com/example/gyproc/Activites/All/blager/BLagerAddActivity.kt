package com.example.gyproc.Activites.All.blager

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.gyproc.Activites.All.logbook.LogbookAddActivity
import com.example.gyproc.R
import com.example.gyproc.models.BLager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_b_lager_add.*
import java.text.SimpleDateFormat
import java.util.*

class BLagerAddActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_b_lager_add)
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN


        b_lager_product_chooser.setOnClickListener {
            showDialog()
        }

        b_lager_team_chooser.setOnClickListener {
            showDialogTeam()
        }

        b_lager_add_save_button.setOnClickListener {
            addToBLager()
            finish()
        }

    }
    private fun showDialog() {
        // Method to show an alert dialog with multiple choice list items

        // Late initialize an alert dialog object
        lateinit var dialog: AlertDialog

        // Initialize an array of colors
        val arrayShifts = arrayOf(
            "GNE", "GN",
            "GKBE", "GKB",
            "GFE", "GF",
            "GHOE", "GHO",
            "GXUE", "GXU",
            "GRE", "GR",
            "GG", "GEE",
            "GUE", "GU",
            "GSE", "GFUE")

        // Initialize a new instance of alert dialog builder object
        val builder = AlertDialog.Builder(this)

        // Set a title for alert dialog
        builder.setTitle("Välj produkt")


        builder.setSingleChoiceItems(arrayShifts,-1) { _, which ->
            // Get the dialog selected item
            val shift = arrayShifts[which]
            toast("$shift produkt vald")
            b_lager_product_chooser.text = shift

            dialog.dismiss()
        }

        dialog = builder.create()

        dialog.show()
    }
    fun Context.toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }


    private fun showDialogTeam() {

        // Late initialize an alert dialog object
        lateinit var dialog: AlertDialog

        // Initialize an array of colors
        val arrayTeams = arrayOf("A", "B", "C", "D", "E")

        // Initialize a new instance of alert dialog builder object
        val builder = AlertDialog.Builder(this)

        // Set a title for alert dialog
        builder.setTitle("Välj skiftlag")


        builder.setSingleChoiceItems(arrayTeams,-1) { _, which ->
            // Get the dialog selected item
            val team = arrayTeams[which]
            toast("$team skiftlag valt")
            b_lager_team_chooser.text = team

            dialog.dismiss()
        }

        dialog = builder.create()

        dialog.show()
    }


    fun addToBLager() {

        val fromId = FirebaseAuth.getInstance().uid ?: return
        val text = b_lager_add_textinput.text.toString()
        val amount = b_lager_enter_amount.text.toString()
        val team = b_lager_team_chooser.text.toString()
        val product = b_lager_product_chooser.text.toString()



        val calendar = Calendar.getInstance()
        val date : Date = calendar.time

        val dateFormat = SimpleDateFormat("E dd-MMM")
        val dateToFirebase = dateFormat.format(date)



        Log.d(LogbookAddActivity.TAG,"$date sparad")
        Log.d(LogbookAddActivity.TAG,"$amount sparad")
        Log.d(LogbookAddActivity.TAG,"$text sparad")
        Log.d(LogbookAddActivity.TAG,"$team sparad")


        val reference = FirebaseDatabase.getInstance()
            .getReference("/b_lager-entries").push()

        val bLagerEntry = BLager(reference.key!!, text, fromId,team  ,dateToFirebase, System
            .currentTimeMillis() ,
            amount, product)

        reference.setValue(bLagerEntry)
            .addOnSuccessListener {
                Log.d(LogbookAddActivity.TAG, "Sparade logboksinlägg ${reference.key}")
                Log.d(LogbookAddActivity.TAG,"shift = $amount, text = $text, fromId = $fromId, " +
                        "team = $team")
                b_lager_add_textinput.text.clear()
            }

    }

}
