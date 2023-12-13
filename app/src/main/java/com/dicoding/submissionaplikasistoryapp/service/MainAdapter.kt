package com.dicoding.submissionaplikasistoryapp.service

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.submissionaplikasistoryapp.database.ListStory
import com.dicoding.submissionaplikasistoryapp.databinding.ItemStoryBinding
import java.text.SimpleDateFormat
import java.util.Locale

class MainAdapter : PagingDataAdapter<ListStory, MainAdapter.ViewModel>(DIFF_CALLBACK) {
    private var onItemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        onItemClickListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewModel {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewModel(binding)
    }

    override fun onBindViewHolder(holder: ViewModel, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
        }
    }

    inner class ViewModel(private val binding: ItemStoryBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ListStory) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(data.photoUrl)
                    .into(imgStory)
                tvTitleStory.text = data.name
                tvStoryDescription.text = data.description

                val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
                val date = inputFormat.parse(data.createdAt)!!
                val outputFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
                tvStoryDate.text = outputFormat.format(date)
            }
        }

        init {
            itemView.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClickListener?.onItemClick(getItem(position))
                }
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(story: ListStory?)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStory>() {
            override fun areItemsTheSame(oldItem: ListStory, newItem: ListStory): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ListStory, newItem: ListStory): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}