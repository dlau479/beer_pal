package cs481.beerpal.fragments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cs481.beerpal.*
import cs481.beerpal.databinding.FragmentWishlistBinding
import cs481.beerpal.model.BeerItem
import cs481.beerpal.recyclerview.WishListRVAdapter
import cs481.beerpal.recyclerview.WishListRVListener
import kotlinx.android.synthetic.main.fragment_wishlist.view.*
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


class WishlistFragment : Fragment(),WishListRVListener {

    private var _binding: FragmentWishlistBinding? = null
    private var wishList = ArrayList<Long>()
    val filteredBeerList: ArrayList<Beer> = ArrayList()
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWishlistBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView: RecyclerView = requireView().findViewById(R.id.wishlist_recView)
        val adapter = CardAdapter(filteredBeerList)
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter.setOnItemClickListener(object : CardAdapter.OnItemClickListener{
            override fun onItemClick(position: Int) {}//do nothing until assigned
        })
        recyclerView.adapter = adapter
        getData()
    }

    fun getWishList(){



        if(wishList.isEmpty()){
            wishList.add(3)
            wishList.add(4)
            wishList.add(9)
        }
    }

    fun getData(){
        BeerRepository.getData(object : BeerRepositoryListener {
            override fun dataListUpdated() {}

            override fun dataListResult(beerList: ArrayList<Beer>) {
                getWishList()

                beerList.forEach{ beer ->
                    wishList.forEach{ id ->
                        if (beer.id == id ){
                            filteredBeerList.add(beer)
                        }
                    }
                }
                val recyclerView: RecyclerView = requireView().findViewById(R.id.wishlist_recView)
                val adapter = CardAdapter(filteredBeerList)
                recyclerView.layoutManager = LinearLayoutManager(context)

                adapter.setOnItemClickListener(object: CardAdapter.OnItemClickListener {
                    override fun onItemClick(position: Int) {
                        val selectedId = filteredBeerList[position].id
                        var beerListPos = -1
                        beerList.forEachIndexed { index, beer ->
                            if(beer.id == selectedId){
                                beerListPos = index
                            }
                        }
                        val intent = Intent(activity, BeerViewActivity::class.java)
                        intent.putExtra(BEER_TITLE_EXTRA, beerList[beerListPos].title)
                        intent.putExtra(BEER_DESC_EXTRA, beerList[beerListPos].description)
                        intent.putExtra(BEER_BREWERY_EXTRA, beerList[beerListPos].brewery)
                        intent.putExtra(BEER_ABV_EXTRA, beerList[beerListPos].abv)
                        intent.putExtra(BEER_RATING_EXTRA, beerList[beerListPos].rating)
                        intent.putExtra(BEER_ID_EXTRA, beerList[beerListPos].id)
                        intent.putExtra(BEER_URL_EXTRA, beerList[beerListPos].url)

                        activity?.startActivity(intent)
                    }
                })
                recyclerView.adapter = adapter
            }

        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun itemSelected(selectedItem: BeerItem) {
        //TODO("Not yet implemented")
    }
}