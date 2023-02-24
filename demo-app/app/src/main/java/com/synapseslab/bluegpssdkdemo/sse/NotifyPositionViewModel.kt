/*
 * Copyright (c) 2023 Synapses s.r.l.s. All rights reserved.
 * https://synapseslab.com/
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