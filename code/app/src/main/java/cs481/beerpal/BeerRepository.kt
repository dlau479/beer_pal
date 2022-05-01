package cs481.beerpal

import com.google.firebase.firestore.FirebaseFirestore

object BeerRepository {
    var dataList : ArrayList<Beer> = ArrayList()
    var db : FirebaseFirestore? = null
    var listener: BeerRepositoryListener? = null

}