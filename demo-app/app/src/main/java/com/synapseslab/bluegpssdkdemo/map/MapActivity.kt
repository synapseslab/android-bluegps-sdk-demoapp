/*
 * (c) Copyright 2021. All rights reserved by Synapses S.r.l.s.
 * https://www.synapseslab.com/
 *
 * Created by Davide Agostini on 30/11/21, 15:50.
 * Last modified 02/11/21, 15:49
 */

package com.synapseslab.bluegpssdkdemo.map

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.transition.Fade
import android.transition.TransitionManager
import android.util.Log
import android.view.*
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContract
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.slider.Slider
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.synapseslab.bluegps_sdk.component.map.BlueGPSMapListener
import com.synapseslab.bluegps_sdk.data.model.advertising.AndroidAdvConfiguration
import com.synapseslab.bluegps_sdk.data.model.map.*
import com.synapseslab.bluegps_sdk.data.model.stats.NavInfo
import com.synapseslab.bluegps_sdk.data.model.stats.NavigationStats
import com.synapseslab.bluegpssdkdemo.R
import com.synapseslab.bluegpssdkdemo.databinding.ActivityMapBinding
import com.synapseslab.bluegpssdkdemo.utils.Environment
import com.synapseslab.bluegpssdkdemo.utils.hide
import com.synapseslab.bluegpssdkdemo.utils.show
import com.synapseslab.bluegpssdkdemo.view.ViewState

private val TAG = "MapActivity"

class MapActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMapBinding

    private var toolbarView: View? = null

    private val viewModel: MapViewModel = MapViewModel()

    private var configurationMap = ConfigurationMap(
        style = MapStyle(
            icons = IconStyle(
                name = "chorus",
                align = "center",
                vAlign = "center",
                followZoom = true
            ),
            navigation = NavigationStyle(
                iconSource = "/api/public/resource/icons/commons/start.svg",
                iconDestination = "/api/public/resource/icons/commons/end.svg",
                velocityOptions = mutableMapOf("foot" to 4.0, "bike" to 10.0),
                navigationStep = 1.5,
                autoZoom = true,
                showVoronoy = false
            )
        ),
        show = ShowMap(all = false, me = true, room = true),
    )

    private var hideRoomLayer = false

    private var navigationMode = false

    private lateinit var source: Position

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        supportActionBar?.title = "Map View"
        tapViewClickListener()

        setListenerOnMapView()
        setOnNavigationModeButtonListener()
        setOnGoToClickListener()
        getDeviceConfiguration()

        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowHomeEnabled(true)
        }
    }

    /**
     * Check the configuration mode of the smartphone and change the style of the map.
     */
    private fun adjustDarkMode() {
        when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> binding.webView.setDarkMode(true)
            Configuration.UI_MODE_NIGHT_NO -> binding.webView.setDarkMode(false)
            else -> binding.webView.setDarkMode(false)
        }
    }

    private fun startInitMap() {
        /**
         * The BlueGPSMapView component expose an initMap method for initialize the web view
         * with the required parameters and load the start url. [ *baseURL* + api/public/resource/sdk/mobile.html]
         */
        binding.webView.initMap(
            sdkEnvironment = Environment.sdkEnvironment,
            configurationMap = configurationMap
        )
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
                    // triggered when init sdk is completed
                    TypeMapCallback.INIT_SDK_COMPLETED -> {
                        runOnUiThread {
                            adjustDarkMode()
                        }
                    }
                    TypeMapCallback.PARK_CONF -> {
                        val cType = object : TypeToken<BookingConfiguration>() {}.type
                        val payloadResponse = Gson().fromJson<BookingConfiguration>(data.payload, cType)
                        if (payloadResponse.availableDateList!!.isNotEmpty()) {
                            Log.d(TAG, TAG + payloadResponse.availableDateList)
                        }
                    }
                    TypeMapCallback.MAP_CLICK, TypeMapCallback.TAG_CLICK -> {
                        val cType = object : TypeToken<Position>() {}.type
                        val payloadResponse = Gson().fromJson<Position>(data.payload, cType)
                        if (navigationMode) {
                            runOnUiThread {
                                source = payloadResponse
                                binding.tvDestination.text =
                                    "Destination: (${(payloadResponse.x.toString()).take(6)}, ${
                                        (payloadResponse.y.toString()).take(6)
                                    })"
                                showHideLayoutDestination(true)
                            }
                        } else {

                            payloadResponse.roomId?.let {
                                MaterialAlertDialogBuilder(this@MapActivity)
                                    .setTitle("Type: ${typeMapCallback.name}")
                                    .setMessage(payloadResponse.toString())
                                    .setPositiveButton("Ok") { dialog, _ ->
                                        dialog.dismiss()
                                    }
                                    .setNeutralButton("Draw pin") { dialog, _ ->
                                        runOnUiThread {
                                            binding.webView.drawPin(payloadResponse)
                                        }

                                        dialog.dismiss()
                                    }
                                    .setNegativeButton("Center") { dialog, _ ->
                                        runOnUiThread {
                                            binding.webView.centerToRoom(payloadResponse.roomId!!);
                                        }
                                        dialog.dismiss()
                                    }
                                    .show()

                                return
                            }


                            payloadResponse.tagid?.let {
                                MaterialAlertDialogBuilder(this@MapActivity)
                                    .setTitle("Type: ${typeMapCallback.name}")
                                    .setMessage(payloadResponse.toString())
                                    .setPositiveButton("Ok") { dialog, _ ->
                                        dialog.dismiss()
                                    }
                                    .setNegativeButton("Center") { dialog, _ ->
                                        runOnUiThread {
                                            binding.webView.centerToPosition(payloadResponse, 5.0);
                                        }
                                        dialog.dismiss()
                                    }
                                    .setNeutralButton("Draw pin") { dialog, _ ->
                                        runOnUiThread {
                                            binding.webView.drawPin(payloadResponse)
                                        }

                                        dialog.dismiss()
                                    }
                                    .show()

                                return
                            }
                        }
                    }

                    // triggered if a bookable resource is clicked
                    TypeMapCallback.BOOKING_CLICK -> {
                        val cType = object : TypeToken<ClickedObject>() {}.type
                        val payloadResponse = Gson().fromJson<ClickedObject>(data.payload, cType)
                        MaterialAlertDialogBuilder(this@MapActivity)
                            .setTitle("Type: ${typeMapCallback.name}")
                            .setMessage(payloadResponse.toString())
                            .setPositiveButton("Ok") { dialog, _ ->
                                dialog.dismiss()
                            }
                            .show()
                    }

                    // triggered on any navigation update
                    TypeMapCallback.NAV_STATS -> {
                        val cType = object : TypeToken<NavigationStats>() {}.type
                        val payloadResponse = Gson().fromJson<NavigationStats>(data.payload, cType)
                        Log.d(TAG, " $TAG $payloadResponse ")
                        var vehicles: String? = ""
                        payloadResponse.vehicles?.let {
                            payloadResponse.vehicles!!.map { v ->
                                vehicles += "${v.name}: ${Math.round(v.remainingTimeSecond!! * 100) / 100.0}s\n"
                            }
                        }

                        runOnUiThread {
                            binding.tvRemaining.text =
                                "Remaining distance: ${Math.round(payloadResponse.remainingDistance!! * 100) / 100.0}m \n$vehicles"
                        }
                    }

                    // triggered on navigation mode when tag is proximity to specific points
                    TypeMapCallback.NAV_INFO -> {
                        val cType = object : TypeToken<NavInfo>() {}.type
                        val payloadResponse = Gson().fromJson<NavInfo>(data.payload, cType)
                        Snackbar
                            .make(
                                findViewById(android.R.id.content),
                                "${payloadResponse.message}",
                                Snackbar.LENGTH_LONG
                            )
                            .show()
                    }

                    // triggered when loadGenericResource is called
                    TypeMapCallback.RESORUCE -> {
                        val cType = object : TypeToken<DataFilter>() {}.type
                        val payloadResponse = Gson().fromJson<DataFilter>(data.payload, cType)
                        Log.d(TAG, " $TAG $payloadResponse ")
                        receiveSelectedResource.launch(payloadResponse)
                    }

                    // triggered if ConfigurationMap.tagid is present and the tag change its visibility status
                    TypeMapCallback.TAG_VISIBILITY -> {
                        val cType = object : TypeToken<TagVisibility>() {}.type
                        val payloadResponse = Gson().fromJson<TagVisibility>(data.payload, cType)
                        Log.d(TAG, " $TAG $payloadResponse ")
                    }

                    // triggered when user enter a room
                    TypeMapCallback.ROOM_ENTER -> {
                        val cType = object : TypeToken<Position>() {}.type
                        val payloadResponse = Gson().fromJson<Position>(data.payload, cType)
                        Log.d(TAG, " $TAG $payloadResponse ")
                    }

                    // triggered when user exit a room
                    TypeMapCallback.ROOM_EXIT -> {
                        val cType = object : TypeToken<Position>() {}.type
                        val payloadResponse = Gson().fromJson<Position>(data.payload, cType)
                        Log.d(TAG, " $TAG $payloadResponse ")
                    }

                    // triggered when the user change floor
                    TypeMapCallback.FLOOR_CHANGE -> {
                        val cType = object : TypeToken<Floor>() {}.type
                        val payloadResponse = Gson().fromJson<Floor>(data.payload, cType)
                        Log.d(TAG, " $TAG $payloadResponse ")
                    }

                    // triggered when a generic async action end with success
                    TypeMapCallback.SUCCESS -> {
                        val cType = object : TypeToken<GenericInfo>() {}.type
                        val payloadResponse = Gson().fromJson<GenericInfo>(data.payload, cType)
                        payloadResponse.key = data.key
                        Log.d(TAG, " ${payloadResponse.message} ")
                    }

                    // triggered when a generic async action end with error
                    TypeMapCallback.ERROR -> {
                        val cType = object : TypeToken<GenericInfo>() {}.type
                        val payloadResponse = Gson().fromJson<GenericInfo>(data.payload, cType)
                        payloadResponse.key = data.key
                        Log.e(TAG, TAG + " ${payloadResponse.message} ")
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


    private fun getDeviceConfiguration() {
        viewModel.getDeviceConfiguration()

        viewModel.viewState.observe(this) { state ->
            when(state) {
                is ViewState.Failed -> {
                    Log.d(TAG, " FAILED: ${state.message}")
                }
                is ViewState.Success -> {
                    val androidAdvConfiguration = state.data as AndroidAdvConfiguration
                    this.configurationMap.tagid = androidAdvConfiguration.tagid
                    startInitMap()
                }
                else -> {}
            }
        }
    }

    /**
     * Toolbox GUI for configure and change the map control layer.
     * This demo show only some functions. Look at the documentation for all available methods.
     */
    private fun showToolbarView() {
        if (toolbarView != null && toolbarView!!.isShown) {
            return
        }

        toolbarView =
            LayoutInflater.from(this).inflate(R.layout.toolbar_map_view, binding.mapView, false)
        TransitionManager.beginDelayedTransition(binding.mapView, Fade())
        binding.mapView.addView(toolbarView)
        binding.tapView.visibility = View.VISIBLE


        val switchStatus: SwitchMaterial = toolbarView!!.findViewById(R.id.switchStatus)
        switchStatus.isChecked = configurationMap.toolbox?.mapControl?.enabled ?: true
        switchStatus.setOnCheckedChangeListener { _, isChecked ->
            configurationMap.toolbox?.mapControl?.enabled = isChecked
            binding.webView.updateConfigurationMap(configurationMap)
        }


        val btnHorizontal: Button = toolbarView!!.findViewById(R.id.btnHorizontal)
        val btnVertical: Button = toolbarView!!.findViewById(R.id.btnVertical)

        if (configurationMap.toolbox!!.mapControl!!.orientation!! == OrientationType.horizontal) {
            btnHorizontal.backgroundTintList =
                ContextCompat.getColorStateList(this, R.color.blue_500)
            btnVertical.backgroundTintList = ContextCompat.getColorStateList(this, R.color.grey)
        } else {
            btnHorizontal.backgroundTintList = ContextCompat.getColorStateList(this, R.color.grey)
            btnVertical.backgroundTintList = ContextCompat.getColorStateList(this, R.color.blue_500)
        }

        btnHorizontal.setOnClickListener {
            btnHorizontal.backgroundTintList =
                ContextCompat.getColorStateList(this, R.color.blue_500)
            btnVertical.backgroundTintList = ContextCompat.getColorStateList(this, R.color.grey)
            configurationMap.toolbox?.mapControl?.orientation = OrientationType.horizontal
            binding.webView.updateConfigurationMap(configurationMap)
        }

        btnVertical.setOnClickListener {
            btnVertical.backgroundTintList = ContextCompat.getColorStateList(this, R.color.blue_500)
            btnHorizontal.backgroundTintList = ContextCompat.getColorStateList(this, R.color.grey)
            configurationMap.toolbox?.mapControl?.orientation = OrientationType.vertical
            binding.webView.updateConfigurationMap(configurationMap)
        }


        val sliderButtonWidth: Slider = toolbarView!!.findViewById(R.id.sliderButtonWidth)
        sliderButtonWidth.value = configurationMap.toolbox?.mapControl?.buttonWidth!!.toFloat()
        sliderButtonWidth.addOnSliderTouchListener(object : Slider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: Slider) {
            }

            override fun onStopTrackingTouch(slider: Slider) {
                configurationMap.toolbox?.mapControl?.buttonWidth = slider.value.toInt()
                binding.webView.updateConfigurationMap(configurationMap)
            }
        })

        val sliderButtonHeight: Slider = toolbarView!!.findViewById(R.id.sliderButtonHeight)
        sliderButtonHeight.value = configurationMap.toolbox?.mapControl?.buttonHeight!!.toFloat()
        sliderButtonHeight.addOnSliderTouchListener(object : Slider.OnSliderTouchListener {
            override fun onStartTrackingTouch(slider: Slider) {
            }

            override fun onStopTrackingTouch(slider: Slider) {
                configurationMap.toolbox?.mapControl?.buttonHeight = slider.value.toInt()
                binding.webView.updateConfigurationMap(configurationMap)
            }
        })

        val actionNextFloor: Button = toolbarView!!.findViewById(R.id.actionNextFloor)
        actionNextFloor.setOnClickListener {
            binding.webView.nextFloor()

        }

        val actionResetView: Button = toolbarView!!.findViewById(R.id.actionResetView)
        actionResetView.setOnClickListener {
            binding.webView.resetView()

            binding.mapView.removeView(toolbarView)
        }

        val loadResourceView: Button = toolbarView!!.findViewById(R.id.actionLoadResource)
        loadResourceView.setOnClickListener {

            binding.webView.loadGenericResource()

            binding.mapView.removeView(toolbarView)
        }


        val actionCurrentFloor: Button = toolbarView!!.findViewById(R.id.actionCurrentFloor)
        actionCurrentFloor.setOnClickListener {
            binding.webView.getCurrentFloor { result, error ->
                error?.let {
                    Log.e(TAG, "$error")
                } ?: run {
                    Log.d(TAG, result.toString())
                    Snackbar
                        .make(
                            findViewById(android.R.id.content),
                            "$result",
                            Snackbar.LENGTH_LONG
                        )
                        .show()
                }
            }
        }

        val actionHideRoomLayer: Button = toolbarView!!.findViewById(R.id.actionHideRoomLayer)
        if (!hideRoomLayer) actionHideRoomLayer.setText(R.string.hide_layer)
        else actionHideRoomLayer.setText(R.string.show_layer)
        actionHideRoomLayer.setOnClickListener {
            hideRoomLayer = !hideRoomLayer
            binding.webView.hideRoomLayer(hideRoomLayer)

            if (!hideRoomLayer) actionHideRoomLayer.setText(R.string.hide_layer)
            else actionHideRoomLayer.setText(R.string.show_layer)

            binding.mapView.removeView(toolbarView)
        }

        val actionGetFloor: Button = toolbarView!!.findViewById(R.id.actionGetFloor)
        actionGetFloor.setOnClickListener {
            binding.webView.getFloor { result, error ->

                error?.let {
                    Log.e(TAG, "$error")
                } ?: run {
                    MaterialAlertDialogBuilder(this@MapActivity)
                        .setTitle("Floor list")
                        .setMessage(result.toString())
                        .setPositiveButton("Ok") { dialog, _ ->
                            dialog.dismiss()
                        }
                        .show()
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.settings_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.settings -> {
                showToolbarView()
                true
            }
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun tapViewClickListener() {
        binding.tapView.setOnClickListener {
            if (toolbarView != null && toolbarView!!.isShown) {
                binding.mapView.removeView(toolbarView)
            }

            binding.tapView.visibility = View.GONE
        }
    }

    private fun setOnNavigationModeButtonListener() {
        binding.btnNavigationMode.setOnClickListener {
            navigationMode = !navigationMode

            if (navigationMode) {
                binding.btnNavigationMode.backgroundTintList =
                    ContextCompat.getColorStateList(this, R.color.blue_500)
            } else {
                binding.btnNavigationMode.backgroundTintList =
                    ContextCompat.getColorStateList(this, R.color.grey)

                showHideLayoutDestination(false)
                binding.tvDestination.text = ""
                binding.tvRemaining.text = ""
                binding.webView.removeNavigation()
            }
        }
    }

    private fun setOnGoToClickListener() {

        binding.btnGoTo.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                binding.webView.gotoFromMe(source, true)
            }
        })
    }

    private fun showHideLayoutDestination(visibility: Boolean) {
        if (visibility) binding.layoutDestination.show() else binding.layoutDestination.hide()
    }

    private val receiveSelectedResource = registerForActivityResult(ResourceSelectPoi()) { result ->
        if (result != null) {

            binding.webView.selectPoi(result)

            //binding.webView.selectPoiById(result.id!!)
        }
    }
}

class ResourceSelectPoi : ActivityResultContract<DataFilter, GenericResource?>() {

    override fun createIntent(context: Context, input: DataFilter) =
        Intent(context, ResourceActivity::class.java).apply {
            putExtra("resourceList", input)
        }

    override fun parseResult(resultCode: Int, result: Intent?) : GenericResource? {
        if (resultCode != Activity.RESULT_OK) {
            return null
        }

        if (Build.VERSION.SDK_INT >= 33) {
            return result?.getParcelableExtra("resource", GenericResource::class.java)
        } else {
            return result?.getParcelableExtra("resource")
        }
    }
}