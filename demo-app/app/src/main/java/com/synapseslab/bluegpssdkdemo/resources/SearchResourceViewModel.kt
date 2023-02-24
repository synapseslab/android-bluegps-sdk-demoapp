/*
 * (c) Copyright 2021. All rights reserved by Synapses S.r.l.s.
 * https://www.synapseslab.com/
 *
 * Created by Davide Agostini on 30/11/21, 14:41.
 * Last modified 30/11/21, 14:41
 */

package com.synapseslab.bluegpssdkdemo.resources

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.synapseslab.bluegps_sdk.core.BlueGPSLib
import com.synapseslab.bluegps_sdk.core.findResources
import com.synapseslab.bluegps_sdk.utils.Resource
import com.synapseslab.bluegpssdkdemo.view.ViewState
import kotlinx.coroutines.launch

class SearchResourceViewModel : ViewModel() {

    val viewState = MutableLiveData<ViewState<Any>>()

    /**
     * Register the SDK to the server.
     * The management of the environment is demanded to the app.
     *
     * @param sdkEnvironment
     */
    fun findResources(
        isDesc: Boolean? = null,
        mapId: Int? = null,
        order: String? = null,
        search: String? = null,
        subTypes: List<String>? = null,
        types: List<String>? = null,
        roomId: Int? = null,
        roomName: String? = null
    ) {
        viewState.value = ViewState.loading()
        viewModelScope.launch {
            when (val result = BlueGPSLib.instance.findResources(
                hasPosition = true,
                isDesc = isDesc,
                mapId = mapId,
                order = order,
                search = search,
                subTypes = subTypes,
                types = types,
                roomId = roomId,
                roomName = roomName
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