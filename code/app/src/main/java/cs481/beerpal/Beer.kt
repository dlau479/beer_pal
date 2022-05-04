package cs481.beerpal

import android.media.Image

var beerList = mutableListOf<Beer>()

class Beer(
    var title: String,
    var description: String,
    var rating: Float,
    var brewery: String,
    var abv: Double,
    var id: Long,
    var url: String
)