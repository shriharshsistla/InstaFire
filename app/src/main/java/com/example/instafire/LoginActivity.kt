package com.example.instafire

import android.content.ContentValues.TAG
import android.content.Intent
import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*


class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val auth = FirebaseAuth.getInstance()
        if(auth.currentUser!=null){
            goPostsActivity()
        }


        loginButton.setOnClickListener {
            loginButton.isEnabled = false
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()
            if(email.isBlank()||password.isBlank()){
                Toast.makeText(this,"Email and password cant be empty",Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.signInWithEmailAndPassword(email,password).addOnCompleteListener { task ->

                loginButton.isEnabled = true
                if(task.isSuccessful){
                    Toast.makeText(this,"Success",Toast.LENGTH_SHORT).show()
                    goPostsActivity()

                }else{
                    Log.i(TAG,"signInWithEmail Failed",task.exception)

                    Toast.makeText(this,"Authentication Failed",Toast.LENGTH_SHORT).show()
                }
            }


        }



    }

    private fun goPostsActivity() {
        Log.i(TAG,"goPostsActivity")
        val intent = Intent(this,PostsActivity::class.java)
        startActivity(intent)
        finish()

    }
}