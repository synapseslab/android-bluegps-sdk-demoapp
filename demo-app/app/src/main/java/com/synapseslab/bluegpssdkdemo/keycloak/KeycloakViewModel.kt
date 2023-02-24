/*
 * Copyright (c) 2023 Synapses s.r.l.s. All rights reserved.
 * https://synapseslab.com/
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