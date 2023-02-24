package com.synapseslab.bluegpssdkdemo.sse

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.synapseslab.bluegpssdkdemo.databinding.ActivitySseDiagnosticTagBinding

private const val TAG = "DiagnosticTagActivity"

class DiagnosticTagActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySseDiagnosticTagBinding

    private val viewModel: DiagnosticTagViewModel = DiagnosticTagViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySseDiagnosticTagBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        supportActionBar?.title = "Diagnostic tags"

        viewModel.startDiagnostic()

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
        viewModel.stopDiagnostic()
    }

}