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

package com.synapseslab.bluegpssdkdemo.area

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import com.synapseslab.bluegpssdkdemo.databinding.ActivityAreaBinding
import com.synapseslab.bluegpssdkdemo.view.ViewState

class AreaActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAreaBinding

    private val viewModel: AreaViewModel = AreaViewModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAreaBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        supportActionBar?.title = "Area utilities"

        viewModel.viewState.observe(this) { state ->
            when(state) {
                is ViewState.Failed -> {
                    binding.progressCircular.visibility = View.GONE
                }
                is ViewState.Loading -> {
                    binding.progressCircular.visibility = View.VISIBLE
                }
                is ViewState.Success -> {
                    binding.progressCircular.visibility = View.GONE
                    binding.textLog.text = state.data.toString()
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