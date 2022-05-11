package cs481.beerpal.recyclerview


import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import cs481.beerpal.BeerRepository
import cs481.beerpal.R
import cs481.beerpal.model.BeerItem


interface WishListRVListener{
    fun itemSelected(selectedItem:BeerItem)
}

class WishListRVAdapter (val listener: WishListRVListener) : RecyclerView.Adapter<WishListRVAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WishListRVAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.beer_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: WishListRVAdapter.ViewHolder, position: Int) {
        Log.e("RVAdapter", "onBindViewHolder: " )
        val item = BeerRepository.beerListMock()[position]
        holder.beername.text = item.name.toString()
        holder.beerbrand.text = item.brand.toString()

    }
    override fun getItemCount(): Int {
       Log.e("RVAdapter", "getItemCount: ${BeerRepository.beerListMock().size} " )
        return BeerRepository.beerListMock().size
    }
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val beername: TextView = itemView.findViewById(R.id.beername)
        val beerbrand: TextView = itemView.findViewById(R.id.beerbrand)

        init {
            itemView.setOnClickListener {
                Toast.makeText(itemView.context, "${BeerRepository.beerListMock()[adapterPosition]} clicked.", Toast.LENGTH_LONG).show()
                listener.itemSelected(selectedItem = BeerRepository.beerListMock()[adapterPosition])
            }
        }
    }
}