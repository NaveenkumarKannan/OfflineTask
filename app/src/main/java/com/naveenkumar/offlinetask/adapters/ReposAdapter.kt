package com.naveenkumar.offlinetask.adapters

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.ShapeDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.naveenkumar.offlinetask.R
import com.naveenkumar.offlinetask.room.Repo
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.list_item.view.*

typealias ListItemClickListener = (position: Int) -> Unit

class ReposAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    public var repo: List<Repo> = mutableListOf()

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

            val gradientDrawable = (itemView.tv_language_color.background as GradientDrawable).mutate()
            val color = item.languageColor.toString()
            if(color == "null"){
                itemView.tv_language_color.visibility = View.GONE
            }else{
                val pColor = Color.parseColor(color)
                (gradientDrawable as GradientDrawable).setColor(pColor)
            }

            //itemView.tv_language_color.background = gradientDrawable

            itemView.tv_star_count.text = item.stars.toString()

            itemView.setOnClickListener{
                mListItemClickListener?.invoke(adapterPosition)
            }
        }
    }
}