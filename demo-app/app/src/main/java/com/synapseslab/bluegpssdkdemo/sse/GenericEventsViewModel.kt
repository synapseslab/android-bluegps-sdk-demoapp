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
import com.synapseslab.bluegps_sdk.core.startNotifyEventChanges
import com.synapseslab.bluegps_sdk.core.stopNotifyEventChanges
import com.synapseslab.bluegps_sdk.data.model.sse.RequestFilter
import com.synapseslab.bluegps_sdk.data.model.sse.RequestUpdate
import com.synapseslab.bluegps_sdk.data.model.sse.StreamType
import com.synapseslab.bluegps_sdk.data.model.sse.SubscriptionRequest

private const val TAG = "GenericViewModel"

class GenericEventsViewModel : ViewModel() {

    val viewState = MutableLiveData<String>()

    var logText = ""

    fun start() {
        /**
         * Accessory method for activate the notify generic event.
         * If a previously job is active, its stopped and a new job started with the new configuration.
         *
         * @param subscriptionRequest [SubscriptionRequest]
         * @param callbackHandler returns a generic event
         * @param onStop callback when the connection with server is closed
         */
        BlueGPSLib.instance.startNotifyEventChanges(
            subscriptionRequest = SubscriptionRequest(
                filter = RequestFilter(
                    streamType = StreamType.TAGID_EVENT,
                    outputEvents = listOf("TAGID_EVENT")
                ),
                update = RequestUpdate()
            ),
            callbackHandler = {
                logText += "$it\n\n"
                viewState.postValue(logText)
            },
            onStop = {

            }
        )
    }

    fun stop() {
        BlueGPSLib.instance.stopNotifyEventChanges()
    }
}