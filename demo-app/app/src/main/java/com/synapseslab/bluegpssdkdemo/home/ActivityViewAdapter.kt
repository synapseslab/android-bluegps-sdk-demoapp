/*
 * (c) Copyright 2021. All rights reserved by Synapses S.r.l.s.
 * https://www.synapseslab.com/
 *
 * Created by Davide Agostini on 30/11/21, 12:56.
 * Last modified 30/11/21, 12:56
 */

package com.synapseslab.bluegpssdkdemo.home

import android.view.LayoutInflater
import android.view.ViewGroup
import com.synapseslab.bluegpssdkdemo.databinding.ItemActivityBinding

class ActivityViewAdapter(
    private val list: MutableList<ViewActivity>,
    private val listener: (ViewActivity) -> Unit
): androidx.recyclerview.widget.RecyclerView.Adapter<ActivityViewAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemActivityBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setList(list: List<ViewActivity>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    inner class ViewHolder(val view: ItemActivityBinding) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view.root) {
        fun bind(viewActivity: ViewActivity) {
            view.textView.text = viewActivity.title
            view.ivIcon.setImageResource(viewActivity.icon)
            view.root.setOnClickListener { listener(list[adapterPosition]) }
        }
    }
}


data class ViewActivity(
    val title: String,
    val icon: Int,
    val cls: Class<*>
)