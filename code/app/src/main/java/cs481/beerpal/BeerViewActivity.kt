package cs481.beerpal

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import cs481.beerpal.databinding.ActivityBeerViewBinding


class BeerViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBeerViewBinding
    var db = FirebaseFirestore.getInstance()
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBeerViewBinding.inflate(layoutInflater)
        setContentView(binding.root)


        fillViews()

        var bID = intent.getLongExtra(BEER_ID_EXTRA, -1)

        fillReviews(bID)

        val fab_back = findViewById<FloatingActionButton>(R.id.fab_back)
        fab_back.setOnClickListener {
            val intent = Intent(this, MainMenu::class.java)
            startActivity(intent)
        }


    }

    private fun fillViews() {
        auth = Firebase.auth

        //create references to UI elems
        var tvThumbnail = findViewById<ImageView>(R.id.beer_thumbnail)
        var tvTitle = findViewById<TextView>(R.id.beer_title)
        var tvABV = findViewById<TextView>(R.id.beer_abv)
        var tvBrewery = findViewById<TextView>(R.id.beer_brewery)
        var tvRating = findViewById<RatingBar>(R.id.beer_rating)
        var tvDesc = findViewById<TextView>(R.id.beer_description)
        var cbWish = findViewById<CheckBox>(R.id.beer_wishlist)

        //get beer object data from intent puts
        val bTitle = intent.getStringExtra(BEER_TITLE_EXTRA)
        val bDesc = intent.getStringExtra(BEER_DESC_EXTRA)
        val bRating = intent.getFloatExtra(BEER_RATING_EXTRA, 0.0F)
        val bBrewery = intent.getStringExtra(BEER_BREWERY_EXTRA)
        val bABV = intent.getDoubleExtra(BEER_ABV_EXTRA, 0.0)
        val bURL = intent.getStringExtra(BEER_URL_EXTRA)
        val bID = intent.getLongExtra(BEER_ID_EXTRA, -1)

        //fill UI elems with data
        tvTitle.text = bTitle
        tvABV.text = bABV.toString().plus("%")
        tvBrewery.text = bBrewery
        tvRating.rating = bRating
        tvDesc.text = bDesc
        getImageFromUrl(tvThumbnail).execute(bURL)

        //for wishlist, make separate query
        db.collection("wishlist")
            .get()
            .addOnCompleteListener {
                if(it.isSuccessful) {
                    for (document in it.result!!) {
                        if ((document.data.getValue("beer_id") as Long == bID) &&
                            (auth.currentUser?.email == document.data.getValue("user_email"))) {
                            cbWish.isChecked = true
                        }
                    }
                }
            }
    }

    //dynamically fills and inflates reviewslayout
    //takes a beer id (for db query)
    private fun fillReviews(id: Long) {
        var reviewList: ArrayList<Review> = ArrayList()

        //the linear layout that builds all of the reviews
        var reviewsLayout = findViewById<LinearLayout>(R.id.beer_reviews_llayout)
        reviewsLayout.layoutParams.height = WRAP_CONTENT
        reviewsLayout.layoutParams.width = MATCH_PARENT

        reviewsLayout.removeAllViews()

        db.collection("ratings_reviews")
            .get()
            .addOnCompleteListener {
                if(it.isSuccessful) {
                    for (document in it.result!!) {
                        val review = Review(
                            beer_id = document.data.getValue("beer_id") as Long,
                            rating = document.data.getValue("rating").toString().toFloat(),
                            review = document.data.getValue("review").toString(),
                            userName = document.data.getValue("user_email").toString()
                        )
                        reviewList.add(review)
                    }
                    for (review in reviewList) {
                        if (review != null && review.beer_id == id) {
                            val view = layoutInflater.inflate(R.layout.review_cell, null)

                            var rating = view.findViewById<RatingBar>(R.id.beer_reviews_rating)
                            rating.rating = review.rating

                            var uname = view.findViewById<TextView>(R.id.beer_reviews_uname)
                            uname.text = review.userName

                            var description = view.findViewById<TextView>(R.id.beer_reviews_description)
                            description.text = review.review

                            reviewsLayout.addView(view)
                        }
                    }
                }
            }
    }
    private inner class getImageFromUrl(var imageView: ImageView) : AsyncTask<String, Void, Bitmap?>(){
        override fun doInBackground(vararg url: String?): Bitmap? {
            val imageUrl = url[0]
            var image: Bitmap? = null
            try{
                image = BitmapFactory.decodeStream(java.net.URL(imageUrl).openStream())
            }
            catch(e:Exception){
                Log.d("Error downloading image: ", e.message.toString())
                e.printStackTrace()
            }
            return image
        }

        override fun onPostExecute(result: Bitmap?) {
            imageView.setImageBitmap(result)
        }
    }


}