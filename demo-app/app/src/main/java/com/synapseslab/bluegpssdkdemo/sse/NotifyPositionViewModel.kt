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

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.synapseslab.bluegps_sdk.core.BlueGPSLib
import com.synapseslab.bluegps_sdk.core.startNotifyPositionChanges
import com.synapseslab.bluegps_sdk.core.stopNotifyPositionChanges

class NotifyPositionViewModel: ViewModel() {

    var logText = ""
    val viewState = MutableLiveData<String>()

    init {
        startNotifyPositionChanges()
    }

    private fun startNotifyPositionChanges() {
        BlueGPSLib.instance.startNotifyPositionChanges {
            logText += "$it\n\n"
            viewState.postValue(logText)
        }
    }

    fun stopNotifyPositionChanges() {
        BlueGPSLib.instance.stopNotifyPositionChanges()
    }

}