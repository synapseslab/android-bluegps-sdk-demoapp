package com.synapseslab.bluegpssdkdemo.search_object

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.synapseslab.bluegps_sdk.core.BlueGPSLib
import com.synapseslab.bluegps_sdk.core.getTrackElement
import com.synapseslab.bluegps_sdk.utils.Resource
import com.synapseslab.bluegpssdkdemo.view.ViewState
import kotlinx.coroutines.launch

private const val TAG = "ObjectDetailViewModel"

class ObjectDetailViewModel: ViewModel() {

    val viewState = MutableLiveData<ViewState<Any>>()

    /**
     * Returns a track element passing a corresponding id
     *
     * @param trackElementId
     */
    fun getTrackElement(
        trackElementId: Int
    ) {
        viewState.value = ViewState.loading()
        viewModelScope.launch {
            when (val result = BlueGPSLib.instance.getTrackElement(trackElementId)) {
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