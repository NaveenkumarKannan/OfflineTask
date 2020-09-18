package com.naveenkumar.offlinetask.adapters

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.naveenkumar.offlinetask.R
import com.naveenkumar.offlinetask.api.model.BuiltBy
import com.naveenkumar.offlinetask.utils.Utility
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.activity_view_repo.*
import kotlinx.android.synthetic.main.built_by_list_item.view.*

class BuiltByAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    public var builtBy: List<BuiltBy> = mutableListOf()

    private var mListItemClickListener: ListItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.built_by_list_item, parent, false)
        return BuiltByHolder(view)
    }

    override fun getItemCount(): Int {
        return builtBy.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = builtBy.get(position)
        (holder as? BuiltByHolder)?.bind(item)
    }

    fun setData(builtBy: List<BuiltBy>) {
        this.builtBy = builtBy
        notifyDataSetChanged()
    }

    fun setListeners(listItemClickListener: ListItemClickListener?) {
        mListItemClickListener = listItemClickListener
    }

    inner class BuiltByHolder(override val containerView: View): RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(item: BuiltBy) {
            itemView.tv_name.text = item.username

            Glide.with(containerView.context)
                .load(item.avatar)
                .apply(Utility.options())
                .into(itemView.iv_avatar)

            itemView.setOnClickListener{
                mListItemClickListener?.invoke(adapterPosition)
            }
        }
    }
}