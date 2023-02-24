/*
 * (c) Copyright 2021. All rights reserved by Synapses S.r.l.s.
 * https://www.synapseslab.com/
 *
 * Created by Davide Agostini on 30/11/21, 15:50.
 * Last modified 14/10/21, 18:16
 */

package com.synapseslab.bluegpssdkdemo.map

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.synapseslab.bluegps_sdk.data.model.map.DataFilter
import com.synapseslab.bluegps_sdk.data.model.map.GenericResource
import com.synapseslab.bluegpssdkdemo.R
import com.synapseslab.bluegpssdkdemo.databinding.ActivityResourceBinding

class ResourceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResourceBinding
    private var adapter = VersionAdapter(mutableListOf()) { version ->
        setResult(RESULT_OK, Intent().putExtra("resource", version))
        //onBackPressed()
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
            }
        })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResourceBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        supportActionBar?.title = "Resource list"

        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.adapter = adapter

        val resourceList = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra("resourceList", DataFilter::class.java)
        } else {
            intent.getParcelableExtra("resourceList")
        }

        resourceList?.let {
            adapter.setList(resourceList.data)
        }
    }
}

class VersionAdapter(
    private val resourceList: MutableList<GenericResource>,
    private val clickListener: (version: GenericResource) -> Unit
) :
    RecyclerView.Adapter<VersionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VersionViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return VersionViewHolder.create(inflater, parent).apply {
            itemView.setOnClickListener {
                val version = resourceList[adapterPosition]
                clickListener(version)
            }
        }
    }

    override fun onBindViewHolder(holder: VersionViewHolder, position: Int) {
        val version = resourceList[position]
        holder.textView.text = "${version.id} - ${version.name} - ${version.type}"
    }

    override fun getItemCount(): Int {
        return resourceList.size
    }

    fun setList(resourceList: List<GenericResource>) {
        this.resourceList.clear()
        this.resourceList.addAll(resourceList)
        notifyDataSetChanged()
    }

}

class VersionViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val textView: TextView = itemView as TextView

    companion object {
        private const val LAYOUT_ID = R.layout.item

        fun create(inflater: LayoutInflater, parent: ViewGroup?): VersionViewHolder {
            return VersionViewHolder(inflater.inflate(LAYOUT_ID, parent, false))
        }
    }
}