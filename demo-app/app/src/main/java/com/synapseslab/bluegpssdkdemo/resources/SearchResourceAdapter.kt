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