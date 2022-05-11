package cs481.beerpal

//var beerList = mutableListOf<Beer>()
val BEER_ID_EXTRA =  "cs481.beerpal.beerExtra"
val BEER_TITLE_EXTRA =  "cs481.beerpal.beerTitle"
val BEER_DESC_EXTRA =  "cs481.beerpal.beerDesc"
val BEER_RATING_EXTRA =  "cs481.beerpal.beerRating"
val BEER_BREWERY_EXTRA =  "cs481.beerpal.beerBrewery"
val BEER_ABV_EXTRA =  "cs481.beerpal.beerABV"
val BEER_URL_EXTRA =  "cs481.beerpal.beerURL"
val BEER_WISH_EXTRA =  "cs481.beerpal.beerWISH"

class Beer(
    var title: String,
    var description: String,
    var rating: Float,
    var brewery: String,
    var abv: Double,
    var id: Long,
    var url: String,
    var isOnWishList:  Boolean = false
)