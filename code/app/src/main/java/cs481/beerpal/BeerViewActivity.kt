package cs481.beerpal

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.google.api.Distribution
import com.google.firebase.firestore.FirebaseFirestore
import cs481.beerpal.databinding.ActivityBeerViewBinding

class BeerViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBeerViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBeerViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fillViews()

        var bID = intent.getLongExtra(BEER_ID_EXTRA, -1)

        fillReviews(bID)
    }

    private fun fillViews() {
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
        val bWish = intent.getBooleanExtra(BEER_WISH_EXTRA, false)

        //fill UI elems with data
        tvTitle.text = bTitle
        tvABV.text = bABV.toString().plus("%")
        tvBrewery.text = bBrewery
        tvRating.rating = bRating
        tvDesc.text = bDesc
        cbWish.isChecked = bWish
    }

    //dynamically fills and inflates reviewslayout
    //takes a beer id (for db query)
    //takes parent reviewslayout and child reviewlayout
    private fun fillReviews(id: Long) {
        var db = FirebaseFirestore.getInstance()
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


}