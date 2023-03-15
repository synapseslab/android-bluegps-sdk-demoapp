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
import android.os.Build
import android.util.Log
import android.view.Gravity
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.synapseslab.bluegps_sdk.data.model.controllable_item.ItemControl
import com.synapseslab.bluegpssdkdemo.R
import com.synapseslab.bluegpssdkdemo.utils.convertRange
import com.synapseslab.bluegpssdkdemo.utils.getConvertedValue


private const val TAG = "ControllableElements"

object ControllableElements {


    fun ctrlCardView(context: Context): CardView {
        val cardView = CardView(context)
        cardView.radius = 15f
        cardView.elevation = 10f

        cardView.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        val lpCardView = cardView.layoutParams as ViewGroup.MarginLayoutParams
        lpCardView.setMargins(48, 32, 48, 32)

        return cardView
    }

    fun ctrlLabel(context: Context, itemControl: ItemControl): TextView {

        val tv = TextView(context)
        tv.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        tv.gravity = Gravity.CENTER

        val lpt = tv.layoutParams as ViewGroup.MarginLayoutParams
        lpt.setMargins(48, 16, 48, 16)

        tv.text = itemControl.currentValue.value ?: "Label"
        tv.textSize = 16f
        tv.setTextColor(ContextCompat.getColor(context, R.color.blue_500))

        return tv

    }

    fun ctrlButton(
        context: Context,
        itemControl: ItemControl,
        onClickListener: ((Any?) -> Unit)?
    ): Button {

        val btn = Button(context)
        btn.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        val lpt = btn.layoutParams as ViewGroup.MarginLayoutParams
        lpt.setMargins(48, 16, 48, 16)

        btn.text = itemControl.buttonTitle
        btn.textSize = 16f
        btn.isAllCaps = true
        btn.setBackgroundColor(ContextCompat.getColor(context, R.color.blue_500))
        btn.setTextColor(ContextCompat.getColor(context, R.color.white))
        btn.setBackgroundResource(R.drawable.round_button_border)

        btn.setOnClickListener {
            onClickListener?.let { it(itemControl.buttonValue) }
        }

        return btn
    }

    fun ctrlRadioButton(
        context: Context,
        itemControl: ItemControl,
        orientation: Int = RadioGroup.VERTICAL,
        onClickListener: ((Any?) -> Unit)?
    ): RadioGroup {

        val radioGroup = RadioGroup(context)
        radioGroup.orientation = orientation
        radioGroup.layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        val lpt = radioGroup.layoutParams as ViewGroup.MarginLayoutParams
        lpt.setMargins(0, 16, 0, 16)

        itemControl.choiceOptions?.forEachIndexed { index, option ->
            val radioButton = RadioButton(context)
            radioButton.text = option.name
            radioButton.textSize = 16f
            radioButton.isChecked = option.optionValue == itemControl.currentValue.value
            radioButton.id = index
            radioGroup.addView(radioButton)
        }

        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            val radioButton = group.findViewById<RadioButton>(checkedId)
            Log.d(TAG, "${radioButton.text}")
            onClickListener?.let { it(itemControl.choiceOptions?.get(checkedId)) }
        }

        return radioGroup

    }

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    fun ctrlToggle(
        context: Context,
        itemControl: ItemControl,
        onClickListener: ((Any?) -> Unit)?
    ): Switch {

        val switch = Switch(context)
        switch.layoutParams = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            Gravity.CENTER
        )
        switch.gravity = Gravity.CENTER
        val lpt = switch.layoutParams as ViewGroup.MarginLayoutParams
        lpt.setMargins(48, 16, 48, 16)

        switch.isChecked = itemControl.currentValue.value == itemControl.toggleOnValue

        switch.setOnCheckedChangeListener { _, isChecked ->
            onClickListener?.let { it(if(isChecked) itemControl.toggleOnValue else itemControl.toggleOffValue) }
        }

        return switch

    }

    fun ctrlSlider(
        context: Context,
        itemControl: ItemControl,
        onClickListener: ((Any?) -> Unit)?
    ): SeekBar {

        val seekBar = SeekBar(context)
        seekBar.layoutParams = FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            Gravity.CENTER
        )
        val lpt = seekBar.layoutParams as ViewGroup.MarginLayoutParams
        lpt.setMargins(48, 16, 48, 16)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            seekBar.maxHeight = 15
            seekBar.minHeight = 15
        }

        if (itemControl.max != null && itemControl.min != null && itemControl.currentValue.value != null) {
            seekBar.max = itemControl.max!!.toInt()
            val progress = convertRange(
                itemControl.min!!.toFloat(),
                itemControl.max!!.toFloat(),
                0f,
                itemControl.max!!.toFloat(),
                itemControl.currentValue.value!!.toFloat()
            )
            seekBar.progress = progress.toInt()
        }

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
                val progress = getConvertedValue(
                    p0?.progress!!.toFloat(),
                    itemControl.max!!.toFloat(),
                    itemControl.max!!.toFloat(),
                    itemControl.min!!.toFloat(),
                )
                onClickListener?.let { it(progress.toDouble()) }
            }
        })

        return seekBar
    }

}