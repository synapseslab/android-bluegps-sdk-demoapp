/*
 * (c) Copyright 2021. All rights reserved by Synapses S.r.l.s.
 * https://www.synapseslab.com/
 *
 * Created by Davide Agostini on 5/13/21 10:59 AM.
 * Last modified 5/13/21 10:59 AM
 */

package com.synapseslab.bluegpssdkdemo.utils

import com.synapseslab.bluegps_sdk.data.model.environment.SdkEnvironment

/**
 * Set the environment for register the SDK to the server.
 * The management of the environment is demanded to the app.
 *
 * SDK_ENDPOINT (mandatory)
 * APP_ID (mandatory)
 *
 */
object Environment {

    private const val APP_ID = "com.synapseslab.demosdk"
    private const val SDK_ENDPOINT = "{BLUEGPS-SDK-ENDPOINT_URL}"
    private const val SDK_KEY = "{BLUEGPS-SDK-KEY}"
    private const val SDK_SECRET = "{BLUEGPS-SDK-SECRET}"


    val sdkEnvironment = SdkEnvironment(
        sdkEndpoint = SDK_ENDPOINT,
        appId = APP_ID,
        sdkKey = SDK_KEY,
        sdkSecret = SDK_SECRET,
    )


    // For JWT Authentication, this credentials are for demo purpose.
    const val USERNAME = ""
    const val PASSWORD = ""

}