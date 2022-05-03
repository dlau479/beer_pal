package cs481.beerpal

import androidx.recyclerview.widget.RecyclerView
import cs481.beerpal.databinding.SearchCardCellBinding

class CardViewHolder(
    private val cardCellBinding: SearchCardCellBinding
    ) : RecyclerView.ViewHolder(cardCellBinding.root)
{
    fun bindBeer(beer: Beer) {
        var abvText: String = "${beer.abv}%"
        //update card image resource inside setImageResource
        //cardCellBinding.cardThumbnail.setImageResource()

        cardCellBinding.cardTitle.text = beer.title
        cardCellBinding.cardDescription.text = beer.description
        cardCellBinding.cardRating.rating = beer.rating
        cardCellBinding.cardAbv.text = abvText
        cardCellBinding.cardBrewery.text = beer.brewery
    }
}