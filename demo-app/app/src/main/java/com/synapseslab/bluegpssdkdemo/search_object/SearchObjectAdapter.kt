/*
 * (c) Copyright 2021. All rights reserved by Synapses S.r.l.s.
 * https://www.synapseslab.com/
 *
 * Created by Davide Agostini on 30/11/21, 12:56.
 * Last modified 30/11/21, 12:56
 */

package com.synapseslab.bluegpssdkdemo.search_object

import android.view.LayoutInflater
import android.view.ViewGroup
import com.synapseslab.bluegps_sdk.data.model.search_object.TrackElement
import com.synapseslab.bluegpssdkdemo.databinding.ItemObjectBinding

class SearchObjectAdapter(
    private val list: MutableList<TrackElement>,
    private val listener: (TrackElement) -> Unit
): androidx.recyclerview.widget.RecyclerView.Adapter<SearchObjectAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemObjectBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setList(list: List<TrackElement>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    inner class ViewHolder(val view: ItemObjectBinding) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view.root) {
        fun bind(resource: TrackElement) {
            view.textView.text = resource.label
            view.root.setOnClickListener { listener(list[adapterPosition]) }
        }
    }
}