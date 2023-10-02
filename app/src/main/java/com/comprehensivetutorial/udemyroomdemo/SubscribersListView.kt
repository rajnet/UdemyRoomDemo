package com.comprehensivetutorial.udemyroomdemo

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.comprehensivetutorial.udemyroomdemo.database.Subscriber
import com.comprehensivetutorial.udemyroomdemo.databinding.SubscriberListItemBinding

class SubscribersListView(
    private val onItemClicked: (Subscriber) -> Unit
): ListAdapter<Subscriber, SubscribersListView.SubscriberViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubscriberViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: SubscriberListItemBinding = DataBindingUtil
            .inflate(
                layoutInflater,
                R.layout.subscriber_list_item,
                parent, false
            )

        val viewHolder = SubscriberViewHolder(binding)
        binding.itemCardView.setOnClickListener {
            val item = getItem(viewHolder.adapterPosition)
            onItemClicked(item)
        }

        return viewHolder
    }

    override fun onBindViewHolder(holder: SubscriberViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class SubscriberViewHolder(
        private val binding: SubscriberListItemBinding
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(subscriber: Subscriber) {
            binding.subscriberName.text = subscriber.name
            binding.subscriberEmail.text = subscriber.email
        }
    }

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<Subscriber>() {
            override fun areItemsTheSame(oldItem: Subscriber, newItem: Subscriber): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Subscriber, newItem: Subscriber): Boolean {
                return oldItem == newItem
            }
        }
    }
}