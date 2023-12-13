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

import com.synapseslab.bluegps_sdk.authentication.data.models.KeyCloakParameters
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

    private const val SDK_ENDPOINT = "{{provided-bluegps-endpoint}}"
    private val APP_ID = "com.synapseslab.demosdk"

    val sdkEnvironment = SdkEnvironment(
        sdkEndpoint = SDK_ENDPOINT,
        appId = APP_ID,
    )

    /**
     * AUTHENTICATION
     *
     * BlueGPS provides 2 kinds of authentication:
     *
     * - User Authentication:
     *
     * If you want only the User authentication you must set the @clientId.
     * This means that for each device this is the user on Keycloak that can manage grants for this particular user.
     *
     * - Guest Authentication:
     *
     * If you want only the Guest authentication, you must set the @guestClientSecret and @guestClientId.
     * This means that we don't have a user that has to login but we use client credentials and there is not an individual user for each app install.
     * Instead BlueGPS treats the user account as a "guest". In this case multiple devices can use the same client credentials to be authenticated and
     * BlueGPS will register the user as a device, and not as a formal Keycloak user.
     */
    val keyCloakParameters = KeyCloakParameters(
        authorization_endpoint = "https://[BASE-URL]/realms/[REALMS]/protocol/openid-connect/auth",
        token_endpoint = "https://[BASE-URL]/realms/[REALMS]/protocol/openid-connect/token",
        redirect_uri = "{{provided-redirect-uri}}",
        clientId = "{{provided-client-secret}}",
        userinfo_endpoint = "https://[BASE-URL]/realms/[REALMS]/protocol/openid-connect/userinfo",
        end_session_endpoint = "https://[BASE-URL]/realms/[REALMS]/protocol/openid-connect/logout",
        guestClientSecret = "{{provided-guest-client-secret}}",
        guestClientId = "{{provided-guest-client-id}}"
    )
}