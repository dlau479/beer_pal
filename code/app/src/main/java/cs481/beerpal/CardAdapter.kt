package cs481.beerpal

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import cs481.beerpal.databinding.SearchCardCellBinding
/*
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
*/

class CardAdapter(private val dataList: List<Beer>) : RecyclerView.Adapter<CardAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.search_card_cell, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dataList[position]
        holder.b_name.text = item.title
        holder.b_desc.text = item.description
        holder.b_rating.text = item.rating.toString()
        holder.b_brewery.text = item.brewery
        holder.b_abv.text = item.abv.toString()
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val b_name: TextView = itemView.findViewById(R.id.card_title)
        val b_desc: TextView = itemView.findViewById(R.id.card_description)
        val b_rating: TextView = itemView.findViewById(R.id.card_rating)
        val b_brewery: TextView = itemView.findViewById(R.id.card_brewery)
        val b_abv: TextView = itemView.findViewById(R.id.card_abv)

        init {
            itemView.setOnClickListener {
                Toast.makeText(itemView.context, "${dataList[adapterPosition]} clicked.", Toast.LENGTH_LONG).show()
            }
        }
    }
}

