/*
 * Copyright (c) 2022 Synapses s.r.l.s. All rights reserved.
 * https://synapseslab.com/
 */

package com.synapseslab.bluegpssdkdemo.area

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.synapseslab.bluegps_sdk.core.BlueGPSLib
import com.synapseslab.bluegps_sdk.data.model.request.AreaFilterRequest
import com.synapseslab.bluegps_sdk.data.model.request.RealtimeAreaElementRequest
import com.synapseslab.bluegps_sdk.data.model.request.RealtimeElementType
import com.synapseslab.bluegps_sdk.utils.Resource
import com.synapseslab.bluegpssdkdemo.view.ViewState
import kotlinx.coroutines.launch

private const val TAG = "AreaViewModel"

class AreaViewModel: ViewModel() {

    val viewState = MutableLiveData<ViewState<Any>>()

    init {
        getAreasWithTagsInside()

        //getAreasList()

        //getAreaListRealtimeElement()
    }


    /**
     * Return a list of areas with a list of inside tags
     *
     * @param areaFilterRequest a list of areas id or maps id
     */
    private fun getAreasWithTagsInside() {
        viewState.value = ViewState.loading()
        viewModelScope.launch {

            val areaFilterRequest = AreaFilterRequest(
                areaIds = listOf(14)
            )

            when(val result = BlueGPSLib.instance.getAreasWithTagsInside()) {
                is Resource.Error -> {
                    Log.e(TAG, result.message)
                    viewState.value = ViewState.failed(result.message)
                }
                is Resource.Exception -> {
                    Log.e(TAG, result.e.localizedMessage ?: "Exception")
                    viewState.value = ViewState.failed(result.e.localizedMessage ?: "Exception")
                }
                is Resource.Success -> {
                    Log.d(TAG, result.data.toString())
                    val areaList = result.data
                    if (areaList.isNotEmpty()) {
                        viewState.value = ViewState.success(areaList.toString())
                    } else {
                        viewState.value = ViewState.success("No tags inside")
                    }
                }
            }
        }
    }

    /**
     * Return a list of areas
     *
     */
    private fun getAreasList() {
        viewState.value = ViewState.loading()
        viewModelScope.launch {
            when(val result = BlueGPSLib.instance.getAreasList()) {
                is Resource.Error -> {
                    Log.e(TAG, result.message)
                    viewState.value = ViewState.failed(result.message)
                }
                is Resource.Exception -> {
                    Log.e(TAG, result.e.localizedMessage ?: "Exception")
                    viewState.value = ViewState.failed(result.e.localizedMessage ?: "Exception")
                }
                is Resource.Success -> {
                    Log.d(TAG, result.data.toString())
                    val areaList = result.data
                    if (areaList.isNotEmpty()) {
                        viewState.value = ViewState.success(areaList.toString())
                    } else {
                        viewState.value = ViewState.success("No areas")
                    }
                }
            }
        }
    }

    /**
     * Return a `RealtimeAreaElementResponse` that contains the list of areas where
     * a realtime element (Tag, Element, etc.) is located.
     *
     */
    private fun getAreaListRealtimeElement() {
        viewState.value = ViewState.loading()
        viewModelScope.launch {
            val realtimeAreaElementRequest = RealtimeAreaElementRequest(
                id = "010001010001",
                type = RealtimeElementType.TAGS
            )
            when(val result = BlueGPSLib.instance.getAreaListRealtimeElement(realtimeAreaElementRequest)) {
                is Resource.Error -> {
                    Log.e(TAG, result.message)
                    viewState.value = ViewState.failed(result.message)
                }
                is Resource.Exception -> {
                    Log.e(TAG, result.e.localizedMessage ?: "Exception")
                    viewState.value = ViewState.failed(result.e.localizedMessage ?: "Exception")
                }
                is Resource.Success -> {
                    Log.d(TAG, result.data.toString())
                    viewState.value = ViewState.success(result.data.toString())
                }
            }
        }
    }

}