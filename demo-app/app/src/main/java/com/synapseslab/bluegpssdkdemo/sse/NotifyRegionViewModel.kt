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

package com.synapseslab.bluegpssdkdemo.sse

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.synapseslab.bluegps_sdk.core.BlueGPSLib
import com.synapseslab.bluegps_sdk.core.startNotifyRegionChanges
import com.synapseslab.bluegps_sdk.core.stopNotifyRegionChanges
import com.synapseslab.bluegps_sdk.data.model.region.BGPRegion
import com.synapseslab.bluegps_sdk.utils.Resource
import kotlinx.coroutines.launch

private const val TAG = "NotifyRegionViewModel"

class NotifyRegionViewModel: ViewModel()  {

    val viewState = MutableLiveData<String>()

    var logText = ""

    init {
        getRoomsCoordinates()
    }

    private fun getRoomsCoordinates() {
        viewModelScope.launch {
            when (val result = BlueGPSLib.instance.getRoomsCoordinates()) {
                is Resource.Error -> {
                    Log.e(TAG, result.message)
                }
                is Resource.Exception -> {
                    Log.e(TAG, result.e.localizedMessage ?: "Exception")
                }
                is Resource.Success -> {
                    startNotifyRegionChanges(regions = result.data)
                }
            }
        }
    }

    private fun startNotifyRegionChanges(regions: List<BGPRegion>) {
        BlueGPSLib.instance.startNotifyRegionChanges(
            tags = listOf("010001010007", "RRRR00000010"),
            regions = regions,
            callbackHandler = { it: Map<String, MutableList<BGPRegion>> ->
                var concat = ""
                it.map {
                    val key = it.key
                    var regionList = ""
                    val myList = it.value


                    myList.forEach {
                        regionList += "${it.id}-${it.areaId}-${it.name},"
                    }
                    concat += "$key - ${regionList}\n"
                }
                logText += concat
                viewState.postValue(logText)
            }
        )
    }

    fun stopNotifyRegionChanges() {
        BlueGPSLib.instance.stopNotifyRegionChanges()
    }
}