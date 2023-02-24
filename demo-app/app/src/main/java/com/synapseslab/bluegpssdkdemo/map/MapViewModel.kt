/*
 * (c) Copyright 2021. All rights reserved by Synapses S.r.l.s.
 * https://www.synapseslab.com/
 *
 * Created by Davide Agostini on 30/11/21, 15:50.
 * Last modified 11/10/21, 15:46
 */

package com.synapseslab.bluegpssdkdemo.map


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.synapseslab.bluegps_sdk.core.BlueGPSLib
import com.synapseslab.bluegps_sdk.utils.Resource
import com.synapseslab.bluegpssdkdemo.view.ViewState
import kotlinx.coroutines.launch

private const val TAG = "MapViewModel"

class MapViewModel : ViewModel() {

    val viewState = MutableLiveData<ViewState<Any>>()

    /**
     * Asks for a device configuration for advertising
     *
     */
    fun getDeviceConfiguration() {

        viewModelScope.launch {
            when(val result = BlueGPSLib.instance.getDeviceConfiguration()) {
                is Resource.Error -> {
                    viewState.value = ViewState.failed("- ${result.message}")
                }
                is Resource.Exception -> {
                    viewState.value = ViewState.failed(result.e.localizedMessage ?: "Exception")
                }
                is Resource.Success -> {
                    viewState.value = ViewState.success(result.data)
                }
            }
        }
    }
}