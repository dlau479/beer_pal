package cs481.beerpal.fragments

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import cs481.beerpal.EditProfile
import cs481.beerpal.databinding.FragmentProfileBinding
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.fragment_profile.view.*


class ProfileFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private var storageReference = FirebaseStorage.getInstance().getReference();

    private var _binding: FragmentProfileBinding? = null

    var profile_setting_strings: Array<String> = arrayOf("Find Friends", "My Statistics", "Favorites", "Edit Profile")


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set spinner
        pBar.visibility = View.VISIBLE

        auth = Firebase.auth
        var profileRef = storageReference.child("users/" + auth.currentUser?.uid + "/profile.jpg")
        profileRef.getDownloadUrl().addOnSuccessListener { uri ->
            Picasso.get().load(uri).into(profile_picture_area);
            pBar.visibility = View.GONE
        }

        //Get user info and display
        populateProfile();

        profile_settings.adapter = ArrayAdapter(view.getContext(), android.R.layout.simple_list_item_1, profile_setting_strings)

        profile_settings.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id ->
                val selectedItemText = parent.getItemAtPosition(position)
                if(selectedItemText == profile_setting_strings[0]) { // Find Friends

                } else if (selectedItemText == profile_setting_strings[1]) { // My Statistics

                } else if (selectedItemText == profile_setting_strings[2]) { // Favorites

                } else if (selectedItemText == profile_setting_strings[3]) { // Edit Profile
                    startActivity(Intent(context, EditProfile::class.java))
                }
            }

        profile_picture_area.setOnClickListener {
            val openGalleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(openGalleryIntent, 1000);

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 1000) {
            if(resultCode == Activity.RESULT_OK) {
                var imageUri = data?.getData();
                profile_picture_area.setImageURI(imageUri)

                if (imageUri != null) {
                    uploadImageToFirebase(imageUri)
                };

            }
        }
    }

    private fun uploadImageToFirebase(imageUri: Uri) {
        // Upload image to firebase storage
        var fileRef = storageReference.child("users/" + auth.currentUser?.uid + "/profile.jpg")
        fileRef.putFile(imageUri).addOnSuccessListener {
            fileRef.getDownloadUrl().addOnSuccessListener { uri ->
                Picasso.get().load(uri).into(profile_picture_area)
                Toast.makeText(context,"Image Uploaded",Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            Toast.makeText(context,"Image Failed to Upload",Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root



        return root
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
                    data.forEach { entry ->
                        if (entry.key == "bio") {
                            bio = entry.value as String
                            Log.d(TAG, "bio: " + entry.value)
                        } else if (entry.key == "friends_list") {
                            friends_list = entry.value as ArrayList<String>
                            friends_list_count = friends_list.size
                            Log.d(TAG, "friends_list: " + friends_list_count.toString())
                        } else if (entry.key == "style_choice") {
                            style_choice = entry.value as String
                            Log.d(TAG, "style_choice: " + entry.value)
                        } else if (entry.key == "username") {
                            username = entry.value as String
                            Log.d(TAG, "username: " + entry.value)
                        } else if (entry.key == "user_email") {
                            user_email = entry.value as String
                            Log.d(TAG, "user_email: " + entry.value)
                        }
                        else {
                            throw(NoSuchFieldException())
                        }
                    }
                    profile_username.text = username;
                    profile_bio.text = bio;
                    profile_style_choice.text = style_choice;
                    profile_friends.text = friends_list_count.toString();
                }
            }

        val dbRefBeers = db().collection("ratings_reviews")
        val beerQuery = dbRefBeers.whereEqualTo("user_email", userEmail)
        var beersTried = 0;
        beerQuery
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    beersTried++;
                }
                profile_beers_tasted.text = beersTried.toString();
            }
    }

    private fun db(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}