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
import cs481.beerpal.*
import cs481.beerpal.databinding.FragmentWhatihavetriedBinding
import cs481.beerpal.model.RatingReview

class WhatIHaveTriedFragment : Fragment() {


    private var _binding: FragmentWhatihavetriedBinding? = null


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    val wishList = mutableListOf<RatingReview>()
    val filteredBeerList: ArrayList<Beer> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWhatihavetriedBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView: RecyclerView = requireView().findViewById(R.id.tried_recView)
        val adapter = CardAdapter(filteredBeerList)
        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter.setOnItemClickListener(object : CardAdapter.OnItemClickListener{
            override fun onItemClick(position: Int) {}//do nothing until assigned
        })
        recyclerView.adapter = adapter
        getData()
    }

    fun getWishList() {
        val db = FirebaseFirestore.getInstance()


        db.collection("ratings_reviews")
            .get()
            .addOnCompleteListener {

                if (it.isSuccessful) {
                    for (document in it.result!!) {
                        var userobj = RatingReview(
                            beer_id = document.data.getValue("beer_id").toString().toInt(),
                            rating = document.data.getValue("rating").toString().toDouble(),
                            review = document.data.getValue("review").toString(),
                            user_email = document.data.getValue("user_email").toString()
                        )
                        wishList.add(userobj)
                    }

                }

                // Log.d("Verbose","This is the size "+ this.wishList.size.toString())

            }




    }

    fun getData(){
        getWishList()

        BeerRepository.getData(object : BeerRepositoryListener {
            override fun dataListUpdated() {}

            override fun dataListResult(beerList: ArrayList<Beer>) {
                getWishList()

                beerList.forEach{ beer ->
                    wishList.forEach{ id ->
                        if (beer.id == id.beer_id.toLong() ){
                            filteredBeerList.add(beer)
                       }
                        //Log.d("Verbose","This is the "+id)
                        //Log.d("Verbose",id.toString())
                    }
                }
                Log.d("Verbose","This is the size "+wishList.size.toString())

                val recyclerView: RecyclerView = requireView().findViewById(R.id.tried_recView)
                val adapter = CardAdapter(filteredBeerList)
                recyclerView.layoutManager = LinearLayoutManager(context)
                adapter.setOnItemClickListener(object : CardAdapter.OnItemClickListener{
                    override fun onItemClick(position: Int) {}//do nothing until assigned
                })
                recyclerView.adapter = adapter
            }

        })

     //   wishList.forEach{ id ->
            // if (beer == id ){
            //   filteredBeerList.add(beer)
            // }

     //   }
        //Log.d("Verbose","This is the size "+wishList.size.toString())

    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}