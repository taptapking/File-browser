package vn.edu.hust.filebrowser

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class ItemAdapter (val items: ArrayList<ItemModel>, var listener: ItemClickListener? = null, var listener2: ItemLongClickListener? = null) :  RecyclerView.Adapter<ItemAdapter.ItemViewHolder>(){

    override fun getItemCount(): Int {
        return items.size
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        return ItemViewHolder(itemView, listener, listener2)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.name?.text = items[position].name
        holder.extension?.text = items[position].extension
    }

    class ItemViewHolder(itemView: View, val listener: ItemClickListener?,  listener2: ItemLongClickListener?): RecyclerView.ViewHolder(itemView){
        var name: TextView? = null
        var extension: TextView? = null
        init{
            name = itemView.findViewById(R.id.name)
            extension = itemView.findViewById(R.id.extension)

            itemView.setOnClickListener{
                listener?.ItemClick(adapterPosition)
            }
            itemView.setOnLongClickListener{
                listener2?.ItemLongClick(adapterPosition)
                return@setOnLongClickListener true
            }
        }
    }

    interface ItemClickListener{
        fun ItemClick(position: Int)
    }
    interface ItemLongClickListener{
        fun ItemLongClick(position: Int)
    }


}