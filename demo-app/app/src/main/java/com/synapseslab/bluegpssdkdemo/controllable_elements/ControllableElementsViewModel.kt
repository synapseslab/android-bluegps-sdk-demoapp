/*
 * Copyright (c) 2022 Synapses s.r.l.s. All rights reserved.
 * https://synapseslab.com/
 */

package com.synapseslab.bluegpssdkdemo.controllable_elements

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.synapseslab.bluegps_sdk.core.BlueGPSLib
import com.synapseslab.bluegps_sdk.core.getControllableItems
import com.synapseslab.bluegps_sdk.data.model.controllable_item.ItemControllableFilter
import com.synapseslab.bluegps_sdk.utils.Resource
import com.synapseslab.bluegpssdkdemo.view.ViewState
import kotlinx.coroutines.launch

class ControllableElementsViewModel: ViewModel() {

    val viewState = MutableLiveData<ViewState<Any>>()


    fun getControllableItems() {
        viewState.value = ViewState.loading()
        viewModelScope.launch {
            when(val result = BlueGPSLib.instance.getControllableItems(ItemControllableFilter())) {
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