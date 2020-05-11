package com.example.gyproc.logbook

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.gyproc.R
import com.example.gyproc.models.LogBook
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_logbook_add.*
import java.text.SimpleDateFormat
import java.util.*

class LogbookAddActivity : AppCompatActivity() {

    companion object {
        val TAG = "LogbookAdd"
        val USER_KEY = "USER_KEY"

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logbook_add)

        supportActionBar?.title = "Ny logg"

        logbook_shift_chooser.setOnClickListener {
            showDialog()
        }

        logbook_add_save_button.setOnClickListener {
            addToLogbook()
//            val intent = Intent(this,LogbookActivity::class.java)
////            intent.putExtra(USER_KEY, POSITION_KEY)
////            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
//            startActivity(intent)
            finish()
        }


    }

    private fun showDialog() {
        // Method to show an alert dialog with multiple choice list items

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


    fun addToLogbook() {

        val fromId = FirebaseAuth.getInstance().uid ?: return
        val text = logbook_add_edittext.text.toString()
        val shift = logbook_shift_chooser.text.toString()


        val calendar = Calendar.getInstance()
        val date : Date = calendar.time

        val dateFormat = SimpleDateFormat("E dd-MMM")
        val dateToFirebase = dateFormat.format(date)



        Log.d(TAG,"$date sparad")
        Log.d(TAG,"$shift sparad")
        Log.d(TAG,"$text sparad")


        val reference = FirebaseDatabase.getInstance()
            .getReference("/logbook-entries").push()

        val logBookEntry = LogBook(reference.key!!, text, fromId, shift, dateToFirebase)

        reference.setValue(logBookEntry)
            .addOnSuccessListener {
                Log.d(TAG, "Sparade logboksinlägg ${reference.key}")
                Log.d(TAG,"shift = $shift, text = $text, fromId = $fromId")
                logbook_add_edittext.text.clear()
            }

    }
    }

