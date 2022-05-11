package cs481.beerpal

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.*
import cs481.beerpal.databinding.ActivityBeerViewBinding
import android.widget.CheckBox
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import com.google.api.Distribution
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.properties.Delegates

class BeerViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBeerViewBinding
    private lateinit var reviewList: ArrayList<Review>
    private lateinit var reviewsLayout: LinearLayout
    private lateinit var reviewLayout: ConstraintLayout
    var db: FirebaseFirestore? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding  = ActivityBeerViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //create references to UI elems
        var tvThumbnail = findViewById<ImageView>(R.id.beer_thumbnail)
        var tvTitle = findViewById<TextView>(R.id.beer_title)
        var tvABV = findViewById<TextView>(R.id.beer_abv)
        var tvBrewery = findViewById<TextView>(R.id.beer_brewery)
        var tvRating = findViewById<RatingBar>(R.id.beer_rating)
        var tvDesc = findViewById<TextView>(R.id.beer_description)
        var cbWish = findViewById<CheckBox>(R.id.beer_wishlist)

        //the linear layout that builds all of the reviews
        var reviewsLayout = findViewById<LinearLayout>(R.id.beer_reviews_llayout)
        reviewsLayout.layoutParams.height = WRAP_CONTENT
        reviewsLayout.layoutParams.width = MATCH_PARENT

        //the constraint layout for a single review
        var reviewLayout = findViewById<ConstraintLayout>(R.id.beer_reviews_clayout)


        //get beer object data from intent puts
        val bTitle = intent.getStringExtra(BEER_TITLE_EXTRA)
        val bDesc = intent.getStringExtra(BEER_DESC_EXTRA)
        val bRating = intent.getFloatExtra(BEER_RATING_EXTRA, 0.0F)
        val bBrewery = intent.getStringExtra(BEER_BREWERY_EXTRA)
        val bABV = intent.getDoubleExtra(BEER_ABV_EXTRA, 0.0)
        var bID = intent.getLongExtra(BEER_ID_EXTRA, -1)
        val bURL = intent.getStringExtra(BEER_URL_EXTRA)
        val bWish = intent.getBooleanExtra(BEER_WISH_EXTRA, false)

        //fill UI elems with data
        tvTitle.text = bTitle
        tvABV.text = bABV.toString().plus("%")
        tvBrewery.text = bBrewery
        tvRating.rating = bRating
        tvDesc.text = bDesc
        cbWish.isChecked = bWish

        //make db review query
        getReviews(bID, reviewsLayout, reviewLayout)
    }

    private fun getReviews(id: Long, layout: LinearLayout, clayout: ConstraintLayout) {
        var db = FirebaseFirestore.getInstance()
        var reviewList: ArrayList<Review> = ArrayList()
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
                            layout.removeView(clayout)
                            layout.addView(newReviewCell(review, clayout))
                        }
                    }
                }
            }
    }

    //Builds a constraint layout from an empty constraint layout  and a review object
    private fun newReviewCell(review: Review, cell: ConstraintLayout) : ConstraintLayout {
        val ID1: Int = 1
        val ID2: Int = 2
        val ID3: Int = 3
        val ID4: Int = 4

        val constraintSet = ConstraintSet()

        cell.id = ID1
        cell.layoutParams.height = WRAP_CONTENT
        cell.layoutParams.width = MATCH_PARENT

        var rating = RatingBar(applicationContext)
        cell.addView(rating)
        rating.id = ID2
        rating.isClickable = false
        rating.layoutParams.width = 184
        rating.layoutParams.height = 34
        rating.setIsIndicator(true)
        rating.numStars = 5


        var uname = TextView(applicationContext)
        cell.addView(uname)
        uname.id = ID3
        uname.layoutParams.width = MATCH_PARENT
        uname.layoutParams.height = 35


        var description = TextView(applicationContext)
        cell.addView(description)
        description.id = ID4
        description.layoutParams.width = MATCH_PARENT
        description.layoutParams.height = WRAP_CONTENT


        constraintSet.connect(ID3, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP)
        constraintSet.connect(ID4, ConstraintSet.TOP, ID3, ConstraintSet.BOTTOM)
        constraintSet.connect(ID2, ConstraintSet.BOTTOM, ID3, ConstraintSet.BOTTOM)
        constraintSet.connect(ID2, ConstraintSet.END, ID3, ConstraintSet.END)

        return cell
    }
}

