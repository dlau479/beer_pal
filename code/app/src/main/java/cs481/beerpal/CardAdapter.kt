package cs481.beerpal

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.AppCompatRatingBar
import androidx.recyclerview.widget.RecyclerView



class CardAdapter(private var dataList: List<Beer>) : RecyclerView.Adapter<CardAdapter.ViewHolder>() {

    private lateinit var itemListener : OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(position : Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemListener = listener
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_cell, parent, false)
        return ViewHolder(view, itemListener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dataList[position]
        holder.b_name.text = item.title
        holder.b_desc.text = item.description
        holder.b_rating.rating = item.rating
        holder.b_brewery.text = item.brewery
        holder.b_abv.text = item.abv.toString().plus("%")
        getImageFromUrl(holder.b_picture).execute(item.url)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun filterList(beers: ArrayList<Beer>) {
        this.dataList = beers
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View, listener: OnItemClickListener) : RecyclerView.ViewHolder(itemView) {
        val b_name: TextView = itemView.findViewById(R.id.card_title)
        val b_desc: TextView = itemView.findViewById(R.id.card_description)
        val b_rating: RatingBar = itemView.findViewById(R.id.card_rating)
        val b_brewery: TextView = itemView.findViewById(R.id.card_brewery)
        val b_abv: TextView = itemView.findViewById(R.id.card_abv)
        val b_picture: ImageView = itemView.findViewById(R.id.card_thumbnail)

        init {
            itemView.setOnClickListener { v: View ->
                //Toast.makeText(itemView.context, "${dataList[adapterPosition]} clicked.", Toast.LENGTH_LONG).show()
                var position: Int = adapterPosition
                listener.onItemClick(position)
            }
        }
    }

    private inner class getImageFromUrl(var imageView: ImageView) : AsyncTask<String, Void, Bitmap?>(){
        override fun doInBackground(vararg url: String?): Bitmap? {
            val imageUrl = url[0]
            var image: Bitmap? = null
            try{
                image = BitmapFactory.decodeStream(java.net.URL(imageUrl).openStream())
            }
            catch(e:Exception){
                Log.d("Error downloading image: ", e.message.toString())
                e.printStackTrace()
            }
            return image
        }

        override fun onPostExecute(result: Bitmap?) {
            imageView.setImageBitmap(result)
        }
    }

}

