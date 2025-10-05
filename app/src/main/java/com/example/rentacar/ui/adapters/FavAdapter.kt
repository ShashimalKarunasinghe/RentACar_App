package com.example.rentacar.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.rentacar.data.Car
import com.example.rentacar.databinding.ItemFavouriteBinding

/**
 * Simple adapter for the horizontal favourites row.
 * - Shows car image + name
 * - onTap returns the car id so caller can toggle favourite or open details
 */
class FavAdapter(
    private var items: List<Car>,
    private val onTap: (String) -> Unit
) : RecyclerView.Adapter<FavAdapter.VH>() {

    class VH(val b: ItemFavouriteBinding) : RecyclerView.ViewHolder(b.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemFavouriteBinding.inflate(inflater, parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val car = items[position]
        holder.b.imgThumb.setImageResource(car.imageRes)
        holder.b.tvName.text = car.name
        holder.b.root.setOnClickListener { onTap(car.id) }
    }

    override fun getItemCount(): Int = items.size

    /** Replace whole list (called from Activity when favourites change). */
    fun replace(newItems: List<Car>) {
        items = newItems
        notifyDataSetChanged()
    }
}
