package com.example.gyproc.blager

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.gyproc.R
import kotlinx.android.synthetic.main.activity_b_lager_add.*

class BLagerAddActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_b_lager_add)

        b_lager_product_chooser.setOnClickListener {
            showDialog()
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
            "GHOE X", "GEE",
            "GRE", "GR",
            "GG",
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

}
