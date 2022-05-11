package cs481.beerpal.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import cs481.beerpal.*
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


    fun getData(){
        BeerRepository.getData(object : BeerRepositoryListener{
            override fun dataListUpdated() {}

            override fun dataListResult(beerList: ArrayList<Beer>) {
                val recyclerView: RecyclerView = requireView().findViewById(R.id.home_recView)
                var adapter = CardAdapter(beerList)
                recyclerView.layoutManager = LinearLayoutManager(context)
                recyclerView.adapter = adapter

                adapter.setOnItemClickListener(object: CardAdapter.OnItemClickListener {
                    override fun onItemClick(position: Int) {
                        val intent = Intent(activity, BeerViewActivity::class.java)
                        intent.putExtra(BEER_TITLE_EXTRA, beerList[position].title)
                        intent.putExtra(BEER_DESC_EXTRA, beerList[position].description)
                        intent.putExtra(BEER_BREWERY_EXTRA, beerList[position].brewery)
                        intent.putExtra(BEER_ABV_EXTRA, beerList[position].abv)
                        intent.putExtra(BEER_RATING_EXTRA, beerList[position].rating)
                        intent.putExtra(BEER_ID_EXTRA, beerList[position].id)
                        intent.putExtra(BEER_URL_EXTRA, beerList[position].url)

                        activity?.startActivity(intent)
                    }
                })
            }
        })
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}