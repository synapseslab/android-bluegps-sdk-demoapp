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

package com.synapseslab.bluegpssdkdemo.login

import android.Manifest.permission.BLUETOOTH_ADVERTISE
import android.Manifest.permission.BLUETOOTH_CONNECT
import android.bluetooth.BluetoothAdapter
import android.content.*
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.synapseslab.bluegps_sdk.data.model.advertising.AdvertisingStatus
import com.synapseslab.bluegps_sdk.data.model.advertising.AndroidAdvConfiguration
import com.synapseslab.bluegps_sdk.data.model.advertising.ServiceStatus
import com.synapseslab.bluegps_sdk.data.model.environment.SdkEnvironmentLoggedUser
import com.synapseslab.bluegps_sdk.service.BlueGPSAdvertisingService
import com.synapseslab.bluegpssdkdemo.R
import com.synapseslab.bluegpssdkdemo.databinding.ActivityMainBinding
import com.synapseslab.bluegpssdkdemo.utils.Environment
import com.synapseslab.bluegpssdkdemo.utils.Environment.PASSWORD
import com.synapseslab.bluegpssdkdemo.utils.Environment.USERNAME
import com.synapseslab.bluegpssdkdemo.utils.hide
import com.synapseslab.bluegpssdkdemo.utils.hideKeyboard
import com.synapseslab.bluegpssdkdemo.utils.show
import com.synapseslab.bluegpssdkdemo.view.ViewState
import java.lang.Exception

private const val TAG = "MainActivity"

private const val PERMISSION_REQUEST_CODE = 200

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel = MainViewModel()

    private lateinit var binding: ActivityMainBinding

    private var blueGPSAdvertisingService: BlueGPSAdvertisingService? = null

    private var guestMode = true

    private var androidAdvConfiguration: AndroidAdvConfiguration? = null

    /** Defines callbacks for service binding, passed to bindService()  */
    private val advertisingServiceConnection = object : ServiceConnection {

        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            // We've bound to BlueGPSAdvertisingService, cast the IBinder and get BlueGPSAdvertisingService instance
            val binder = service as BlueGPSAdvertisingService.LocalBinder
            blueGPSAdvertisingService = binder.serviceBlueGPS
        }

        override fun onServiceDisconnected(name: ComponentName) {
            blueGPSAdvertisingService = null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        supportActionBar?.title = "Login View"

        /**
         * Android 12 require new BLUETOOTH_CONNECT and BLUETOOTH_ADVERTISE runtime permissions.
         * You must explicitly request user approval in your app before you can look for Bluetooth devices.
         */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (checkPermission()) {
                Toast.makeText(applicationContext, "Permission already granted", Toast.LENGTH_LONG)
                    .show()
                startBluetooth()
            } else {
                requestPermission()
            }
        } else {
            startBluetooth()
        }

        fillJwtMode()
        addListenerOnButton()
        setupSubscribers()
        getDeviceConfiguration()

        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowHomeEnabled(true)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onStart() {
        super.onStart()

        // Bind to BlueGPSAdvertisingService
        val serviceIntent = Intent(this, BlueGPSAdvertisingService::class.java)
        bindService(
            serviceIntent,
            advertisingServiceConnection,
            Context.BIND_AUTO_CREATE
        )
    }

    override fun onResume() {
        super.onResume()

        // register advertising service receiver
        registerReceiver(
            advertisingServiceReceiver,
            IntentFilter(BlueGPSAdvertisingService.ACTION_ADV)
        )
    }

    override fun onPause() {
        super.onPause()

        // unregister advertising service receiver
        unregisterReceiver(advertisingServiceReceiver)
    }

    override fun onStop() {
        super.onStop()
        unbindService(advertisingServiceConnection)
    }


    private fun startBluetooth() {
        try {
            val bluetoothAdapter: BluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
            if (!bluetoothAdapter.isEnabled) {
                if (ActivityCompat.checkSelfPermission(
                        this,
                        BLUETOOTH_CONNECT
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return
                }
                bluetoothAdapter.enable()
                Toast.makeText(this, "Bluetooth is enabled", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun fillJwtMode() {
        binding.username.setText(USERNAME)
        binding.password.setText(PASSWORD)
    }


    /**
     * Update the UI with the response server.
     * If Success print the token otherwise show the response error.
     */
    private fun setupSubscribers() {
        viewModel.viewState.observe(this) { state ->
            when (state) {
                is ViewState.Failed -> {
                    //Log.d(TAG, " FAILED: ${state.message}")
                    binding.token.text = state.message
                    binding.token.setTextColor(
                        ContextCompat.getColor(
                            applicationContext,
                            R.color.red
                        )
                    )
                    binding.inLoadingLayout.loading.hide()
                }
                is ViewState.Loading -> {
                    binding.inLoadingLayout.loading.show()
                }
                is ViewState.Success -> {
                    //Log.d(TAG, " SUCCESS: ${state.data}")
                    binding.token.text = "${state.data}"
                    binding.token.setTextColor(
                        ContextCompat.getColor(
                            applicationContext,
                            R.color.black
                        )
                    )
                    binding.inLoadingLayout.loading.hide()
                }
            }
        }
    }

    private fun getDeviceConfiguration() {
        viewModel.getDeviceConfiguration()

        viewModel.deviceConfiguration.observe(this) { state ->
            when(state) {
                is ViewState.Failed -> {
                    Log.d(TAG, " FAILED: ${state.message}")
                }
                is ViewState.Success -> {
                    androidAdvConfiguration = state.data as AndroidAdvConfiguration
                    binding.buttonStartService.isEnabled = true
                }
                else -> {}
            }
        }
    }

    /**
     * Listener on two authentication method.
     * Guest mode or Username and Password authentication
     *
     */
    private fun addListenerOnButton() {
        binding.radioGroup.setOnCheckedChangeListener { _, checkedId ->
            run {
                when (checkedId) {
                    binding.radioGuest.id -> {
                        binding.username.hide()
                        binding.password.hide()
                        binding.tvAuthentication.text = getString(R.string.guest_authentication)
                        guestMode = true
                    }
                    binding.radioJwt.id -> {
                        binding.tvAuthentication.text = getString(R.string.jwt_authentication)
                        binding.username.show()
                        binding.password.show()
                        guestMode = false
                    }
                }
            }
        }
    }

    fun loginGuestMode(view: View) {

        if (guestMode) {
            Environment.sdkEnvironment.loggedUser = null
        } else {
            if (binding.username.text.isNullOrEmpty() || binding.password.text.isNullOrEmpty()) return

            Environment.sdkEnvironment.loggedUser = SdkEnvironmentLoggedUser(
                username = binding.username.text.toString(),
                password = binding.password.text.toString()
            )
        }

        login()
    }

    /**
     * Register the SDK to the server.
     */
    private fun login() {
        hideKeyboard()
        viewModel.registerSDK(Environment.sdkEnvironment)
    }


    private fun updateServiceButton(value: Boolean) {
        binding.buttonStartService.isEnabled = value
        binding.buttonStopService.isEnabled = !value
    }

    /**
     * Start the Service Advertising
     * The Library print the tag assigned and advMode and advTxPower
     */
    fun startAdvertising(view: View) {

        //blueGPSAdvertisingService?.startAdv()

        blueGPSAdvertisingService?.startAdv(androidAdvConfiguration = androidAdvConfiguration!!)

        updateServiceButton(true)
    }

    /**
     * Stop the Service Advertising
     */
    fun stopAdvertising(view: View) {
        blueGPSAdvertisingService?.stopAdv()

        updateServiceButton(false)
    }


    /**
     * [OPTIONAL]
     * Subclass BroadcastReceiver and implement onReceive(Context, Intent) method
     * The broadcast receiver logs and displays the contents of the broadcast:
     *
     * Service STARTED, Service STOPPED or Service ERROR
     *
     */
    private val advertisingServiceReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == BlueGPSAdvertisingService.ACTION_ADV) {
                val advertisingStatus = if (Build.VERSION.SDK_INT >= 33) {
                    intent.getParcelableExtra(BlueGPSAdvertisingService.DATA_ADV, AdvertisingStatus::class.java)
                } else {
                    intent.getParcelableExtra(BlueGPSAdvertisingService.DATA_ADV)
                }
                advertisingStatus?.let {

                        //Log.d(TAG, "- Service ${it.status} ${it.message}")

                        when (it.status) {
                            ServiceStatus.STARTED -> {
                                updateServiceButton(false)
                                binding.statusService.setTextColor(
                                    ContextCompat.getColor(
                                        applicationContext,
                                        R.color.green
                                    )
                                )
                            }
                            ServiceStatus.STOPPED -> {
                                updateServiceButton(true)
                                binding.statusService.setTextColor(
                                    ContextCompat.getColor(
                                        applicationContext,
                                        R.color.black
                                    )
                                )
                            }
                            ServiceStatus.ERROR -> {
                                updateServiceButton(true)
                                binding.statusService.setTextColor(
                                    ContextCompat.getColor(
                                        applicationContext,
                                        R.color.red
                                    )
                                )
                            }
                        }

                        binding.statusService.text = "- Service ${it.status} ${it.message}"
                    }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun checkPermission(): Boolean {
        val permissionAdvertise =
            ContextCompat.checkSelfPermission(applicationContext, BLUETOOTH_ADVERTISE)
        val permissionScan =
            ContextCompat.checkSelfPermission(applicationContext, BLUETOOTH_CONNECT)

        return permissionAdvertise == PackageManager.PERMISSION_GRANTED && permissionScan == PackageManager.PERMISSION_GRANTED
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(BLUETOOTH_ADVERTISE, BLUETOOTH_CONNECT),
            PERMISSION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty()) {
                    val advertiseAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED
                    val scanAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED

                    if (!advertiseAccepted && !scanAccepted) {
                        Snackbar.make(
                            findViewById(android.R.id.content),
                            "Permission denied, You cannot access bluetooth advertise and scan",
                            Snackbar.LENGTH_LONG
                        ).show()

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                            if (shouldShowRequestPermissionRationale(BLUETOOTH_ADVERTISE)) {
                                showMessageOKCancel(
                                    "You need to allow access to both the permissions"
                                ) { dialog, which ->
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                                        requestPermissions(
                                            arrayOf(
                                                BLUETOOTH_ADVERTISE,
                                                BLUETOOTH_CONNECT
                                            ),
                                            PERMISSION_REQUEST_CODE
                                        )
                                    }
                                }
                                return
                            }
                        }
                    } else {
                        startBluetooth()
                    }
                }
            }
        }
    }

    private fun showMessageOKCancel(message: String, okListener: DialogInterface.OnClickListener) {
        AlertDialog.Builder(this@MainActivity)
            .setMessage(message)
            .setPositiveButton("OK", okListener)
            .setNegativeButton("Cancel", null)
            .create()
            .show()
    }
}