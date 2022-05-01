package cs481.beerpal

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cs481.beerpal.databinding.SearchCardCellBinding

class CardAdapter(private val beers: List<Beer>)
    : RecyclerView.Adapter<CardViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val from = LayoutInflater.from(parent.context)
        val binding = SearchCardCellBinding.inflate(from, parent, false)
        return CardViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.bindBeer(beers[position])
    }

    override fun getItemCount(): Int {
        Log.e("CardAdapter", "getItemCount: ${beers.size}")
        return beers.size
    }
}