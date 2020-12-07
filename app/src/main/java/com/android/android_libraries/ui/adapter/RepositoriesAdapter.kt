package com.android.android_libraries.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.android_libraries.R
import com.android.android_libraries.mvp.presenter.list.IRepositoriesListPresenter
import com.android.android_libraries.mvp.view.list.RepositoryItemView

class RepositoriesAdapter(private var presenter: IRepositoriesListPresenter) :
    RecyclerView.Adapter<RepositoriesAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_repository, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.pos = position
        presenter.bind(holder)
    }

    override fun getItemCount(): Int {
        return presenter.getCount()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        RepositoryItemView {
        var pos = 0

        private val textView: TextView

        init {
            textView = itemView.findViewById(R.id.tv_name)
        }

        override fun getCurrentPos(): Int {
            return pos
        }

        override fun setName(name: String) {
            textView.text = name
        }
    }
}