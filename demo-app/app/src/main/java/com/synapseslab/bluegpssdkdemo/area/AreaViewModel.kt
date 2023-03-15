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