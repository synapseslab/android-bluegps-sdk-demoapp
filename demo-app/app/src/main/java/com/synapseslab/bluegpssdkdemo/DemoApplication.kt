/*
 * (c) Copyright 2021. All rights reserved by Synapses S.r.l.s.
 * https://www.synapseslab.com/
 *
 * Created by Davide Agostini on 5/12/21 10:13 AM.
 * Last modified 5/12/21 10:13 AM
 */

package com.synapseslab.bluegpssdkdemo

import android.app.Application
import com.synapseslab.bluegps_sdk.core.BlueGPSLib
import com.synapseslab.bluegpssdkdemo.utils.Environment

class DemoApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        /**
         * Initialize BlueGPS SDK
         *
         * @param blueGPSEnvironment set the environment for register the SDK to the server.
         * The management of the environment is demanded to the app.
         * @param context the context of the app.
         * @param enableNetworkLogs for enabled the network logs. [optionally]
         *
         */
        BlueGPSLib.instance.initSDK(
            sdkEnvironment = Environment.sdkEnvironment,
            context = this,
            enabledNetworkLogs = true
        )
    }

}