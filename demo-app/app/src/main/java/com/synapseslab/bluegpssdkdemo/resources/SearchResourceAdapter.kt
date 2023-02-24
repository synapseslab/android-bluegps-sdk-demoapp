/*
 * (c) Copyright 2021. All rights reserved by Synapses S.r.l.s.
 * https://www.synapseslab.com/
 *
 * Created by Davide Agostini on 30/11/21, 12:56.
 * Last modified 30/11/21, 12:56
 */

package com.synapseslab.bluegpssdkdemo.resources

import android.view.LayoutInflater
import android.view.ViewGroup
import com.synapseslab.bluegps_sdk.data.model.map.GenericResource
import com.synapseslab.bluegpssdkdemo.databinding.ItemResourceBinding

class SearchResourceAdapter(
    private val list: MutableList<GenericResource>,
    private val listener: (GenericResource) -> Unit
): androidx.recyclerview.widget.RecyclerView.Adapter<SearchResourceAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemResourceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setList(list: List<GenericResource>) {
        this.list.clear()
        this.list.addAll(list)
        notifyDataSetChanged()
    }

    inner class ViewHolder(val view: ItemResourceBinding) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view.root) {
        fun bind(resource: GenericResource) {
            view.textView.text = resource.name
            view.root.setOnClickListener { listener(list[adapterPosition]) }
        }
    }
}