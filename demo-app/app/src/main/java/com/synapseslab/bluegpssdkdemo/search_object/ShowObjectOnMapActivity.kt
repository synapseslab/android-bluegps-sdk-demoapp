/*
* (c) Copyright 2021. All rights reserved by Synapses S.r.l.s.
* https://www.synapseslab.com/
*
* Created by Davide Agostini on 22/06/21, 12:56.
* Last modified 22/06/21, 12:56
*/

package com.synapseslab.bluegpssdkdemo.search_object

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.synapseslab.bluegps_sdk.component.map.BlueGPSMapListener
import com.synapseslab.bluegps_sdk.data.model.map.*
import com.synapseslab.bluegps_sdk.data.model.search_object.TrackElement
import com.synapseslab.bluegpssdkdemo.databinding.ActivityMapBinding
import com.synapseslab.bluegpssdkdemo.utils.Environment


private val TAG = "MapActivity"


class ShowObjectOnMapActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMapBinding

    private var trackElement: TrackElement? = null

    private var configurationMap = ConfigurationMap(
        style = MapStyle(
            navigation = NavigationStyle(
                iconSource = "/api/public/resource/icons/commons/start.svg",
                iconDestination = "/api/public/resource/icons/commons/end.svg",
            ),
            icons = IconStyle(
                name = "saipem",
                align = "center",
                vAlign = "center",
                followZoom = false
            ),
        ),
        show = ShowMap(all = false, room = true, me = true),
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        trackElement = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra("object", TrackElement::class.java)
        } else {
            intent.getParcelableExtra("object")
        }

        supportActionBar?.title = trackElement?.label

        binding.webView.initMap(
            sdkEnvironment = Environment.sdkEnvironment,
            configurationMap = configurationMap
        )

        binding.btnNavigationMode.visibility = View.GONE
        setListenerOnMapView()
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

    /**
     * Setup the listener for BlueGPSMapView in order to implement the code
     * to run when an event click on map occurs.
     */
    private fun setListenerOnMapView() {
        binding.webView.setBlueGPSMapListener(object : BlueGPSMapListener {
            override fun resolvePromise(
                data: JavascriptCallback,
                typeMapCallback: TypeMapCallback
            ) {
                /**
                 * Callback that intercept the click on the map
                 *
                 * @param data the clicked point with all info.
                 * @param typeMapCallback the type of the clicked point.
                 *
                 */
                when (typeMapCallback) {
                    TypeMapCallback.INIT_SDK_COMPLETED -> {
                        runOnUiThread {
                            trackElement?.positionItem?.position?.let {
                                binding.webView.gotoFromMe(trackElement!!.positionItem!!.position!!)
                            }
                        }
                    }
                    TypeMapCallback.SUCCESS -> {
                        val cType = object : TypeToken<GenericInfo>() {}.type
                        val payloadResponse = Gson().fromJson<GenericInfo>(data.payload, cType)
                        payloadResponse.key = data.key
                        Log.d(TAG, " ${payloadResponse} ")
                    }
                    TypeMapCallback.ERROR -> {
                        val cType = object : TypeToken<GenericInfo>() {}.type
                        val payloadResponse = Gson().fromJson<GenericInfo>(data.payload, cType)
                        payloadResponse.key = data.key
                        Snackbar
                            .make(
                                findViewById(android.R.id.content),
                                "${payloadResponse.message}",
                                Snackbar.LENGTH_LONG
                            )
                            .show()
                    }
                    else -> {}
                }
            }
        })
    }


}