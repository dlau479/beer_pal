package cs481.beerpal.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cs481.beerpal.*
import cs481.beerpal.BeerRepository.dataList
import cs481.beerpal.databinding.FragmentHomeBinding
import java.util.*
import kotlin.collections.ArrayList

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

        val recyclerView = view.findViewById<RecyclerView>(R.id.home_recView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = CardAdapter(dataList)

        formatData()
    }


    private fun formatData(){
        BeerRepository.getData(object : BeerRepositoryListener {
            override fun dataListUpdated() {}

            override fun dataListResult(beerList: ArrayList<Beer>) {
                val recyclerView: RecyclerView = requireView().findViewById(R.id.home_recView)
                var adapter = CardAdapter(beerList)
                var filtered = arrayListOf<Beer>()
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
                val searchView = view?.findViewById<SearchView>(R.id.home_searchView)
                if (searchView != null) {
                    searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(p0: String?): Boolean {
                            return false
                        }

                        override fun onQueryTextChange(p0: String?): Boolean {
                            val searchText = p0!!.lowercase(Locale.getDefault())
                            if (searchText.isNotEmpty()) {
                                for (it in beerList) {
                                    if(it.title.lowercase(Locale.getDefault()).contains(searchText)) {
                                        Log.d("HomeFragment", "it.title: ${it.title.lowercase()}" +
                                                "searchtext: ${searchText.lowercase()}")
                                        filtered.add(it)
                                    }
                                }
                            } else {
                                filtered.clear()
                                filtered.addAll(beerList)
                            }
                            adapter.filterList(filtered)
                            return false
                        }
                    })
                }
            }
        })
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}