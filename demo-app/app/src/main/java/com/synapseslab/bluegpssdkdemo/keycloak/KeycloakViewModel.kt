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

package com.synapseslab.bluegpssdkdemo.keycloak

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.synapseslab.bluegps_sdk.authentication.core.BlueGPSAuthManager
import com.synapseslab.bluegps_sdk.utils.Resource
import com.synapseslab.bluegpssdkdemo.utils.Environment
import kotlinx.coroutines.launch

private const val TAG = "KeycloakViewModel"

class KeycloakViewModel : ViewModel() {

    val loginGuestMode = MutableLiveData("")

    fun refreshToken() {
        viewModelScope.launch {
            when (val result = BlueGPSAuthManager.shared.refreshToken()) {
                is Resource.Error -> {
                    Log.e(TAG, result.message)
                }
                is Resource.Exception -> {
                    Log.e(TAG, result.e.localizedMessage ?: "Exception")
                }
                is Resource.Success -> {
                    Log.v(TAG, "Token refreshed, ${result.data}")

                    // update access token on the environment
                    Environment.sdkEnvironment.sdkToken = result.data.access_token
                }
            }
        }
    }

    fun guestLogin() {
        viewModelScope.launch {
            when (val result = BlueGPSAuthManager.shared.guestLogin()) {
                is Resource.Error -> {
                    Log.e(TAG, result.message)
                }
                is Resource.Exception -> {
                    Log.e(TAG, result.e.localizedMessage ?: "Exception")
                }
                is Resource.Success -> {
                    Log.v(TAG, "Login in guest mode, ${result.data}")
                    loginGuestMode.value = "Login in guest mode"

                    // update access token on the environment
                    Environment.sdkEnvironment.sdkToken = result.data.access_token
                }
            }
        }
    }
}