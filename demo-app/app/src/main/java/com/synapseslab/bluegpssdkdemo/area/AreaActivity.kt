/*
 * Copyright (c) 2022 Synapses s.r.l.s. All rights reserved.
 * https://synapseslab.com/
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