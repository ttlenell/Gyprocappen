package com.example.gyproc

import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        val email = email_edittext_register?.text.toString()
        val password = password_edittext_register?.text.toString()

        Log.d("Login", "Attempt login with email/password: $email /$password")

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password)
            .addOnCompleteListener{
        if (!it.isSuccessful) return@addOnCompleteListener
                Log.d("Main","Sucessfully logged user in: ${it.result?.user?.uid}")

            }
            .addOnFailureListener{
                Log.d("Main","Failed to log in user: ${it.message}")
                Toast.makeText(this,"User could not be logged in: ${it.message}", Toast.LENGTH_SHORT).show()

            }

    }
}