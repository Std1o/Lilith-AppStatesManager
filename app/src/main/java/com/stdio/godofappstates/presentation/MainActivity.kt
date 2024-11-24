package com.stdio.godofappstates.presentation

import android.os.Bundle
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.stdio.godofappstates.R
import com.stdio.godofappstates.domain.operationState.OperationState
import com.stdio.godofappstates.presentation.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val textView = findViewById<TextView>(R.id.text_view)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.lastOperationState.collect { uiState ->
                    progressBar.isVisible = uiState is OperationState.Loading
                    when (uiState) {
                        is OperationState.Empty204 -> {}
                        is OperationState.Error -> textView.text = "Error"
                        is OperationState.Loading -> textView.text = "Loading..."
                        is OperationState.NoState -> {}
                        is OperationState.Success -> textView.text = "Success"
                    }
                }
            }
        }
    }
}