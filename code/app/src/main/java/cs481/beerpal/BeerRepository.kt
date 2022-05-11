package cs481.beerpal

import com.google.firebase.firestore.FirebaseFirestore
import cs481.beerpal.model.BeerItem

object BeerRepository {
    var dataList: ArrayList<Beer> = ArrayList()
    var db: FirebaseFirestore? = null
    var listener: BeerRepositoryListener? = null


    public fun getData(dataListener: BeerRepositoryListener) {
        val db = FirebaseFirestore.getInstance()
        val dataList: ArrayList<Beer> = ArrayList()

        db.collection("beers")
            .get()
            .addOnCompleteListener {

                if (it.isSuccessful) {
                    for (document in it.result!!) {

                        val beer = Beer(
                            title = document.data.getValue("name").toString(),
                            description = document.data.getValue("description").toString(),
                            brewery = document.data.getValue("brewery").toString(),
                            abv = document.data.getValue("abv").toString().toDouble(),
                            rating = document.data.getValue("avg_rating").toString().toFloat(),
                            id = document.data.getValue("id") as Long,
                            url = document.data.getValue("url").toString()
                            //isOnWishList = document.data.getValue("isOnWishList")?.toString()?.toBoolean() ?: false
                        )
                        dataList.add(beer)
                    }
                    dataListener.dataListResult(dataList)

                }
            }
    }

///////////////////////////////////////////////////////////////////////////////////

        private val beerList: ArrayList<BeerItem> = ArrayList()


        fun beerListMock(): ArrayList<BeerItem> {
            if (beerList.isEmpty()) {
                beerList.add(BeerItem("Coreslight", "Coors"))
                beerList.add(BeerItem("Budlight", "Budweiser"))
                beerList.add(BeerItem("Corona Extra", "Corona"))
            }

            return beerList
        }
    }
