/*
 * (c) Copyright 2021. All rights reserved by Synapses S.r.l.s.
 * https://www.synapseslab.com/
 *
 * Created by Davide Agostini on 03/12/21, 14:43.
 * Last modified 03/12/21, 14:43
 */

package com.synapseslab.bluegpssdkdemo.navigation

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.synapseslab.bluegps_sdk.data.model.map.GenericResource

class SpinnerAdapter(
    context: Context,
    @LayoutRes private val layoutResource: Int,
    private val resources: List<GenericResource>
) :
    ArrayAdapter<GenericResource>(context, layoutResource, resources) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createViewFromResource(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return createViewFromResource(position, convertView, parent)
    }

    private fun createViewFromResource(
        position: Int,
        convertView: View?,
        parent: ViewGroup?
    ): View {
        val view: TextView = convertView as TextView? ?: LayoutInflater.from(context)
            .inflate(layoutResource, parent, false) as TextView
        //view.setPadding(20, 0, 20, 0)
        view.text = resources[position].name
        return view
    }
}