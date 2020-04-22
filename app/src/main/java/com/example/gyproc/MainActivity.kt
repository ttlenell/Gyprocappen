package com.example.gyproc

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        register_button_register.setOnClickListener {

            performRegister()

        }

        already_have_account_textview.setOnClickListener {

            Log.d("MainActivity","Try to show login activity")

            // launch the login activity
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun performRegister(){


        val email = email_edittext_register.text.toString()
        val password = password_edittext_register.text.toString()

        if (email.isEmpty() || password.isEmpty()){
            Toast.makeText(this,"Fyll i mail och lösenord", Toast.LENGTH_SHORT).show()
            return
        }


        Log.d("MainActivity", "Email is: " + email)
        Log.d("MainActivity","password: $password")

        // firebase auth


        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener

                // else if sucessful
                Log.d("Main","Sucessfully created a user with uid: ${it.result?.user?.uid}")
            }
            .addOnFailureListener{
                Log.d("Main","Failed to create user: ${it.message}")
                Toast.makeText(this,"Gick ej att skapa användare: ${it.message}", Toast.LENGTH_SHORT).show()

            }

    }
}
