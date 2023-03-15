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