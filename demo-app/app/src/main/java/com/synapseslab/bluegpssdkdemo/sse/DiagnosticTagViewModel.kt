package com.synapseslab.bluegpssdkdemo.sse

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.synapseslab.bluegps_sdk.core.BlueGPSLib
import com.synapseslab.bluegps_sdk.data.model.diagnostic.request.DiagnosticSseRequest
import com.synapseslab.bluegps_sdk.data.model.diagnostic.request.response.TrackingSseRequest

private const val TAG = "DiagnosticTagViewModel"

class DiagnosticTagViewModel: ViewModel()  {

    val diagnosticSseRequest =
        DiagnosticSseRequest(TrackingSseRequest(tags = listOf("CFFF00000001", "CFFF00000002")))

    val viewState = MutableLiveData<String>()

    var logText = ""

    fun startDiagnostic() {
        /**
         * SSE API for diagnostic tags
         * For detail info show guide on server sent events section.
         */
        BlueGPSLib.instance.startDiagnostic(
            diagnosticSseRequest = diagnosticSseRequest,
            onComplete = {
                Log.d(TAG, "COMPLETE: $it")
                logText += "COMPLETE: $it\n\n"
                viewState.postValue(logText)
            },
            onTagTracking = {
                Log.d(TAG, "TAG_TRACKING: $it")
                logText += "TAG_TRACKING: $it\n\n"
                viewState.postValue(logText)
            },
            onCheck = {
                Log.d(TAG, "CHECK: $it")
                logText += "CHECK: $it\n\n"
                viewState.postValue(logText)
            }
        )
    }

    fun stopDiagnostic() {
        BlueGPSLib.instance.stopDiagnostic()
    }
}