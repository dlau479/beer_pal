package cs481.beerpal

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() { //note that MainActivity handles logins.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.emailLoginButton).setOnClickListener {
            startActivity(Intent(this, EmailLoginActivity::class.java))
        }

        findViewById<Button>(R.id.googleLoginButton).setOnClickListener {
            Toast.makeText(this, "Not yet implemented!",Toast.LENGTH_SHORT).show()
        }
    }
}