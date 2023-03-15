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

package com.synapseslab.bluegpssdkdemo.location

import android.Manifest
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.synapseslab.bluegps_sdk.data.model.advertising.AndroidAdvConfiguration
import com.synapseslab.bluegps_sdk.data.model.advertising.ServiceStatus
import com.synapseslab.bluegps_sdk.location.BlueGPSLocationManager
import com.synapseslab.bluegps_sdk.helper.BackgroundPermissionHelper
import com.synapseslab.bluegps_sdk.utils.Resource
import com.synapseslab.bluegpssdkdemo.databinding.ActivityLocationBinding

private const val TAG = "LocationActivity"


class LocationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLocationBinding

    private val viewModel: LocationViewModel = LocationViewModel()

    private lateinit var blueGPSLocationManager: BlueGPSLocationManager

    private var androidAdvConfiguration: AndroidAdvConfiguration? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLocationBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        supportActionBar?.title = "GPS Location"
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowHomeEnabled(true)
        }

        checkBackgroundLocationPermission()

        // initialize BlueGPSLocationManager
        blueGPSLocationManager = BlueGPSLocationManager()
    }

    private fun checkBackgroundLocationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (!BackgroundPermissionHelper.hasBackgroundPermission(this)) {
                val launcher =
                    registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
                        if (isGranted) {
                            Log.d(TAG, "Background Permission granted")
                            // get device configuration and on success start location manager service
                            getDeviceConfiguration()

                        } else {
                            Toast.makeText(
                                this,
                                "Background permission is needed for enhance functionality",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                launcher.launch(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
            }
        }

        getDeviceConfiguration()

        startBtnListener()

        stopBtnListener()

    }


    override fun onResume() {
        super.onResume()

        // add listener
        blueGPSLocationManager.addListener(locationManagerListener)
    }

    override fun onPause() {
        super.onPause()

        // remove listener
        blueGPSLocationManager.removeListener()
    }



    private fun startBtnListener() {
        binding.btnStart.setOnClickListener {

            // start BlueGPS Location Manager service
            blueGPSLocationManager.start(applicationContext, androidAdvConfiguration!!)
        }
    }

    private fun stopBtnListener() {
        binding.btnStop.setOnClickListener {

            // stop BlueGPS Location Manager service
            blueGPSLocationManager.stop(applicationContext)
        }
    }

    private val locationManagerListener = object : BlueGPSLocationManager.LocationStatusListener {
        override fun locationStatusChanged(serviceStatus: ServiceStatus, message: String) {
            when(serviceStatus) {
                ServiceStatus.STARTED -> {
                    Log.d(TAG, "$serviceStatus - $message")
                    binding.tvStatusService.text = "$serviceStatus - $message"
                    binding.btnStop.isEnabled = true
                    binding.btnStart.isEnabled = false
                }
                ServiceStatus.STOPPED -> {
                    Log.d(TAG, "$serviceStatus - $message")
                    binding.tvStatusService.text = "$serviceStatus - $message"
                    binding.btnStart.isEnabled = true
                    binding.btnStop.isEnabled = false
                }
                ServiceStatus.ERROR -> {
                    Log.e(TAG, "$serviceStatus - $message")
                    binding.tvStatusService.text = "$serviceStatus - $message"
                    binding.btnStart.isEnabled = true
                    binding.btnStop.isEnabled = false
                }
            }
        }
    }


    private fun getDeviceConfiguration() {
        viewModel.getDeviceConfiguration {
            when (it) {
                is Resource.Error -> {
                    Log.e(TAG, it.message)
                }
                is Resource.Exception -> {
                    Log.e(TAG, it.e.toString())
                }
                is Resource.Success -> {
                    // on success, start location manager service
                    androidAdvConfiguration = it.data
                    binding.btnStart.isEnabled = true
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item)
    }
}