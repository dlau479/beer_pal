package cs481.beerpal.fragments

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import cs481.beerpal.databinding.FragmentProfileBinding
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.fragment_profile.*


class ProfileFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var fStore: FirebaseFirestore
    private var storageReference = FirebaseStorage.getInstance().getReference();

    //private val profileImage: CircleImageView

    private var _binding: FragmentProfileBinding? = null

    var activity: Activity? = getActivity()

    var profile_setting_strings: Array<String> = arrayOf("Find Friends", "My Statistics", "Favorites")



    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profile_settings.adapter = ArrayAdapter(view.getContext(), android.R.layout.simple_list_item_1, profile_setting_strings)

        profile_picture_area.setOnClickListener {
            //Toast.makeText(activity, "Long Text Test", Toast.LENGTH_LONG).show()
            //Toast.makeText(activity, "Short Text Test", Toast.LENGTH_SHORT).show()
            //Toast.makeText(view.getContext(),"Short Text Test",Toast.LENGTH_SHORT).show()
            //val user = auth.currentUser
            //Log.d("TAG", "Current user: " + user.toString())

            // Open gallery
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

        var profileRef = storageReference.child("users/" + auth.currentUser?.uid + "/profile.jpg")
        profileRef.getDownloadUrl().addOnSuccessListener { uri ->
            Picasso.get().load(uri).into(profile_picture_area);
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}