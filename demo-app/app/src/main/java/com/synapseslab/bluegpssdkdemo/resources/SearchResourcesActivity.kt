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

package com.synapseslab.bluegpssdkdemo.resources

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import com.synapseslab.bluegps_sdk.data.model.map.GenericResource
import com.synapseslab.bluegpssdkdemo.R
import com.synapseslab.bluegpssdkdemo.databinding.ActivitySearchResourcesBinding
import com.synapseslab.bluegpssdkdemo.view.ViewState


private const val TAG = "SearchResourcesActivity"

class SearchResourcesActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchResourcesBinding

    private val viewModel: SearchResourceViewModel = SearchResourceViewModel()

    private lateinit var searchListAdapter: SearchResourceAdapter

    private var searchQuery: String? = null
    private var isDesc = false
    private val order = "name"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchResourcesBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setupSubscribers()

        searchListAdapter =
            SearchResourceAdapter(mutableListOf()) { resource -> showResourceOnMap(resource) }
        binding.list.adapter = searchListAdapter

        supportActionBar?.title = "Search Resources View"
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
            supportActionBar!!.setDisplayShowHomeEnabled(true)
        }

        binding.noElement.visibility = View.VISIBLE
        binding.list.visibility = View.GONE
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> {
                finish()
            }
            R.id.menu_filter -> {
                showFilteringPopUpMenu()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showResourceOnMap(resource: GenericResource) {
        val intent = Intent(this@SearchResourcesActivity, ShowResourceOnMapActivity::class.java)
        intent.putExtra("resource", resource)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.search_resources_menu, menu)
        val searchItem: MenuItem = menu!!.findItem(R.id.searchBar)
        val searchView = searchItem.actionView as SearchView

        searchView.isIconifiedByDefault = true
        searchView.isFocusable = true
        searchView.isIconified = false
        searchView.requestFocusFromTouch()
        searchView.queryHint = "Search a resources"

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                //searchView.clearFocus()
                //searchView.setQuery("", false)
                ///searchItem.collapseActionView()
                //viewModel.setFiltering(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                //viewModel.setFiltering(newText)
                searchQuery = newText
                viewModel.findResources(
                    isDesc = isDesc,
                    search = searchQuery,
                    order = order
                )
                return false
            }
        })

        searchItem.expandActionView()
        menu.findItem(R.id.searchBar)?.expandActionView()
        return super.onCreateOptionsMenu(menu)
    }

    private fun setupSubscribers() {
        viewModel.viewState.observe(this) { state ->
            when (state) {
                is ViewState.Failed -> {
                    Log.d(TAG, " FAILED: ${state.message}")
                }
                is ViewState.Success -> {
                    Log.d(TAG, " SUCCESS: ${state.data}")
                    if ((state.data as List<GenericResource>).isNullOrEmpty()) {
                        binding.noElement.visibility = View.VISIBLE
                        binding.list.visibility = View.GONE
                    } else {
                        searchListAdapter.setList(state.data)
                        binding.noElement.visibility = View.GONE
                        binding.list.visibility = View.VISIBLE
                    }
                }
                else -> {}
            }
        }
    }

    private fun showFilteringPopUpMenu() {
        val view = findViewById<View>(R.id.menu_filter) ?: return
        PopupMenu(this@SearchResourcesActivity, view).run {
            menuInflater.inflate(R.menu.filter_order, menu)

            val increasingItem: MenuItem = menu.findItem(R.id.increasing)
            val decreasingItem: MenuItem = menu.findItem(R.id.decreasing)

            if(isDesc) {
                decreasingItem.isChecked = true
            } else {
                increasingItem.isChecked = false
            }

            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.increasing -> {
                        isDesc = false
                        it.isChecked = !it.isChecked
                    }
                    R.id.decreasing -> {
                        isDesc = true
                        it.isChecked = !it.isChecked
                    }
                    else -> {
                    }
                }
                viewModel.findResources(
                    isDesc = isDesc,
                    order = order,
                    search = searchQuery,
                )
                true
            }
            show()
        }
    }
}