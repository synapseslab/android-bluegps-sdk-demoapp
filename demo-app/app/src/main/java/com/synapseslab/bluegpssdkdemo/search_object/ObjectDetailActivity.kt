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

package com.synapseslab.bluegpssdkdemo.search_object

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.synapseslab.bluegps_sdk.data.model.search_object.TrackElement
import com.synapseslab.bluegps_sdk.data.model.search_object.TrackedElement
import com.synapseslab.bluegpssdkdemo.databinding.ActivityObjectDetailBinding
import com.synapseslab.bluegpssdkdemo.view.ViewState


private const val TAG = "ObjectDetailActivity"


class ObjectDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityObjectDetailBinding

    private val viewModel: ObjectDetailViewModel = ObjectDetailViewModel()

    private var trackElement: TrackElement? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityObjectDetailBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setupSubscribers()

        trackElement = if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra("object", TrackElement::class.java)
        } else {
            intent.getParcelableExtra("object")
        }

        supportActionBar?.title = trackElement?.label ?: "Object detail"
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowHomeEnabled(true)
        }

        trackElement?.let {
            viewModel.getTrackElement(trackElement?.id!!)
        }


        binding.btnShowOnMap.setOnClickListener {
            trackElement?.let {
                showResourceOnMap(trackElement!!)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }


    private fun showResourceOnMap(obj: TrackElement) {
        val intent = Intent(this@ObjectDetailActivity, ShowObjectOnMapActivity::class.java)
        intent.putExtra("object", obj)
        startActivity(intent)
    }

    private fun setupSubscribers() {
        viewModel.viewState.observe(this) { state ->
            when(state) {
                is ViewState.Failed -> Log.d(TAG, " FAILED: ${state.message}")
                is ViewState.Loading -> {}
                is ViewState.Success -> {
                    Log.d(TAG, " SUCCESS: ${state.data}")

                    state.data.let {
                        binding.tvDescription.text = (it as TrackedElement).description ?: "No description"
                        binding.tvType.text = it.type ?: "No type"

                        var groups = ""
                        it.groups?.forEach { g ->
                            groups += "${g.label}, "
                        }

                        binding.tvGroups.text = if (groups.isEmpty()) "No groups" else groups

                    }
                }
            }
        }

    }
}