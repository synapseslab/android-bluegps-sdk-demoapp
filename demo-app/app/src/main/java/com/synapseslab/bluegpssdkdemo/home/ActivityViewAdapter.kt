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