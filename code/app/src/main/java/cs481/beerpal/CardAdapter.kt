package cs481.beerpal

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatRatingBar
import androidx.recyclerview.widget.RecyclerView

class CardAdapter(private val dataList: List<Beer>) : RecyclerView.Adapter<CardAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_cell, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dataList[position]
        //image set view .setImageResource()
        holder.b_name.text = item.title
        holder.b_desc.text = item.description
        holder.b_rating.rating = item.rating
        holder.b_brewery.text = item.brewery
        holder.b_abv.text = item.abv.toString().plus("%")
    }

    override fun getItemCount(): Int {
        return dataList.size
    }
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val b_name: TextView = itemView.findViewById(R.id.card_title)
        val b_desc: TextView = itemView.findViewById(R.id.card_description)
        val b_rating: RatingBar = itemView.findViewById(R.id.card_rating)
        val b_brewery: TextView = itemView.findViewById(R.id.card_brewery)
        val b_abv: TextView = itemView.findViewById(R.id.card_abv)

        init {
            itemView.setOnClickListener {
                Toast.makeText(itemView.context, "${dataList[adapterPosition]} clicked.", Toast.LENGTH_LONG).show()
            }
        }
    }
}

