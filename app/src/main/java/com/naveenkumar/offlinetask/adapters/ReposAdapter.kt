package com.naveenkumar.offlinetask.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.naveenkumar.offlinetask.R
import com.naveenkumar.offlinetask.api.model.Repo
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.list_item.view.*

typealias ListItemClickListener = (position: Int) -> Unit

class ReposAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var repo: List<Repo> = mutableListOf()

    private var mListItemClickListener: ListItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return RepoHolder(view)
    }

    override fun getItemCount(): Int {
        return repo.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = repo.get(position)
        (holder as? RepoHolder)?.bind(item)
    }

    fun setData(repo: List<Repo>) {
        this.repo = repo
        notifyDataSetChanged()
    }

    fun setListeners(listItemClickListener: ListItemClickListener?) {
        mListItemClickListener = listItemClickListener
    }

    inner class RepoHolder(override val containerView: View): RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(item: Repo) {
            itemView.tv_name.text = item.name
            itemView.tv_desc.text = item.description
            itemView.tv_lang.text = item.language

            itemView.setOnClickListener{
                mListItemClickListener?.invoke(adapterPosition)
            }
        }
    }
}