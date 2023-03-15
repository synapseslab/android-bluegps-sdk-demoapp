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

package com.synapseslab.bluegpssdkdemo.navigation

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.synapseslab.bluegps_sdk.component.map.BlueGPSMapListener
import com.synapseslab.bluegps_sdk.data.model.map.*
import com.synapseslab.bluegpssdkdemo.R
import com.synapseslab.bluegpssdkdemo.databinding.ActivityNavigationBinding
import com.synapseslab.bluegpssdkdemo.resources.SearchResourceViewModel
import com.synapseslab.bluegpssdkdemo.utils.Environment
import com.synapseslab.bluegpssdkdemo.view.ViewState


private const val TAG = "NavigationActivity"

class NavigationActivity : AppCompatActivity() {


    private lateinit var binding: ActivityNavigationBinding

    private val viewModel: SearchResourceViewModel = SearchResourceViewModel()

    private var source: GenericResource? = null
    private var destination: GenericResource? = null
    private var genericResourceList = listOf<GenericResource>()

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
        binding = ActivityNavigationBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setupSubscribers()
        setListenerOnMapView()

        supportActionBar?.title = "Navigation view"
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowHomeEnabled(true)
        }
        viewModel.findResources(
            isDesc = false,
            order = "name"
        )

        binding.webView.initMap(
            sdkEnvironment = Environment.sdkEnvironment,
            configurationMap = configurationMap
        )
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupSubscribers() {
        viewModel.viewState.observe(this) { state ->
            when (state) {
                is ViewState.Failed -> {
                    Log.d(TAG, " FAILED: ${state.message}")
                }
                is ViewState.Success -> {
                    Log.d(TAG, " SUCCESS: ${state.data}")
                    if (!(state.data as List<GenericResource>).isNullOrEmpty()) {
                        setSpinner(state.data)
                        genericResourceList = state.data
                    }
                }
                else -> {}
            }
        }
    }

    private fun setSpinner(list: List<GenericResource>) {
        val spinnerArrayAdapterFrom = SpinnerAdapter(this, R.layout.item_spinner, list)
        binding.spinnerFrom.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

                source = genericResourceList[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }


        spinnerArrayAdapterFrom.setDropDownViewResource(R.layout.item_spinner)
        binding.spinnerFrom.adapter = spinnerArrayAdapterFrom

        val spinnerArrayAdapterTo = SpinnerAdapter(this, R.layout.item_spinner, list)
        binding.spinnerTo.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                destination = genericResourceList[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }
        spinnerArrayAdapterTo.setDropDownViewResource(R.layout.item_spinner)
        binding.spinnerTo.adapter = spinnerArrayAdapterTo
    }


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
                    else -> {
                    }
                }
            }
        })
    }

    fun startNavigation(view: View) {
        binding.webView.goto(
            source = source?.position!!,
            dest = destination?.position!!,
        )
    }
}