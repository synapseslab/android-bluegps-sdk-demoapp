/*
 * (c) Copyright 2021. All rights reserved by Synapses S.r.l.s.
 * https://www.synapseslab.com/
 *
 * Created by Davide Agostini on 29/06/21, 11:00.
 * Last modified 29/06/21, 11:00
 */

package com.synapseslab.bluegpssdkdemo.home

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.synapseslab.bluegps_sdk.data.model.diagnostic.request.DiagnosticSseRequest
import com.synapseslab.bluegps_sdk.data.model.diagnostic.request.response.TrackingSseRequest
import com.synapseslab.bluegpssdkdemo.sse.DiagnosticTagActivity
import com.synapseslab.bluegpssdkdemo.sse.NotifyRegionActivity
import com.synapseslab.bluegpssdkdemo.R
import com.synapseslab.bluegpssdkdemo.BuildConfig
import com.synapseslab.bluegpssdkdemo.area.AreaActivity
import com.synapseslab.bluegpssdkdemo.databinding.ActivityHomeBinding
import com.synapseslab.bluegpssdkdemo.login.MainActivity
import com.synapseslab.bluegpssdkdemo.map.MapActivity
import com.synapseslab.bluegpssdkdemo.navigation.NavigationActivity
import com.synapseslab.bluegpssdkdemo.resources.SearchResourcesActivity
import com.synapseslab.bluegpssdkdemo.search_object.SearchObjectsActivity
import com.synapseslab.bluegpssdkdemo.sse.NotifyPositionActivity


private const val TAG = "HomeActivity"

class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private var btManager: BluetoothManager? = null
    private var btAdapter: BluetoothAdapter? = null

    private lateinit var activityListAdapter: ActivityViewAdapter

    val diagnosticSseRequest =
        DiagnosticSseRequest(TrackingSseRequest(tags = listOf("CFFF00000001", "CFFF00000002")))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        startBluetooth()

        activityListAdapter =
            ActivityViewAdapter(mutableListOf()) { clssActivity -> openView(clssActivity) }
        binding.list.adapter = activityListAdapter
        activityListAdapter.setList(activityList)
        binding.tvVersion.text = "ver. ${BuildConfig.VERSION_NAME}"

    }

    private fun openView(view: ViewActivity) {
        startActivity(Intent(this, view.cls))
    }

    private fun startBluetooth() {
        if (packageManager.hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            btManager = getSystemService(BLUETOOTH_SERVICE) as BluetoothManager
            btAdapter = btManager!!.adapter
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (checkPermission()) {
                btAdapter?.enable()
            } else {
                requestPermission()
            }
        } else {
            btAdapter?.enable()
        }
    }


    /**
     * Android 12 Bluetooth runtime permission
     */
    @RequiresApi(Build.VERSION_CODES.S)
    private fun checkPermission(): Boolean {
        val permissionAdvertise =
            ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.BLUETOOTH_ADVERTISE
            )
        val permissionScan = ContextCompat.checkSelfPermission(
            applicationContext,
            Manifest.permission.BLUETOOTH_CONNECT
        )

        return permissionAdvertise == PackageManager.PERMISSION_GRANTED && permissionScan == PackageManager.PERMISSION_GRANTED
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.BLUETOOTH_ADVERTISE, Manifest.permission.BLUETOOTH_CONNECT),
            PERMISSION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        Log.d(TAG, "onRequestPermissionResult")

        if (requestCode == PERMISSION_REQUEST_CODE) {
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
                        if (shouldShowRequestPermissionRationale(Manifest.permission.BLUETOOTH_ADVERTISE)) {
                            showMessageOKCancel(
                                "You need to allow access to both the permissions"
                            ) { dialog, which ->
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                                    requestPermissions(
                                        arrayOf(
                                            Manifest.permission.BLUETOOTH_ADVERTISE,
                                            Manifest.permission.BLUETOOTH_CONNECT
                                        ),
                                        PERMISSION_REQUEST_CODE
                                    )
                                }
                            }
                            return
                        }
                    }
                } else {
                    if (ActivityCompat.checkSelfPermission(
                            this,
                            Manifest.permission.BLUETOOTH_CONNECT
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return
                    }
                    btAdapter?.enable()
                }
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    private fun showMessageOKCancel(message: String, okListener: DialogInterface.OnClickListener) {
        AlertDialog.Builder(this@HomeActivity)
            .setMessage(message)
            .setPositiveButton(getString(R.string.ok), okListener)
            .setNegativeButton(getString(R.string.cancel), null)
            .create()
            .show()
    }

    companion object {
        private const val PERMISSION_REQUEST_CODE = 1337
        private val activityList = mutableListOf(
            ViewActivity(
                title = "Login view",
                icon = R.drawable.ic_login,
                cls = MainActivity::class.java
            ),
            ViewActivity(
                title = "Map View",
                icon = R.drawable.ic_map,
                cls = MapActivity::class.java
            ),
            ViewActivity(
                title = "Search Resources View",
                icon = R.drawable.ic_search,
                cls = SearchResourcesActivity::class.java
            ),
            ViewActivity(
                title = "Search Objects View",
                icon = R.drawable.ic_search_object,
                cls = SearchObjectsActivity::class.java
            ),
            ViewActivity(
                title = "Area utilities",
                icon = R.drawable.ic_api,
                cls = AreaActivity::class.java
            ),
            ViewActivity(
                title = "Navigate view",
                icon = R.drawable.ic_navigation,
                cls = NavigationActivity::class.java
            ),
            ViewActivity(
                title = "Diagnostic tags",
                icon = R.drawable.ic_blur,
                cls = DiagnosticTagActivity::class.java
            ),
            ViewActivity(
                title = "Notify region changes",
                icon = R.drawable.ic_location,
                cls = NotifyRegionActivity::class.java
            ),
            ViewActivity(
                title = "Notify position changes",
                icon = R.drawable.ic_person_pin,
                cls = NotifyPositionActivity::class.java
            ),
        )
    }
}