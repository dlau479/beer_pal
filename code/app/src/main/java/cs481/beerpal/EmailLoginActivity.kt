package cs481.beerpal

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class EmailLoginActivity : Activity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        setContentView(R.layout.activity_email_login)

        findViewById<Button>(R.id.emailLoginCreateAccount).setOnClickListener {
            val email = findViewById<EditText>(R.id.emailLoginEmailForm).text.toString()
            val password = findViewById<EditText>(R.id.emailLoginPasswordForm).text.toString()
            createAccount(email,password)
            findViewById<EditText>(R.id.emailLoginEmailForm).setText("")
            findViewById<EditText>(R.id.emailLoginPasswordForm).setText("")
        }

        findViewById<Button>(R.id.emailLoginSignIn).setOnClickListener {
            val email = findViewById<EditText>(R.id.emailLoginEmailForm).text.toString()
            val password = findViewById<EditText>(R.id.emailLoginPasswordForm).text.toString()
            signIn(email,password)
            findViewById<EditText>(R.id.emailLoginEmailForm).setText("")
            findViewById<EditText>(R.id.emailLoginPasswordForm).setText("")
        }
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        if(currentUser != null){ //there was a user previously signed in. must explicitly sign out for this to be null
            goToMainMenu()
        }
    }

    private fun createAccount(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = auth.currentUser
                    Toast.makeText(baseContext,"Welcome " + user!!.email,Toast.LENGTH_SHORT).show()
                    //todo: update ui to use user account details e.g. profile pics, maybe pass user to MainMenu?
                    goToMainMenu()
                }
                else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser
                    Toast.makeText(baseContext,"Welcome " + user!!.email,Toast.LENGTH_SHORT).show()
                    //todo: update ui to use user account details e.g. profile pics, maybe pass user to MainMenu?
                    goToMainMenu()
                }
                else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

    //not implemented currently, but could be fun to do.
    private fun sendEmailVerification() {
        val user = auth.currentUser!!
        user.sendEmailVerification()
            .addOnCompleteListener(this) { task ->
                // Email Verification sent
            }
    }

    private fun goToMainMenu(){
        startActivity(Intent(this, MainMenu::class.java))
    }
}