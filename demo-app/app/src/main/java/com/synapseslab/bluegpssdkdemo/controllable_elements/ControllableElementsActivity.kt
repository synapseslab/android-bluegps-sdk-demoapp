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

package com.synapseslab.bluegpssdkdemo.controllable_elements

import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.synapseslab.bluegps_sdk.data.model.controllable_item.ItemControllable
import com.synapseslab.bluegpssdkdemo.view.ViewState
import com.synapseslab.bluegpssdkdemo.databinding.ActivityControllableElementsBinding

private const val TAG = "ControllableActivity"

class ControllableElementsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityControllableElementsBinding

    private val viewModel: ControllableElementsViewModel = ControllableElementsViewModel()

    private lateinit var controllableElementsAdapter: ControllableElementsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityControllableElementsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setupRecyclerView()
        setupSubscribers()


        supportActionBar?.title = "Controllable Elements"
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowHomeEnabled(true)
        }


        viewModel.getControllableItems()

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }


    private fun setupRecyclerView() = binding.recyclerView.apply {
        controllableElementsAdapter = ControllableElementsAdapter(applicationContext)
        adapter = controllableElementsAdapter
        layoutManager = LinearLayoutManager(applicationContext)
    }


    private fun setupSubscribers() {
        viewModel.viewState.observe(this) { state ->
            when(state) {
                is ViewState.Failed -> {
                    Log.d(TAG, " FAILED: ${state.message}")
                    binding.progressCircular.visibility = View.GONE
                }
                is ViewState.Loading -> {
                    binding.progressCircular.visibility = View.VISIBLE
                }
                is ViewState.Success -> {
                    binding.progressCircular.visibility = View.GONE
                    val data = state.data as? List<ItemControllable> ?: return@observe
                    controllableElementsAdapter.list = data.toList()
                }
            }
        }
    }
}