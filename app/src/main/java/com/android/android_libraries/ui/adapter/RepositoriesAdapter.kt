package com.android.android_libraries.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.android_libraries.databinding.ItemRepositoryBinding
import com.android.android_libraries.mvp.presenter.list.IRepositoriesListPresenter
import com.android.android_libraries.mvp.view.list.RepositoryItemView

class RepositoriesAdapter(private var presenter: IRepositoriesListPresenter?) :
    RecyclerView.Adapter<RepositoriesAdapter.ViewHolder>() {

    private lateinit var binding: ItemRepositoryBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemRepositoryBinding.inflate(LayoutInflater.from(parent.context))
        val view = binding.root
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(view.id, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.pos = position
        presenter!!.bind(holder)
    }

    override fun getItemCount(): Int {
        return presenter!!.getCount()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        RepositoryItemView {
        var pos = 0

        override fun getCurrentPos(): Int {
            return pos
        }

        override fun setName(name: String?) {
            binding.tvName.text = name
        }
    }
}