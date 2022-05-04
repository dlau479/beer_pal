package cs481.beerpal.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import cs481.beerpal.Beer
import cs481.beerpal.CardAdapter
import cs481.beerpal.R
import cs481.beerpal.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getData()
    }
    /*
    getData() -> void
    Creates an instance of the Firebase DB,
    Gets data in "Beers" collection,
    Formats data to beer obj,
    Injects data into card,
    Fills cards into HomeFragment's RecyclerView
     */
    private fun getData() {
        val db = FirebaseFirestore.getInstance()

        var beers: MutableList<Beer> = mutableListOf<Beer>()

        val documents = db.collection("beers")
            .get()
            .addOnCompleteListener {

                if(it.isSuccessful) {
                    for (document in it.result!!) {

                        val beer = Beer(
                            title = document.data.getValue("name").toString(),
                            description = document.data.getValue("description").toString(),
                            brewery = document.data.getValue("brewery").toString(),
                            abv = document.data.getValue("abv").toString().toDouble(),
                            rating = document.data.getValue("avg_rating").toString().toFloat(),
                            id = document.data.getValue("id") as Long
                        )
                        beers.add(beer)
                    }
                    val recyclerView: RecyclerView = requireView().findViewById(R.id.home_recView)
                    recyclerView.layoutManager = LinearLayoutManager(context)
                    recyclerView.adapter = CardAdapter(beers)
                }
            }

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}