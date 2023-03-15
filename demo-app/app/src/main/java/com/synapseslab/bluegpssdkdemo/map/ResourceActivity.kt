/*
 * Copyright (c) 2023 Synapses s.r.l.s. All rights reserved.
 *
 * Licensed under the Apache License.
 * You may obtain a copy of the License at
 *
 * https://github.com/synapseslab/android-bluegps-sdk-demoapp/blob/main/LICENSE.md
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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