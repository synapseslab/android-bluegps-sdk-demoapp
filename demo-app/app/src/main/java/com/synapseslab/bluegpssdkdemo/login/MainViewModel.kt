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

package com.synapseslab.bluegpssdkdemo.login


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.synapseslab.bluegps_sdk.core.BlueGPSLib
import com.synapseslab.bluegps_sdk.data.model.environment.SdkEnvironment
import com.synapseslab.bluegps_sdk.utils.Resource
import com.synapseslab.bluegpssdkdemo.view.ViewState
import kotlinx.coroutines.launch

private const val TAG = "MainViewModel"

class MainViewModel : ViewModel() {

    val viewState = MutableLiveData<ViewState<Any>>()

    val deviceConfiguration = MutableLiveData<ViewState<Any>>()

    /**
     * Register the SDK to the server.
     * The management of the environment is demanded to the app.
     *
     * @param sdkEnvironment
     */
    fun registerSDK(sdkEnvironment: SdkEnvironment) {
        viewState.value = ViewState.loading()
        viewModelScope.launch {
            when (val result = BlueGPSLib.instance.registerSDK(sdkEnvironment)) {
                is Resource.Success -> {
                    viewState.value = ViewState.success("- Successfully logged in with token. ${result.data}")

                }
                is Resource.Error -> {
                    viewState.value = ViewState.failed("- ${result.message}")
                }
                is Resource.Exception -> {
                    viewState.value = ViewState.failed(result.e.localizedMessage ?: "Exception")
                }
            }
        }
    }

    /**
     * Asks for a device configuration for advertising
     *
     */
    fun getDeviceConfiguration() {

        viewModelScope.launch {
            when(val result = BlueGPSLib.instance.getOrCreateConfiguration()) {
                is Resource.Error -> {
                    deviceConfiguration.value = ViewState.failed("- ${result.message}")
                }
                is Resource.Exception -> {
                    deviceConfiguration.value = ViewState.failed(result.e.localizedMessage ?: "Exception")
                }
                is Resource.Success -> {
                    deviceConfiguration.value = ViewState.success(result.data)
                }
            }
        }
    }
}