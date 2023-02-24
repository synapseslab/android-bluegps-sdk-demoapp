/*
 * Copyright (c) 2023 Synapses s.r.l.s. All rights reserved.
 * https://synapseslab.com/
 */

package com.synapseslab.bluegpssdkdemo.sse

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import com.synapseslab.bluegpssdkdemo.databinding.ActivityNotifyPositionBinding

class NotifyPositionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNotifyPositionBinding

    private val viewModel: NotifyPositionViewModel = NotifyPositionViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotifyPositionBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        supportActionBar?.title = "Notify position changes"

        viewModel.viewState.observe(this) {
            binding.textLog.text = it
            binding.scrollView.post {
                binding.scrollView.fullScroll(View.FOCUS_DOWN)
            }
        }

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

    override fun onStop() {
        super.onStop()
        viewModel.stopNotifyPositionChanges()
    }
}