/*
 * (c) Copyright 2021. All rights reserved by Synapses S.r.l.s.
 * https://www.synapseslab.com/
 *
 * Created by Davide Agostini on 30/11/21, 15:50.
 * Last modified 11/10/21, 15:46
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
            when(val result = BlueGPSLib.instance.getDeviceConfiguration()) {
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