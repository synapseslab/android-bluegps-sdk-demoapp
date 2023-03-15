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

package com.synapseslab.bluegpssdkdemo.controllable_elements

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.synapseslab.bluegps_sdk.core.BlueGPSLib
import com.synapseslab.bluegps_sdk.core.setControllableItem
import com.synapseslab.bluegps_sdk.data.model.controllable_item.ItemControl
import com.synapseslab.bluegps_sdk.data.model.controllable_item.ItemControlType
import com.synapseslab.bluegps_sdk.data.model.controllable_item.ItemControllable
import com.synapseslab.bluegps_sdk.utils.Resource
import com.synapseslab.bluegpssdkdemo.controllable_elements.ControllableElements.ctrlButton
import com.synapseslab.bluegpssdkdemo.controllable_elements.ControllableElements.ctrlCardView
import com.synapseslab.bluegpssdkdemo.controllable_elements.ControllableElements.ctrlLabel
import com.synapseslab.bluegpssdkdemo.controllable_elements.ControllableElements.ctrlRadioButton
import com.synapseslab.bluegpssdkdemo.controllable_elements.ControllableElements.ctrlSlider
import com.synapseslab.bluegpssdkdemo.controllable_elements.ControllableElements.ctrlToggle
import com.synapseslab.bluegpssdkdemo.databinding.ItemControllableBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


private const val TAG = "ControllableElementsAdapter"

class ControllableElementsAdapter(
    private val context: Context,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        lateinit var viewHolder: RecyclerView.ViewHolder
        val view =
            ItemControllableBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        viewHolder = TitleViewHolder(view)
        return viewHolder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewHolder = holder as TitleViewHolder
        viewHolder.bind(position)
    }

    inner class TitleViewHolder(val view: ItemControllableBinding) :
        RecyclerView.ViewHolder(view.root) {

        @SuppressLint("UseSwitchCompatOrMaterialCode")
        fun bind(position: Int) = with(view) {
            val item = list[position] as ItemControllable
            tvTitle.text = item.name ?: item.i18nName

            val lpt = tvTitle.layoutParams as ViewGroup.MarginLayoutParams
            lpt.setMargins(48, 48, 48, 0)


            llControls.removeAllViews()

            item.controls?.forEach { c ->
                when (c.type) {
                    ItemControlType.CTRL_BUTTON -> {
                        val button = ctrlButton(context, c) { value ->
                            value?.let {
                                c.setCurrentValue(value)
                                setControllableItem(c)
                            }
                        }
                        llControls.addView(button)
                    }
                    ItemControlType.CTRL_TOGGLE -> {
                        val cardView = ctrlCardView(context)
                        val toggle = ctrlToggle(context, c) { value ->
                            value?.let {
                                c.setCurrentValue(value)
                                setControllableItem(c)
                            }
                        }

                        val lpGroup = toggle.layoutParams as ViewGroup.MarginLayoutParams
                        lpGroup.setMargins(48, 48, 48, 48)
                        cardView.addView(toggle)
                        llControls.addView(cardView)
                    }
                    ItemControlType.CTRL_CHOICE -> {
                        val cardView = ctrlCardView(context)
                        val group = ctrlRadioButton(context, c) { value ->
                            value?.let {
                                c.setCurrentValue(value)
                                setControllableItem(c)
                            }
                        }

                        val lpGroup = group.layoutParams as ViewGroup.MarginLayoutParams
                        lpGroup.setMargins(48, 48, 48, 48)
                        cardView.addView(group)
                        llControls.addView(cardView)
                    }
                    ItemControlType.CTRL_SLIDER -> {
                        val cardView = ctrlCardView(context)
                        val slider = ctrlSlider(context, c) { value ->
                            value?.let {
                                c.setCurrentValue(value)
                                setControllableItem(c)
                            }
                        }

                        val lpGroup = slider.layoutParams as ViewGroup.MarginLayoutParams
                        lpGroup.setMargins(48, 64, 48, 64)
                        cardView.addView(slider)
                        llControls.addView(cardView)
                    }
                    ItemControlType.CTRL_LABEL -> {
                        val tv = ctrlLabel(context, c)
                        llControls.addView(tv)
                    }
                }
            }


        }
    }

    private fun setControllableItem(itemControl: ItemControl) {
        CoroutineScope(Dispatchers.IO).launch {
            when (val result =
                BlueGPSLib.instance.setControllableItem(itemControl.currentValue)) {
                is Resource.Error -> {
                    Log.e(TAG, result.message)
                }
                is Resource.Exception -> {
                    Log.e(TAG, result.e.localizedMessage ?: "Exception")
                }
                is Resource.Success -> {
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(
                            context,
                            "Action successfully executed",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    Log.d(TAG, result.data.toString())
                }
            }
        }
    }


    override fun getItemCount(): Int {
        return list.size
    }

    private val diffCallback = object : DiffUtil.ItemCallback<Any>() {
        override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
            return when {
                oldItem is ItemControllable && newItem is ItemControllable -> oldItem.id == newItem.id
                oldItem is String && newItem is String -> oldItem == newItem
                else -> oldItem == newItem
            }
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(
            oldItem: Any,
            newItem: Any
        ): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    var list: List<Any>
        get() = differ.currentList
        set(value) = differ.submitList(value)
}