/*
 * (c) Copyright 2021. All rights reserved by Synapses S.r.l.s.
 * https://www.synapseslab.com/
 *
 * Created by Davide Agostini on 30/11/21, 14:41.
 * Last modified 30/11/21, 14:41
 */

package com.synapseslab.bluegpssdkdemo.search_object

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.synapseslab.bluegps_sdk.core.BlueGPSLib
import com.synapseslab.bluegps_sdk.core.getTrackElements
import com.synapseslab.bluegps_sdk.utils.Resource
import com.synapseslab.bluegpssdkdemo.view.ViewState
import kotlinx.coroutines.launch

class SearchObjectViewModel : ViewModel() {

    val viewState = MutableLiveData<ViewState<Any>>()

    /**
     *
     * Return a Track Element list
     *
     * @param search the name of the track element
     * @param type
     * @param groupIds
     * @param isDesc a boolean
     * @param order
     */
    fun getTrackElements(
        type: String? = null,
        groupIds: List<Int>? = null,
        search: String? = null,
        isDesc: Boolean? = null,
        order: String? = null,
    ) {
        viewState.value = ViewState.loading()
        viewModelScope.launch {
            when (val result = BlueGPSLib.instance.getTrackElements(
                type = type,
                groupIds = groupIds,
                search = search,
                isDesc = isDesc,
                order = order,
            )) {
                is Resource.Success -> {
                    viewState.value = ViewState.success(result.data)
                }
                is Resource.Error -> {
                    viewState.value = ViewState.failed(result.message)
                }
                is Resource.Exception -> {
                    viewState.value = ViewState.failed(result.e.localizedMessage ?: "Exception")
                }
            }
        }
    }
}