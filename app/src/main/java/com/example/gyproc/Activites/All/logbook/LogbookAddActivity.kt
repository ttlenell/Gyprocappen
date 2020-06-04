package com.example.gyproc.Activites.All.logbook

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.gyproc.R
import com.example.gyproc.models.LogBook
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_logbook_add.*
import java.text.SimpleDateFormat
import java.util.*

class LogbookAddActivity : AppCompatActivity() {

    companion object {
        val TAG = "LogbookAdd"

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logbook_add)


        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

        supportActionBar?.title = "Ny logg"

        logbook_shift_chooser.setOnClickListener {
            showDialogShift()
        }

        logbook_team_chooser.setOnClickListener {
            showDialogTeam()
        }

        logbook_add_save_button.setOnClickListener {
            addToLogbook()

            finish()
        }


    }

    // visar alertdialog där man väljer vilket arbetspass man jobbat på

    private fun showDialogShift() {

            // Late initialize an alert dialog object
            lateinit var dialog: AlertDialog

            // Initialize an array of colors
            val arrayShifts = arrayOf("06-14", "14-22", "22-06", "06-18", "18-06")

            // Initialize a new instance of alert dialog builder object
            val builder = AlertDialog.Builder(this)

            // Set a title for alert dialog
            builder.setTitle("Välj skiftpass")


        builder.setSingleChoiceItems(arrayShifts,-1) { _, which ->
            // Get the dialog selected item
            val shift = arrayShifts[which]
            toast("$shift pass valt")
                    logbook_shift_chooser.text = shift

            dialog.dismiss()
        }

            dialog = builder.create()

            dialog.show()
        }
    fun Context.toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    // alertdialog där man väljer vilket skiftlag man jobbat på

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
            logbook_team_chooser.text = team

            dialog.dismiss()
        }

        dialog = builder.create()

        dialog.show()
    }


    // skickar till firebase

    fun addToLogbook() {

        val fromId = FirebaseAuth.getInstance().uid ?: return
        val text = logbook_add_edittext.text.toString()
        val shift = logbook_shift_chooser.text.toString()
        val team = logbook_team_chooser.text.toString()



        val calendar = Calendar.getInstance()
        val date : Date = calendar.time

        val dateFormat = SimpleDateFormat("E dd-MMM")
        val dateToFirebase = dateFormat.format(date)



        Log.d(TAG,"$date sparad")
        Log.d(TAG,"$shift sparad")
        Log.d(TAG,"$text sparad")
        Log.d(TAG,"$team sparad")


        val reference = FirebaseDatabase.getInstance()
            .getReference("/logbook-entries").push()

        val logBookEntry = LogBook(reference.key!!, text, fromId, shift,team, dateToFirebase,
            System.currentTimeMillis() * 1000)

        reference.setValue(logBookEntry)
            .addOnSuccessListener {
                Log.d(TAG, "Sparade logboksinlägg ${reference.key}")
                Log.d(TAG,"shift = $shift, text = $text, fromId = $fromId, team = $team")
                logbook_add_edittext.text.clear()
            }

    }
    }

