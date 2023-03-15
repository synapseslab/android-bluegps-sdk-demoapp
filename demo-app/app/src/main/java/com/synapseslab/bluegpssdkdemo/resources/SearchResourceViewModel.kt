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