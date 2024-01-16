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

package com.synapseslab.bluegpssdkdemo.location

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.synapseslab.bluegps_sdk.core.BlueGPSLib
import com.synapseslab.bluegps_sdk.data.model.advertising.AndroidAdvConfiguration
import com.synapseslab.bluegps_sdk.utils.Resource
import kotlinx.coroutines.launch

private const val TAG = "LocationViewModel"

class LocationViewModel : ViewModel() {


    /**
     * Asks for a device configuration for advertising
     *
     */
    fun getDeviceConfiguration(handleResult: (Resource<AndroidAdvConfiguration>) -> Unit) {
        viewModelScope.launch {
            when(val res = BlueGPSLib.instance.getOrCreateConfiguration()) {
                is Resource.Error -> handleResult(Resource.Error(message = res.message, code = res.code))
                is Resource.Exception -> {
                    handleResult(Resource.Exception(res.e))
                }
                is Resource.Success -> {
                    val androidAdvConfiguration = res.data
                    handleResult(Resource.Success(androidAdvConfiguration))
                }
            }
        }
    }
}