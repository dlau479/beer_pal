package cs481.beerpal.fragments

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import cs481.beerpal.databinding.FragmentProfileBinding
import kotlinx.android.synthetic.main.fragment_profile.*


class ProfileFragment : Fragment() {

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
            Toast.makeText(context,"Logging you out...",Toast.LENGTH_SHORT).show()
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}