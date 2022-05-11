package cs481.beerpal

import android.content.ContentValues
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.android.synthetic.main.fragment_profile.*

class EditProfile : AppCompatActivity(){

    private lateinit var auth: FirebaseAuth
    private var savedUsername = ""
    private var savedBio = ""
    private var savedStyleChoice = ""
    private var savedUserEmail = ""
    private var savedFriendsList = ""
    private var savedDocumentId = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        // Get user info and populate the fields
        auth = Firebase.auth
        populateProfile()

        saveChangesButton.setOnClickListener {

            // Are you sure you would like to do this pop up

            // If yes, start loading spinner

            // pass inputs to query function
            // onSuccess, stop the spinner and end the popup's life.
            saveProfileChanges();
            finish();
        }
        //saveChangesButton.disable();

        cancelButton.setOnClickListener {
            finish();
        }
    }

    private fun saveProfileChanges() {
        val dbRef = db().collection("user_info")
        val selectedEntry: MutableMap<String, Any> = HashMap()
        selectedEntry["bio"] = bioInput.text;
        selectedEntry["style_choice"] = styleChoiceInput.text
        selectedEntry["username"] = usernameInput.text
        val bio = bioInput.text
        val style = styleChoiceInput.text
        val username = usernameInput.text

        dbRef.document(savedDocumentId).update("bio", bio.toString(),
            "style_choice", style.toString(),
            "username", username.toString()).addOnSuccessListener {
//            profile_username.text = username;
//            profile_bio.text = bio;
//            profile_style_choice.text = style;
        }
    }

    private fun populateProfile() {
        val userEmail = auth.currentUser?.email
        val dbRef = db().collection("user_info")
        val query = dbRef.whereEqualTo("user_email", userEmail)
        query
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val data = document.data
                    var bio = ""
                    var friends_list = ArrayList<String>();
                    var friends_list_count = 0
                    var style_choice = "";
                    var user_email = "";
                    var username = ""
                    savedDocumentId = document.id
                    data.forEach { entry ->
                        if (entry.key == "bio") {
                            bio = entry.value as String
                            Log.d(ContentValues.TAG, "bio: " + entry.value)
                        } else if (entry.key == "friends_list") {
                            friends_list = entry.value as ArrayList<String>
                            friends_list_count = friends_list.size
                            Log.d(ContentValues.TAG, "friends_list: " + friends_list_count.toString())
                        } else if (entry.key == "style_choice") {
                            style_choice = entry.value as String
                            Log.d(ContentValues.TAG, "style_choice: " + entry.value)
                        } else if (entry.key == "username") {
                            username = entry.value as String
                            Log.d(ContentValues.TAG, "username: " + entry.value)
                        } else if (entry.key == "user_email") {
                            user_email = entry.value as String
                            Log.d(ContentValues.TAG, "user_email: " + entry.value)
                        }
                        else {
                            throw(NoSuchFieldException())
                        }
                    }
                    usernameInput.setText(username);
                    bioInput.setText(bio);
                    styleChoiceInput.setText(style_choice);
                    savedUsername = username
                    savedBio = bio
                    savedStyleChoice = style_choice
                    //profile_friends.setText(friends_list_count.toString());
                }
            }
    }

    private fun db(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    private fun Button.disable() {
        setBackgroundColor(Color.GRAY)
        setClickable(false)
    }

    private fun Button.enable() {
        setBackgroundColor(Color.parseColor("#FF6200EE"));
        setClickable(true)
    }

}