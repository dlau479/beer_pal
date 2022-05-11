package cs481.beerpal.model

data class RatingReview (
    val beer_id : Int,
    val rating: Double,
    val review: String,
    val user_email: String

    )