package cs481.beerpal

import cs481.beerpal.model.RatingReview
import org.junit.Test
import org.junit.Assert.*

class DataTypeTests {

    @Test
    fun testReviewDataTypes() {
        println("\n---Testing Review data types---")
        val review = Review(2, 4.2.toFloat(), "Test", "Test")
        var isCorrect = false;
        if(review.beer_id is Long && review.rating is Float && review.review is String && review.userName is String){
            isCorrect = true;
        }
        assertTrue("Review data types are not correct", isCorrect);
    }

    @Test
    fun testRatingReviewDataTypes() {
        println("\n---Testing Rating Review data types---")
        val review = RatingReview(2, 4.2, "Test", "Test")
        var isCorrect = false;
        if(review.beer_id is Int && review.rating is Double && review.review is String && review.user_email is String){
            isCorrect = true;
        }
        assertTrue("Rating Review data types are not correct", isCorrect);
    }
}