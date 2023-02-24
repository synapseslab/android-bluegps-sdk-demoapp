/*
 * Copyright (c) 2023 Synapses s.r.l.s. All rights reserved.
 * https://synapseslab.com/
 */

package com.synapseslab.bluegpssdkdemo.keycloak


import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.synapseslab.bluegps_sdk.authentication.core.BlueGPSAuthManager
import com.synapseslab.bluegps_sdk.authentication.presentation.AuthLoginActivity
import com.synapseslab.bluegps_sdk.authentication.presentation.AuthLogoutActivity
import com.synapseslab.bluegpssdkdemo.R
import com.synapseslab.bluegpssdkdemo.databinding.ActivityKeycloakBinding
import com.synapseslab.bluegpssdkdemo.utils.Environment

private const val TAG = "KeycloakActivity"

class KeycloakActivity : AppCompatActivity() {

    private lateinit var binding: ActivityKeycloakBinding

    private val viewModel: KeycloakViewModel = KeycloakViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityKeycloakBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Keycloak Login"
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowHomeEnabled(true)
        }

        viewModel.loginGuestMode.observe(this) { state ->
            if (state != null) {
                binding.tvResponse.text = state
            }
        }
    }


    private val authLoginActivity = registerForActivityResult(AuthLoginActivity()) { result ->
        if (result != null) {
            Log.d(TAG, result.toString())
            binding.tvResponse.text = getString(R.string.successfully_logged_in)

            // set token on the environment
            Environment.sdkEnvironment.sdkToken = result.access_token
        }
    }


    private val authLogoutActivity = registerForActivityResult(AuthLogoutActivity()) { result ->
        if (result != null) {
            Log.d(TAG, result.toString())
            binding.tvResponse.text = result
        }
    }

    fun guestLogin(view: View) {
        viewModel.guestLogin()
    }

    fun login(view: View) {
        authLoginActivity.launch(AuthLoginActivity.LAUNCH_MODE)
    }

    fun refresh(view: View) {
        BlueGPSAuthManager.shared.currentTokenPayload?.let {
            if (!BlueGPSAuthManager.shared.currentTokenPayload!!.refreshTokenValid) {
                viewModel.refreshToken()
            }
        }
    }

    fun logout(view: View) {
        authLogoutActivity.launch(AuthLogoutActivity.LAUNCH_MODE)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish() // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item)
    }
}