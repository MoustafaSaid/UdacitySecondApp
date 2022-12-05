package com.example.myapplication.main
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.Asteroid
import com.example.myapplication.databinding.ItemAstroidBinding


class AsteroidAdapter(private val onClickListener: OnClickListener) :androidx.recyclerview.widget.ListAdapter<Asteroid,AsteroidAdapter.AsteroidViewHolder>(DiffCallback){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AsteroidViewHolder {
        return AsteroidViewHolder(ItemAstroidBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: AsteroidViewHolder, position: Int) {

        val asteroid = getItem(position)
        holder.bind(asteroid)
        holder.itemView.setOnClickListener{onClickListener.onClick(asteroid)}

    }

    class AsteroidViewHolder(private var binding: ItemAstroidBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(asteroid: Asteroid) {
            binding.asteroid = asteroid
            binding.executePendingBindings()
        }
    }
    companion object DiffCallback : DiffUtil.ItemCallback<Asteroid>() {
        override fun areItemsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem.id == newItem.id
        }
    }
        class OnClickListener(val clickListener: (asteroid: Asteroid) -> Unit) {
        fun onClick(asteroid: Asteroid) = clickListener(asteroid)
    }


}